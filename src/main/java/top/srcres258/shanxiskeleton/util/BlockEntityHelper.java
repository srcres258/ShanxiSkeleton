package top.srcres258.shanxiskeleton.util;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public class BlockEntityHelper {
    /**
     * 尝试将 ItemStack 放入用于输出的 IItemHandler 的所有可放入的槽位中，并返回剩余无法放入部分的 ItemStack。
     * 若全部放入成功，则返回一个空 ItemStack。
     */
    @NotNull
    public static ItemStack putItemStackIntoOutputItemHandler(
            @NotNull ItemStack stack,
            @NotNull IItemHandlerModifiable itemHandler
    ) {
        int slots, i, countDiff;
        ItemStack remaining, stackInside;

        slots = itemHandler.getSlots();
        remaining = stack.copy();
        for (i = 0; i < slots; i++) {
            if (remaining.isEmpty()) {
                break;
            }
            stackInside = itemHandler.getStackInSlot(i);
            if (stackInside.isEmpty()) {
                itemHandler.setStackInSlot(i, remaining.copy());
                remaining = ItemStack.EMPTY;
                break;
            }
            if (ItemStack.isSameItemSameComponents(remaining, stackInside)) {
                if (stackInside.getCount() + remaining.getCount() <= stackInside.getMaxStackSize()) {
                    stackInside.grow(remaining.getCount());
                    remaining = ItemStack.EMPTY;
                    break;
                }
                countDiff = stackInside.getMaxStackSize() - stackInside.getCount();
                stackInside.grow(countDiff);
                remaining.shrink(countDiff);
            }
        }

        return remaining;
    }
}
