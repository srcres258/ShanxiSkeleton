package top.srcres258.shanxiskeleton.screen.custom;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.srcres258.shanxiskeleton.block.ModBlocks;
import top.srcres258.shanxiskeleton.block.custom.WitherSkeletonBreederBlock;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonBreederBlockEntity;
import top.srcres258.shanxiskeleton.screen.ModMenuTypes;
import top.srcres258.shanxiskeleton.util.ContainerMenuHelper;
import top.srcres258.shanxiskeleton.util.RenderHelper;

import java.util.Objects;

public class WitherSkeletonBreederMenu extends BaseMachineMenu<WitherSkeletonBreederBlockEntity> {
    // [quickMoveStack]
    // THIS YOU HAVE TO DEFINE!
    private static final int BE_INVENTORY_SLOT_COUNT = WitherSkeletonBreederBlockEntity.SLOTS_COUNT;

    public WitherSkeletonBreederMenu(
            int containerId,
            @NotNull Inventory inv,
            @NotNull Level level,
            @NotNull WitherSkeletonBreederBlockEntity blockEntity,
            @NotNull ContainerData data
    ) {
        super(ModMenuTypes.WITHER_SKELETON_BREEDER.get(),
                ((WitherSkeletonBreederBlock) ModBlocks.WITHER_SKELETON_BREEDER.get()),
                BE_INVENTORY_SLOT_COUNT, containerId, inv, level, blockEntity, data);
    }

    public WitherSkeletonBreederMenu(
            int containerId,
            @NotNull Inventory inv,
            @NotNull Level level,
            @NotNull FriendlyByteBuf extraData
    ) {
        this(containerId, inv, level, readBlockEntity(level, extraData), readBlockEntity(level, extraData).getContainerData());

        tempBlockEntity = null;
    }

    @Nullable
    private static WitherSkeletonBreederBlockEntity tempBlockEntity = null;

    @NotNull
    private static WitherSkeletonBreederBlockEntity readBlockEntity(
            @NotNull Level level,
            @NotNull FriendlyByteBuf extraData
    ) {
        if (tempBlockEntity == null) {
            tempBlockEntity = ((WitherSkeletonBreederBlockEntity) level.getBlockEntity(extraData.readBlockPos()));
        }

        return Objects.requireNonNull(tempBlockEntity);
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
        int i;

        addSlot(new SlotItemHandler(blockEntity.seedInputItemHandler, 0, 8, 20));
        for (i = 0; i < blockEntity.skeletonInputItemHandler.getSlots(); i++) {
            addSlot(new SlotItemHandler(blockEntity.skeletonInputItemHandler, i, 44 + i * 18, 20));
        }
    }

    private void addMachineOutputSlots() {
        int i;

        for (i = 0; i < blockEntity.outputItemHandler.getSlots(); i++) {
            addSlot(new SlotItemHandler(blockEntity.outputItemHandler, i, 116 + i * 18, 20));
        }
    }

    public boolean isCrafting() {
        return data.get(WitherSkeletonBreederBlockEntity.ContainerDataType.PROGRESS.ordinal()) > 0;
    }

    public int getScaledProgress() {
        return RenderHelper.getScaledProgress(data.get(WitherSkeletonBreederBlockEntity.ContainerDataType.PROGRESS.ordinal()),
                data.get(WitherSkeletonBreederBlockEntity.ContainerDataType.MAX_PROGRESS.ordinal()),
                WitherSkeletonBreederScreen.PROGRESS_BAR_PIXEL_WIDTH);
    }

    public float getProgress() {
        int progress, maxProgress;

        progress = data.get(WitherSkeletonBreederBlockEntity.ContainerDataType.PROGRESS.ordinal());
        maxProgress = data.get(WitherSkeletonBreederBlockEntity.ContainerDataType.MAX_PROGRESS.ordinal());

        return (float) progress / maxProgress;
    }
}
