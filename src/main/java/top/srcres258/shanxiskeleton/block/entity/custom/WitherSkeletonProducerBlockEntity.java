package top.srcres258.shanxiskeleton.block.entity.custom;

import com.google.common.base.Suppliers;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.entity.ModBlockEntityTypes;
import top.srcres258.shanxiskeleton.item.ModItems;
import top.srcres258.shanxiskeleton.screen.custom.WitherSkeletonProducerMenu;
import top.srcres258.shanxiskeleton.util.BlockEntityHelper;
import top.srcres258.shanxiskeleton.util.OutputItemHandler;
import top.srcres258.shanxiskeleton.util.SoundHelper;

import java.util.function.Supplier;

public class WitherSkeletonProducerBlockEntity extends BaseMachineBlockEntity {
    public enum ContainerDataType {
        PROGRESS,
        MAX_PROGRESS,
        ROSE_PROGRESS,
        ROSE_MAX_PROGRESS
    }

    public static final int INPUT_SLOTS_COUNT = 1;
    public static final int OUTPUT_SLOTS_COUNT = 4;
    public static final int ROSE_INPUT_SLOTS_COUNT = 1;
    public static final int ROSE_OUTPUT_SLOTS_COUNT = 4;
    public static final int SLOTS_COUNT = INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT + ROSE_INPUT_SLOTS_COUNT +
            ROSE_OUTPUT_SLOTS_COUNT;

    public static final int DEFAULT_MAX_PROGRESS = 12000;

    public static final Supplier<Block[]> DEFAULT_ROSES = Suppliers.memoize(() -> new Block[] {
            /*
             * 截至 Minecraft Java Edition 1.21.1，共有小型花的列表如下：
             * （注意随着版本更新，需要及时修改此列表）
             */

            Blocks.DANDELION,           // 蒲公英
            Blocks.POPPY,               // 虞美人
            Blocks.BLUE_ORCHID,         // 兰花
            Blocks.ALLIUM,              // 绒球葱
            Blocks.AZURE_BLUET,         // 蓝花美耳草
            Blocks.RED_TULIP,           // 红色郁金香
            Blocks.ORANGE_TULIP,        // 橙色郁金香
            Blocks.WHITE_TULIP,         // 白色郁金香
            Blocks.PINK_TULIP,          // 粉红色郁金香
            Blocks.OXEYE_DAISY,         // 滨菊
            Blocks.CORNFLOWER,          // 矢车菊
            Blocks.LILY_OF_THE_VALLEY,  // 铃兰
//            Blocks.WITHER_ROSE,         // 凋灵玫瑰（排除了，因为目标产物是这个）
            Blocks.TORCHFLOWER          // 火把花
    });

    public final ItemStackHandler inputItemHandler = createSyncSkeletonInputItemHandler(INPUT_SLOTS_COUNT);
    public final ItemStackHandler outputItemHandler = new OutputItemHandler(OUTPUT_SLOTS_COUNT);
    public final ItemStackHandler roseInputItemHandler = new ItemStackHandler(ROSE_INPUT_SLOTS_COUNT) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return ShanxiSkeleton.getInstance().serverConfig.getProducerRoseTypes().stream()
                    .anyMatch(tag -> tag.contains(stack.getItem()));
        }

        @Override
        public int getSlotLimit(int slot) {
            return Item.DEFAULT_MAX_STACK_SIZE;
        }
    };
    public final ItemStackHandler roseOutputItemHandler = new OutputItemHandler(ROSE_OUTPUT_SLOTS_COUNT);

    @Getter
    private int progress = 0;
    @Getter
    private int maxProgress = readMaxProgressFromConfig();
    @Getter
    private int roseProgress = 0;
    @Getter
    private int roseMaxProgress = readMaxProgressFromConfig();
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            if (index == ContainerDataType.PROGRESS.ordinal()) {
                return progress;
            } else if (index == ContainerDataType.MAX_PROGRESS.ordinal()) {
                return maxProgress;
            } else if (index == ContainerDataType.ROSE_PROGRESS.ordinal()) {
                return roseProgress;
            } else if (index == ContainerDataType.ROSE_MAX_PROGRESS.ordinal()) {
                return roseMaxProgress;
            }
            return 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == ContainerDataType.PROGRESS.ordinal()) {
                progress = value;
            } else if (index == ContainerDataType.MAX_PROGRESS.ordinal()) {
                maxProgress = value;
            } else if (index == ContainerDataType.ROSE_PROGRESS.ordinal()) {
                roseProgress = value;
            } else if (index == ContainerDataType.ROSE_MAX_PROGRESS.ordinal()) {
                roseMaxProgress = value;
            }
        }

        @Override
        public int getCount() {
            return ContainerDataType.values().length;
        }
    };

    public WitherSkeletonProducerBlockEntity(@NotNull BlockPos pos, @NotNull BlockState blockState) {
        super(ModBlockEntityTypes.WITHER_SKELETON_PRODUCER.get(), pos, blockState);
    }

    @Override
    @NotNull
    protected ItemStackHandler[] getHandlers() {
        return new ItemStackHandler[] { inputItemHandler, outputItemHandler, roseInputItemHandler,
                roseOutputItemHandler };
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        tag.put("InputStackHandler", inputItemHandler.serializeNBT(registries));
        tag.put("OutputStackHandler", outputItemHandler.serializeNBT(registries));
        tag.put("RoseInputStackHandler", roseInputItemHandler.serializeNBT(registries));
        tag.put("RoseOutputStackHandler", roseOutputItemHandler.serializeNBT(registries));
        tag.putInt("Progress", progress);
        tag.putInt("MaxProgress", maxProgress);
        tag.putInt("RoseProgress", roseProgress);
        tag.putInt("RoseMaxProgress", roseMaxProgress);

        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        inputItemHandler.deserializeNBT(registries, tag.getCompound("InputStackHandler"));
        outputItemHandler.deserializeNBT(registries, tag.getCompound("OutputStackHandler"));
        roseInputItemHandler.deserializeNBT(registries, tag.getCompound("RoseInputStackHandler"));
        roseOutputItemHandler.deserializeNBT(registries, tag.getCompound("RoseOutputStackHandler"));
        progress = tag.getInt("Progress");
        maxProgress = tag.getInt("MaxProgress");
        roseProgress = tag.getInt("RoseProgress");
        roseMaxProgress = tag.getInt("RoseMaxProgress");

        if (progress < 0) {
            progress = 0;
        }
        if (roseProgress < 0) {
            roseProgress = 0;
        }
        if (maxProgress <= 0) {
            maxProgress = readMaxProgressFromConfig();
        }
        if (roseMaxProgress <= 0) {
            roseMaxProgress = readMaxProgressFromConfig();
        }
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state) {
        boolean changed;

        changed = false;

        if (hasWitherSkeleton()) {
            if (canIncreaseProgress()) {
                increaseProgress();
                changed = true;
            }
            if (canIncreaseRoseProgress()) {
                increaseRoseProgress();
                changed = true;
            } else if (!hasRose()) {
                decreaseRoseProgress();
                changed = true;
            }

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
                changed = true;
            }
            if (hasRoseCraftingFinished()) {
                craftRoseItem();
                resetRoseProgress();
                changed = true;
            }
        } else {
            decreaseProgress();
            decreaseRoseProgress();
            changed = true;
        }

        if (changed) {
            setChanged(level, pos, state);
        }

        if (hasWitherSkeleton()) {
            playRandomAmbientSound();
        }
    }

    public boolean hasWitherSkeleton() {
        return !inputItemHandler.getStackInSlot(0).isEmpty();
    }

    public boolean hasRose() {
        return !roseInputItemHandler.getStackInSlot(0).isEmpty();
    }

    private boolean canIncreaseProgress() {
        int i;
        boolean result;

        if (progress >= maxProgress) {
            return false;
        }

        if (inputItemHandler.getStackInSlot(0).isEmpty()) {
            return false;
        }

        // 如果输出槽位已满（都不为空），则无法继续生产。
        result = false;
        for (i = 0; i < outputItemHandler.getSlots(); i++) {
            result = result || outputItemHandler.getStackInSlot(i).isEmpty();
        }

        return result;
    }

    private boolean canIncreaseRoseProgress() {
        int i;
        boolean result;

        if (roseProgress >= roseMaxProgress) {
            return false;
        }

        if (roseInputItemHandler.getStackInSlot(0).isEmpty()) {
            return false;
        }

        // 判断输出槽位是否已满。如果输出槽位已满，则无法继续生产。
        result = false;
        for (i = 0; i < roseOutputItemHandler.getSlots(); i++) {
            result = result || roseOutputItemHandler.getStackInSlot(i).isEmpty();
        }

        return result;
    }

    private void increaseProgress() {
        if (progress < maxProgress) {
            progress = Math.min(progress + getWitherSkeletonCount(), maxProgress);
        }
    }

    private void increaseRoseProgress() {
        if (roseProgress < roseMaxProgress) {
            roseProgress = Math.min(roseProgress + getWitherSkeletonCount(), roseMaxProgress);
        }
    }

    private int getWitherSkeletonCount() {
        return inputItemHandler.getStackInSlot(0).getCount();
    }

    private void decreaseProgress() {
        if (progress > 0) {
            progress = Math.max(progress - getWitherSkeletonCount(), 0);
        }
    }

    private void decreaseRoseProgress() {
        if (roseProgress > 0) {
            roseProgress = Math.max(roseProgress - getWitherSkeletonCount(), 0);
        }
    }

    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    private boolean hasRoseCraftingFinished() {
        return roseProgress >= roseMaxProgress;
    }

    private void resetProgress() {
        progress = 0;
        maxProgress = readMaxProgressFromConfig();
    }

    private void resetRoseProgress() {
        roseProgress = 0;
        roseMaxProgress = readMaxProgressFromConfig();
    }

    private void craftItem() {
        ItemStack output;

        output = genCoalOutput();
        BlockEntityHelper.putItemStackIntoOutputItemHandler(output, outputItemHandler);
        output = genTinyCoalOutput();
        BlockEntityHelper.putItemStackIntoOutputItemHandler(output, outputItemHandler);
    }

    private void craftRoseItem() {
        ItemStack input;

        input = roseInputItemHandler.extractItem(0, 1, false);
        if (input.isEmpty()) {
            return;
        }
        BlockEntityHelper.putItemStackIntoOutputItemHandler(new ItemStack(Items.WITHER_ROSE), roseOutputItemHandler);
    }

    @NotNull
    private ItemStack genCoalOutput() {
        int producerCoalMaxAmount;

        producerCoalMaxAmount = ShanxiSkeleton.getInstance().serverConfig.producerCoalMaxAmount.get();
        return new ItemStack(Items.COAL, getRandom().nextInt(producerCoalMaxAmount + 1));
    }

    @NotNull
    private ItemStack genTinyCoalOutput() {
        int producerTinyCoalMaxAmount;

        producerTinyCoalMaxAmount = ShanxiSkeleton.getInstance().serverConfig.producerTinyCoalMaxAmount.get();
        return new ItemStack(ModItems.TINY_COAL.get(), getRandom().nextInt(producerTinyCoalMaxAmount + 1));
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
                return Component.translatable("block.shanxiskeleton.wither_skeleton_producer");
            }

            @Override
            @NotNull
            public AbstractContainerMenu createMenu(
                    int containerId,
                    @NotNull Inventory playerInventory,
                    @NotNull Player player
            ) {
                return new WitherSkeletonProducerMenu(containerId, playerInventory, player.level(),
                        WitherSkeletonProducerBlockEntity.this, data);
            }
        };
    }

    @Override
    @NotNull
    public IItemHandler getMachineInputItemHandler() {
        return new CombinedInvWrapper(inputItemHandler, roseInputItemHandler);
    }

    @Override
    @NotNull
    public IItemHandler getMachineOutputItemHandler() {
        return new CombinedInvWrapper(outputItemHandler, roseOutputItemHandler);
    }

    private static int readMaxProgressFromConfig() {
        try {
            return ShanxiSkeleton.getInstance().serverConfig.producerMaxTime.get();
        } catch (Exception e) {
            return DEFAULT_MAX_PROGRESS;
        }
    }

    @Override
    @NotNull
    public ContainerData getContainerData() {
        return data;
    }

    private void playRandomAmbientSound() {
        if (level != null && SoundHelper.canPlayWitherSkeletonAmbientSound(level)) {
            SoundHelper.playSoundAtBlock(level, worldPosition, SoundEvents.WITHER_SKELETON_AMBIENT);
        }
    }
}
