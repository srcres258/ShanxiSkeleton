package top.srcres258.shanxiskeleton.util;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class OutputItemHandler extends ItemStackHandler {
    public OutputItemHandler(int size) {
        super(size);
    }

    // Refuse to insert any items because the slots are used for output purpose only.
    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false;
    }

    // Refuse to insert any items because the slots are used for output purpose only.
    @Override
    @NotNull
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return stack.copy();
    }
}
