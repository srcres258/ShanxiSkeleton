package top.srcres258.shanxiskeleton.screen.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.srcres258.shanxiskeleton.block.ModBlocks;
import top.srcres258.shanxiskeleton.block.custom.WitherSkeletonSlaughtererBlock;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonSlaughtererBlockEntity;
import top.srcres258.shanxiskeleton.screen.ModMenuTypes;
import top.srcres258.shanxiskeleton.util.ContainerMenuHelper;
import top.srcres258.shanxiskeleton.util.RenderHelper;

public class WitherSkeletonSlaughtererMenu extends BaseMachineMenu<WitherSkeletonSlaughtererBlockEntity> {
    // [quickMoveStack]
    // THIS YOU HAVE TO DEFINE!
    private static final int BE_INVENTORY_SLOT_COUNT = WitherSkeletonSlaughtererBlockEntity.SLOTS_COUNT;

    public WitherSkeletonSlaughtererMenu(
            int containerId,
            @NotNull Inventory inv,
            @NotNull Level level,
            @NotNull WitherSkeletonSlaughtererBlockEntity blockEntity,
            @NotNull ContainerData data
    ) {
        super(ModMenuTypes.WITHER_SKELETON_SLAUGHTERER.get(),
                ((WitherSkeletonSlaughtererBlock) ModBlocks.WITHER_SKELETON_SLAUGHTERER.get()),
                BE_INVENTORY_SLOT_COUNT, containerId, inv, level, blockEntity, data);
    }

    public WitherSkeletonSlaughtererMenu(
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
    private static WitherSkeletonSlaughtererBlockEntity tempBlockEntity = null;

    @NotNull
    private static WitherSkeletonSlaughtererBlockEntity readBlockEntity(
            @NotNull Level level,
            @NotNull FriendlyByteBuf extraData
    ) {
        if (tempBlockEntity == null) {
            tempBlockEntity = ((WitherSkeletonSlaughtererBlockEntity) level.getBlockEntity(extraData.readBlockPos()));
        }

        return tempBlockEntity;
    }

    @Override
    protected void addPlayerInventorySlots(@NotNull Inventory inv) {
        ContainerMenuHelper.addPlayerInventorySlots(inv, 8, 51, this::addSlot);
    }

    @Override
    protected void addPlayerHotbarSlots(@NotNull Inventory inv) {
        ContainerMenuHelper.addPlayerHotbarSlots(inv, 8, 109, this::addSlot);
    }

    @Override
    protected void addMachineSlots() {
        addMachineInputSlots();
        addMachineOutputSlots();
    }

    private void addMachineInputSlots() {
        addSlot(new SlotItemHandler(blockEntity.inputItemHandler, 0, 26, 20));
    }

    private void addMachineOutputSlots() {
        int i;

        for (i = 0; i < blockEntity.outputItemHandler.getSlots(); i++) {
            addSlot(new SlotItemHandler(blockEntity.outputItemHandler, i, 80 + i * 18, 20));
        }
    }

    public boolean isCrafting() {
        return data.get(WitherSkeletonSlaughtererBlockEntity.ContainerDataType.PROGRESS.ordinal()) > 0;
    }

    public int getScaledProgress() {
        return RenderHelper.getScaledProgress(data.get(WitherSkeletonSlaughtererBlockEntity.ContainerDataType.PROGRESS.ordinal()),
                data.get(WitherSkeletonSlaughtererBlockEntity.ContainerDataType.MAX_PROGRESS.ordinal()),
                WitherSkeletonSlaughtererScreen.PROGRESS_BAR_PIXEL_WIDTH);
    }

    public float getProgress() {
        int progress, maxProgress;

        progress = data.get(WitherSkeletonSlaughtererBlockEntity.ContainerDataType.PROGRESS.ordinal());
        maxProgress = data.get(WitherSkeletonSlaughtererBlockEntity.ContainerDataType.MAX_PROGRESS.ordinal());

        return (float) progress / maxProgress;
    }
}
