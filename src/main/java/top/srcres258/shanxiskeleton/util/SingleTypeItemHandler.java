package top.srcres258.shanxiskeleton.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.srcres258.shanxiskeleton.item.ModItems;

import java.util.function.Consumer;

public abstract class SingleTypeItemHandler extends ItemStackHandler {
    public final Item item;

    protected SingleTypeItemHandler(int size, @NotNull Item item) {
        super(size);

        this.item = item;
    }

    public abstract boolean isInputSlot(int slot);

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        if (isInputSlot(slot)) {
            return stack.getItem() == item;
        }
        return false;
    }

    @Override
    public int getSlotLimit(int slot) {
        return ModItems.WITHER_SKELETON.get().getDefaultMaxStackSize();
    }

    public static class InputOnly extends SingleTypeItemHandler {
        @Nullable
        private final Consumer<Integer> contentsChangedCallback;

        public InputOnly(int size, @NotNull Item item, @Nullable Consumer<Integer> contentsChangedCallback) {
            super(size, item);

            this.contentsChangedCallback = contentsChangedCallback;
        }

        @Override
        public boolean isInputSlot(int slot) {
            return true;
        }

        @Override
        protected void onContentsChanged(int slot) {
            if (contentsChangedCallback != null) {
                contentsChangedCallback.accept(slot);
            }
        }
    }
}
