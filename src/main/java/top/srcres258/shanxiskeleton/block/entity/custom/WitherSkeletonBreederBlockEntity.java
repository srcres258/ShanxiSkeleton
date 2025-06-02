package top.srcres258.shanxiskeleton.block.entity.custom;

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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.entity.ModBlockEntityTypes;
import top.srcres258.shanxiskeleton.item.ModItems;
import top.srcres258.shanxiskeleton.screen.custom.WitherSkeletonBreederMenu;
import top.srcres258.shanxiskeleton.util.BlockEntityHelper;
import top.srcres258.shanxiskeleton.util.OutputItemHandler;
import top.srcres258.shanxiskeleton.util.SoundHelper;

public class WitherSkeletonBreederBlockEntity extends BaseMachineBlockEntity {
    public enum ContainerDataType {
        PROGRESS,
        MAX_PROGRESS
    }

    public static final int SEED_INPUT_SLOTS_COUNT = 1;
    public static final int SKELETON_INPUT_SLOTS_COUNT = 2;
    public static final int OUTPUT_SLOTS_COUNT = 3;
    public static final int SLOTS_COUNT = SEED_INPUT_SLOTS_COUNT + SKELETON_INPUT_SLOTS_COUNT + OUTPUT_SLOTS_COUNT;

    public static final int DEFAULT_MAX_PROGRESS = 48000;

    public final ItemStackHandler seedInputItemHandler = new ItemStackHandler(SEED_INPUT_SLOTS_COUNT) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return stack.is(Items.WITHER_ROSE);
        }

        @Override
        public int getSlotLimit(int slot) {
            return Items.WITHER_ROSE.getDefaultMaxStackSize();
        }
    };
    public final ItemStackHandler skeletonInputItemHandler = createSyncSkeletonInputItemHandler(SKELETON_INPUT_SLOTS_COUNT);
    public final ItemStackHandler outputItemHandler = new OutputItemHandler(OUTPUT_SLOTS_COUNT);

    @Getter
    private int progress = 0;
    @Getter
    private int maxProgress = readMaxProgressFromConfig();
    private final ContainerData data = new ContainerData() {
        @Override
        public int get(int index) {
            if (index == ContainerDataType.PROGRESS.ordinal()) {
                return progress;
            } else if (index == ContainerDataType.MAX_PROGRESS.ordinal()) {
                return maxProgress;
            }
            return 0;
        }

        @Override
        public void set(int index, int value) {
            if (index == ContainerDataType.PROGRESS.ordinal()) {
                progress = value;
            } else if (index == ContainerDataType.MAX_PROGRESS.ordinal()) {
                maxProgress = value;
            }
        }

        @Override
        public int getCount() {
            return ContainerDataType.values().length;
        }
    };

    public WitherSkeletonBreederBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        super(ModBlockEntityTypes.WITHER_SKELETON_BREEDER.get(), pos, state);
    }

    private static int readMaxProgressFromConfig() {
        try {
            return ShanxiSkeleton.getInstance().serverConfig.breederMaxTime.get();
        } catch (Exception e) {
            return DEFAULT_MAX_PROGRESS;
        }
    }

    @Override
    @NotNull
    protected ItemStackHandler[] getHandlers() {
        return new ItemStackHandler[] { seedInputItemHandler, skeletonInputItemHandler, outputItemHandler };
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        tag.put("SeedInputItemHandler", seedInputItemHandler.serializeNBT(registries));
        tag.put("SkeletonInputItemHandler", skeletonInputItemHandler.serializeNBT(registries));
        tag.put("OutputItemHandler", outputItemHandler.serializeNBT(registries));
        tag.putInt("Progress", progress);
        tag.putInt("MaxProgress", maxProgress);

        super.saveAdditional(tag, registries);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, @NotNull HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        seedInputItemHandler.deserializeNBT(registries, tag.getCompound("SeedInputItemHandler"));
        skeletonInputItemHandler.deserializeNBT(registries, tag.getCompound("SkeletonInputItemHandler"));
        outputItemHandler.deserializeNBT(registries, tag.getCompound("OutputItemHandler"));
        progress = tag.getInt("Progress");
        maxProgress = tag.getInt("MaxProgress");

        if (progress < 0) {
            progress = 0;
        }
        if (maxProgress <= 0) {
            maxProgress = readMaxProgressFromConfig();
        }
    }

    @Override
    public void tick(@NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState state) {
        boolean changed, playAmbient;

        changed = false;
        playAmbient = true;

        if (hasEnoughWitherSkeletonsForBreeding() && hasEnoughSeedsForBreeding()) {
            if (canIncreaseProgress()) {
                increaseProgress();
                changed = true;
            }

            if (hasCraftingFinished()) {
                craftItem();
                resetProgress();
                changed = true;

                playBreedingFinishedSound();
                playAmbient = false;
            }
        } else {
            decreaseProgress();
            changed = true;
        }

        if (changed) {
            setChanged(level, pos, state);
        }

        if (hasWitherSkeleton() && playAmbient) {
            playRandomAmbientSound();
        }
    }

    private boolean hasEnoughWitherSkeletonsForBreeding() {
        ItemStack stack;

        stack = skeletonInputItemHandler.getStackInSlot(0);

        return !stack.isEmpty() && stack.getCount() >= 2;
    }

    private boolean hasEnoughSeedsForBreeding() {
        ItemStack stack;

        stack = seedInputItemHandler.getStackInSlot(0);

        return !stack.isEmpty() && stack.getCount() >= 2;
    }

    private boolean canIncreaseProgress() {
        int i;
        boolean result;
        ItemStack stack;

        if (progress >= maxProgress) {
            return false;
        }

        if (seedInputItemHandler.getStackInSlot(0).isEmpty()) {
            return false;
        }
        if (skeletonInputItemHandler.getStackInSlot(0).isEmpty()) {
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
            progress = Math.min(progress + getWitherSkeletonCount(), maxProgress);
        }
    }

    private void decreaseProgress() {
        if (progress > 0) {
            progress = Math.max(progress - getWitherSkeletonCount(), 0);
        }
    }

    private boolean hasCraftingFinished() {
        return progress >= maxProgress;
    }

    private void craftItem() {
        ItemStack consumed;

        consumed = seedInputItemHandler.extractItem(0, 2, false);
        if (!consumed.isEmpty() && consumed.getCount() >= 2) {
            BlockEntityHelper.putItemStackIntoOutputItemHandler(new ItemStack(ModItems.WITHER_SKELETON.get()),
                    outputItemHandler);
        }
    }

    private void resetProgress() {
        progress = 0;
        maxProgress = readMaxProgressFromConfig();
    }

    public int getWitherSkeletonCount() {
        int total, i;

        total = 0;
        for (i = 0; i < skeletonInputItemHandler.getSlots(); i++) {
            total += skeletonInputItemHandler.getStackInSlot(i).getCount();
        }

        return total;
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
                return Component.translatable("block.shanxiskeleton.wither_skeleton_breeder");
            }

            @Override
            @NotNull
            public AbstractContainerMenu createMenu(
                    int containerId,
                    @NotNull Inventory playerInventory,
                    @NotNull Player player
            ) {
                return new WitherSkeletonBreederMenu(containerId, playerInventory, player.level(),
                        WitherSkeletonBreederBlockEntity.this, data);
            }
        };
    }

    @Override
    @NotNull
    public IItemHandler getMachineInputItemHandler() {
        return new CombinedInvWrapper(seedInputItemHandler, skeletonInputItemHandler);
    }

    @Override
    @NotNull
    public IItemHandler getMachineOutputItemHandler() {
        return outputItemHandler;
    }

    public boolean hasWitherSkeleton() {
        return !skeletonInputItemHandler.getStackInSlot(0).isEmpty();
    }

    public boolean hasSeed() {
        return !seedInputItemHandler.getStackInSlot(0).isEmpty();
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

    private void playBreedingFinishedSound() {
        if (level != null) {
            SoundHelper.playSoundAtBlock(level, worldPosition, SoundEvents.CHICKEN_EGG);
        }
    }
}
