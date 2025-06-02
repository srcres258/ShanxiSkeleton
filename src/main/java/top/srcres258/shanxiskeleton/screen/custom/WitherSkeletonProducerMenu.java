package top.srcres258.shanxiskeleton.screen.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.srcres258.shanxiskeleton.block.ModBlocks;
import top.srcres258.shanxiskeleton.block.custom.WitherSkeletonProducerBlock;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonProducerBlockEntity;
import top.srcres258.shanxiskeleton.screen.ModMenuTypes;
import top.srcres258.shanxiskeleton.util.ContainerMenuHelper;
import top.srcres258.shanxiskeleton.util.RenderHelper;

import java.util.Objects;

public class WitherSkeletonProducerMenu extends BaseMachineMenu<WitherSkeletonProducerBlockEntity> {
    // [quickMoveStack]
    // THIS YOU HAVE TO DEFINE!
    private static final int BE_INVENTORY_SLOT_COUNT = WitherSkeletonProducerBlockEntity.SLOTS_COUNT;

    public WitherSkeletonProducerMenu(
            int containerId,
            @NotNull Inventory inv,
            @NotNull Level level,
            @NotNull WitherSkeletonProducerBlockEntity blockEntity,
            @NotNull ContainerData data
    ) {
        super(ModMenuTypes.WITHER_SKELETON_PRODUCER.get(),
                ((WitherSkeletonProducerBlock) ModBlocks.WITHER_SKELETON_PRODUCER.get()),
                BE_INVENTORY_SLOT_COUNT, containerId, inv, level, blockEntity, data);
    }

    public WitherSkeletonProducerMenu(
            int containerId,
            @NotNull Inventory inv,
            @NotNull Level level,
            @NotNull FriendlyByteBuf extraData
    ) {
        this(containerId, inv, level, readBlockEntity(level, extraData),
                readBlockEntity(level, extraData).getContainerData());

        tempBlockEntity = null;
    }

    @Nullable
    private static WitherSkeletonProducerBlockEntity tempBlockEntity = null;

    @NotNull
    private static WitherSkeletonProducerBlockEntity readBlockEntity(
            @NotNull Level level,
            @NotNull FriendlyByteBuf extraData
    ) {
        if (tempBlockEntity == null) {
            tempBlockEntity = ((WitherSkeletonProducerBlockEntity) level.getBlockEntity(extraData.readBlockPos()));
        }

        return Objects.requireNonNull(tempBlockEntity);
    }

    @Override
    protected void addPlayerInventorySlots(@NotNull Inventory inv) {
        ContainerMenuHelper.addPlayerInventorySlots(inv, 8, 64, this::addSlot);
    }

    @Override
    protected void addPlayerHotbarSlots(@NotNull Inventory inv) {
        ContainerMenuHelper.addPlayerHotbarSlots(inv, 8, 122, this::addSlot);
    }

    @Override
    protected void addMachineSlots() {
        addMachineInputSlots();
        addMachineOutputSlots();
    }

    private void addMachineInputSlots() {
        addSlot(new SlotItemHandler(blockEntity.inputItemHandler, 0, 26, 20));
        addSlot(new SlotItemHandler(blockEntity.roseInputItemHandler, 0, 26, 39));
    }

    private void addMachineOutputSlots() {
        int left, top, i;

        left = 80;
        top = 20;
        for (i = 0; i < WitherSkeletonProducerBlockEntity.OUTPUT_SLOTS_COUNT; i++) {
            addSlot(new SlotItemHandler(blockEntity.outputItemHandler, i, left + i * 18, top));
        }
        top = 39;
        for (i = 0; i < WitherSkeletonProducerBlockEntity.ROSE_OUTPUT_SLOTS_COUNT; i++) {
            addSlot(new SlotItemHandler(blockEntity.roseOutputItemHandler, i, left + i * 18, top));
        }
    }

    public boolean isCrafting() {
        return data.get(WitherSkeletonProducerBlockEntity.ContainerDataType.PROGRESS.ordinal()) > 0 ||
                data.get(WitherSkeletonProducerBlockEntity.ContainerDataType.ROSE_PROGRESS.ordinal()) > 0;
    }

    public int getScaledProgress() {
        return RenderHelper.getScaledProgress(data.get(WitherSkeletonProducerBlockEntity.ContainerDataType.PROGRESS.ordinal()),
                data.get(WitherSkeletonProducerBlockEntity.ContainerDataType.MAX_PROGRESS.ordinal()),
                WitherSkeletonProducerScreen.PROGRESS_BAR_PIXEL_WIDTH);
    }

    public int getScaledRoseProgress() {
        return RenderHelper.getScaledProgress(data.get(WitherSkeletonProducerBlockEntity.ContainerDataType.ROSE_PROGRESS.ordinal()),
                data.get(WitherSkeletonProducerBlockEntity.ContainerDataType.ROSE_MAX_PROGRESS.ordinal()),
                WitherSkeletonProducerScreen.PROGRESS_BAR_PIXEL_WIDTH);
    }

    public float getProgress() {
        int progress, maxProgress;

        progress = data.get(WitherSkeletonProducerBlockEntity.ContainerDataType.PROGRESS.ordinal());
        maxProgress = data.get(WitherSkeletonProducerBlockEntity.ContainerDataType.MAX_PROGRESS.ordinal());

        return (float) progress / maxProgress;
    }

    public float getRoseProgress() {
        int progress, maxProgress;

        progress = data.get(WitherSkeletonProducerBlockEntity.ContainerDataType.ROSE_PROGRESS.ordinal());
        maxProgress = data.get(WitherSkeletonProducerBlockEntity.ContainerDataType.ROSE_MAX_PROGRESS.ordinal());

        return (float) progress / maxProgress;
    }
}
