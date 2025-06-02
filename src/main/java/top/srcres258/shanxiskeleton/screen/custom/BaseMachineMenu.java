package top.srcres258.shanxiskeleton.screen.custom;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.block.custom.BaseMachineBlock;
import top.srcres258.shanxiskeleton.block.entity.custom.BaseMachineBlockEntity;
import top.srcres258.shanxiskeleton.util.ContainerMenuHelper;

public abstract class BaseMachineMenu<T extends BaseMachineBlockEntity> extends AbstractContainerMenu {
    protected final BaseMachineBlock machineBlock;
    protected final int machineSlotCount;
    protected final Level level;
    public final T blockEntity;
    public final ContainerData data;

    protected BaseMachineMenu(
            @NotNull MenuType<?> menuType,
            @NotNull BaseMachineBlock machineBlock,
            int machineSlotCount,
            int containerId,
            @NotNull Inventory inv,
            @NotNull Level level,
            @NotNull T blockEntity,
            @NotNull ContainerData data
    ) {
        super(menuType, containerId);

        this.machineBlock = machineBlock;
        this.machineSlotCount = machineSlotCount;
        this.level = level;
        this.blockEntity = blockEntity;
        this.data = data;

        addPlayerInventorySlots(inv);
        addPlayerHotbarSlots(inv);

        addMachineSlots();

        addDataSlots(data);
    }

    protected abstract void addPlayerInventorySlots(@NotNull Inventory inv);

    protected abstract void addPlayerHotbarSlots(@NotNull Inventory inv);

    protected abstract void addMachineSlots();

    @Override
    @NotNull
    public ItemStack quickMoveStack(@NotNull Player player, int index) {
        return ContainerMenuHelper.quickMoveStack(this, player, index, machineSlotCount, this::moveItemStackTo);
    }

    @Override
    public boolean stillValid(@NotNull Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()), player, machineBlock);
    }
}
