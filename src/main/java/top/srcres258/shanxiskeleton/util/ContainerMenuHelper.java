package top.srcres258.shanxiskeleton.util;

import com.mojang.datafixers.util.Function4;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;

import java.util.function.Function;

public class ContainerMenuHelper {
    // [quickMoveStack]
    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_ROW_COUNT * PLAYER_INVENTORY_COLUMN_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int BE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    /**
     * NOTE: This function **assumes** that the beginning indices of slots are supposed to be **vanilla** slots!
     * Ensure your slots' indices' order before calling this function.
     */
    @NotNull
    public static ItemStack quickMoveStack(
            @NotNull AbstractContainerMenu menu,
            @NotNull Player player,
            int index,
            int beInventorySlotCount,
            @NotNull Function4<ItemStack, Integer, Integer, Boolean, Boolean> moveItemStackTo
    ) {
        Slot sourceSlot;
        ItemStack sourceStack, copyOfSourceStack;

        sourceSlot = menu.slots.get(index);
        if (!sourceSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        sourceStack = sourceSlot.getItem();
        copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo.apply(sourceStack, BE_INVENTORY_FIRST_SLOT_INDEX,
                    BE_INVENTORY_FIRST_SLOT_INDEX + beInventorySlotCount, false)) {
                return ItemStack.EMPTY;
            }
        } else if (index < BE_INVENTORY_FIRST_SLOT_INDEX + beInventorySlotCount) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo.apply(sourceStack, VANILLA_FIRST_SLOT_INDEX,
                    VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            ShanxiSkeleton.LOGGER.warn("Invalid slot index: {}", index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.isEmpty()) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(player, sourceStack);
        return copyOfSourceStack;
    }

    public static void addPlayerInventorySlots(
            @NotNull Inventory inv,
            int left,
            int top,
            @NotNull Function<Slot, Slot> addSlot
    ) {
        int i, j;

        for (i = 0; i < 3; i++) {
            for (j = 0; j < 9; j++) {
                addSlot.apply(new Slot(inv, j + i * 9 + 9, left + j * 18, top + i * 18));
            }
        }
    }

    public static void addPlayerHotbarSlots(
            @NotNull Inventory inv,
            int left,
            int top,
            @NotNull Function<Slot, Slot> addSlot
    ) {
        int i;

        for (i = 0; i < 9; i++) {
            addSlot.apply(new Slot(inv, i, left + i * 18, top));
        }
    }
}
