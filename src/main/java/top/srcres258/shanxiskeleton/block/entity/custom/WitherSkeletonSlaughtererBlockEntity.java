package top.srcres258.shanxiskeleton.block.entity.custom;

import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.entity.ModBlockEntityTypes;
import top.srcres258.shanxiskeleton.item.ModItems;
import top.srcres258.shanxiskeleton.network.custom.ClientboundResetMachineHurtTimePayload;
import top.srcres258.shanxiskeleton.screen.custom.WitherSkeletonSlaughtererMenu;
import top.srcres258.shanxiskeleton.util.BlockEntityHelper;
import top.srcres258.shanxiskeleton.util.OutputItemHandler;
import top.srcres258.shanxiskeleton.util.SoundHelper;

public class WitherSkeletonSlaughtererBlockEntity extends BaseMachineBlockEntity {
    public enum ContainerDataType {
        PROGRESS,
        MAX_PROGRESS,
        TIMER
    }

    public static final int INPUT_SLOTS_COUNT = 1;
    public static final int OUTPUT_SLOTS_COUNT = 4;
    public static final int SLOTS_COUNT = INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;

    public static final int DEFAULT_MAX_PROGRESS = 160;

    public final ItemStackHandler inputItemHandler = createSyncSkeletonInputItemHandler(INPUT_SLOTS_COUNT);
    public final ItemStackHandler outputItemHandler = new OutputItemHandler(OUTPUT_SLOTS_COUNT);

    @Getter
    private int progress = 0;
    @Getter
    private int maxProgress = readMaxProgressFromConfig();
    private int timer = 0;
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            if (index == ContainerDataType.PROGRESS.ordinal()) {
                return progress;
            } else if (index == ContainerDataType.MAX_PROGRESS.ordinal()) {
                return maxProgress;
            } else if (index == ContainerDataType.TIMER.ordinal()) {
                return timer;
            }
            return 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == ContainerDataType.PROGRESS.ordinal()) {
                progress = value;
            } else if (index == ContainerDataType.MAX_PROGRESS.ordinal()) {
                maxProgress = value;
            } else if (index == ContainerDataType.TIMER.ordinal()) {
                timer = value;
            }
        }

        @Override
        public int getCount() {
            return ContainerDataType.values().length;
        }
    };

    /**
     * 仅用于客户端，记录当前生物的受伤时间。
     */
    public int witherSkeletonHurtTime = 0;

    public WitherSkeletonSlaughtererBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        super(ModBlockEntityTypes.WITHER_SKELETON_SLAUGHTERER.get(), pos, state);
    }

    private static int readMaxProgressFromConfig() {
        try {
            return ShanxiSkeleton.getInstance().serverConfig.slaughtererMaxTime.get();
        } catch (Exception e) {
            return DEFAULT_MAX_PROGRESS;
        }
    }

    @Override
    @NotNull
    protected ItemStackHandler[] getHandlers() {
        return new ItemStackHandler[] { inputItemHandler, outputItemHandler };
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        tag.put("InputItemHandler", inputItemHandler.serializeNBT(registries));
        tag.put("OutputItemHandler", outputItemHandler.serializeNBT(registries));
        tag.putInt("Progress", progress);
        tag.putInt("MaxProgress", maxProgress);
        tag.putInt("Timer", timer);

        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        inputItemHandler.deserializeNBT(registries, tag.getCompound("InputItemHandler"));
        outputItemHandler.deserializeNBT(registries, tag.getCompound("OutputItemHandler"));
        progress = tag.getInt("Progress");
        maxProgress = tag.getInt("MaxProgress");
        timer = tag.getInt("Timer");

        if (progress < 0) {
            progress = 0;
        }
        if (maxProgress <= 0) {
            maxProgress = readMaxProgressFromConfig();
        }
        if (timer < 0) {
            timer = 0;
        }
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state) {
        boolean changed;

        changed = false;

        if (hasInput()) {
            if (canIncreaseProgress()) {
                increaseProgress();
                changed = true;
            }

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
                changed = true;

                syncInputChangeToClient();
                playDeathSound();
            }

            updateTimer();
            if (timer == 0 && isWorking()) {
                sendWitherSkeletonHurtPayloadToClient();
                playHurtSound();
            }
        } else {
            decreaseProgress();
            changed = true;

            resetTimer();
        }

        if (changed) {
            setChanged(level, pos, state);
        }
    }

    public boolean hasInput() {
        return !inputItemHandler.getStackInSlot(0).isEmpty();
    }

    private boolean canIncreaseProgress() {
        int i;
        boolean result;
        ItemStack stack;

        if (progress >= maxProgress) {
            return false;
        }

        if (inputItemHandler.getStackInSlot(0).isEmpty()) {
            return false;
        }

        // 如果输出槽位已满（都不为空且堆叠数达到最大），则无法继续生产。
        result = false;
        for (i = 0; i < outputItemHandler.getSlots(); i++) {
            stack = outputItemHandler.getStackInSlot(i);
            result = result || stack.isEmpty() || stack.getCount() < stack.getMaxStackSize();
        }

        return result;
    }

    private void increaseProgress() {
        if (progress < maxProgress) {
            progress = Math.min(progress + 1, maxProgress);
        }
    }

    private void decreaseProgress() {
        if (progress > 0) {
            progress = Math.max(progress - 1, 0);
        }
    }

    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    private void craftItem() {
        ItemStack consumed, output;

        consumed = inputItemHandler.extractItem(0, 1, false);
        if (!consumed.isEmpty()) {
            output = genSkullOutput();
            if (!output.isEmpty()) {
                BlockEntityHelper.putItemStackIntoOutputItemHandler(output, outputItemHandler);
            }
            output = genBoneOutput();
            if (!output.isEmpty()) {
                BlockEntityHelper.putItemStackIntoOutputItemHandler(output, outputItemHandler);
            }
            output = genCoalOutput();
            if (!output.isEmpty()) {
                BlockEntityHelper.putItemStackIntoOutputItemHandler(output, outputItemHandler);
            }
            output = genTinyCoalOutput();
            if (!output.isEmpty()) {
                BlockEntityHelper.putItemStackIntoOutputItemHandler(output, outputItemHandler);
            }
        }
    }

    @NotNull
    private ItemStack genSkullOutput() {
        RandomSource rand;
        double chanceBound;

        rand = getRandom();
        chanceBound = ShanxiSkeleton.getInstance().serverConfig.slaughtererSkullChance.get();
        if (rand.nextDouble() < chanceBound) {
            return new ItemStack(Items.WITHER_SKELETON_SKULL);
        }
        return ItemStack.EMPTY;
    }

    @NotNull
    private ItemStack genBoneOutput() {
        int boneMaxAmount;

        boneMaxAmount = ShanxiSkeleton.getInstance().serverConfig.slaughtererBoneMaxAmount.get();
        return new ItemStack(Items.BONE, getRandom().nextInt(boneMaxAmount + 1));
    }

    @NotNull
    private ItemStack genCoalOutput() {
        int coalMaxAmount;

        coalMaxAmount = ShanxiSkeleton.getInstance().serverConfig.slaughtererCoalMaxAmount.get();
        return new ItemStack(Items.COAL, getRandom().nextInt(coalMaxAmount + 1));
    }

    @NotNull
    private ItemStack genTinyCoalOutput() {
        int tinyCoalMaxAmount;

        tinyCoalMaxAmount = ShanxiSkeleton.getInstance().serverConfig.slaughtererTinyCoalMaxAmount.get();
        return new ItemStack(ModItems.TINY_COAL.get(), getRandom().nextInt(tinyCoalMaxAmount + 1));
    }

    private void resetProgress() {
        progress = 0;
        maxProgress = readMaxProgressFromConfig();
    }

    public int getWitherSkeletonCount() {
        ItemStack stack;

        stack = inputItemHandler.getStackInSlot(0);

        return stack.isEmpty() ? 0 :inputItemHandler.getStackInSlot(0).getCount();
    }

    public int getMaxWitherSkeletonCount() {
        return ModItems.WITHER_SKELETON.get().getDefaultMaxStackSize();
    }

    @Override
    public boolean hasMenu() {
        return true;
    }

    @Override
    @NotNull
    public MenuProvider createMenuProvider() {
        return new MenuProvider() {
            @Override
            @NotNull
            public Component getDisplayName() {
                return Component.translatable("block.shanxiskeleton.wither_skeleton_slaughterer");
            }

            @Override
            @NotNull
            public AbstractContainerMenu createMenu(
                    int containerId,
                    @NotNull Inventory playerInventory,
                    @NotNull Player player
            ) {
                return new WitherSkeletonSlaughtererMenu(containerId, playerInventory, player.level(),
                        WitherSkeletonSlaughtererBlockEntity.this, data);
            }
        };
    }

    @Override
    @NotNull
    public IItemHandler getMachineInputItemHandler() {
        return inputItemHandler;
    }

    @Override
    @NotNull
    public IItemHandler getMachineOutputItemHandler() {
        return outputItemHandler;
    }

    public boolean isWorking() {
        return hasInput() && canIncreaseProgress() && !hasCraftingFinished();
    }

    private void sendWitherSkeletonHurtPayloadToClient() {
        ClientboundResetMachineHurtTimePayload.sendToAllPlayers(worldPosition, 20);
    }

    private void updateTimer() {
        timer = (timer + 1) % 40;
    }

    private void resetTimer() {
        timer = 0;
    }

    @Override
    @NotNull
    public ContainerData getContainerData() {
        return data;
    }

    private void playHurtSound() {
        if (level != null) {
            SoundHelper.playSoundAtBlock(level, worldPosition, SoundEvents.WITHER_SKELETON_HURT);
        }
    }

    private void playDeathSound() {
        if (level != null) {
            SoundHelper.playSoundAtBlock(level, worldPosition, SoundEvents.WITHER_SKELETON_DEATH);
        }
    }
}
