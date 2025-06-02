package top.srcres258.shanxiskeleton.network;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.block.entity.custom.BaseMachineBlockEntity;
import top.srcres258.shanxiskeleton.network.custom.ClientboundSyncMachineInputPayload;

public class ClientboundSyncMachineInputAmountPayloadHandler {
    public static void handleDataOnMain(
            @NotNull ClientboundSyncMachineInputPayload data,
            @NotNull IPayloadContext context
    ) {
        BlockPos blockPos;
        Player player;
        Level level;
        BlockEntity blockEntity;
        IItemHandler handler;
        ItemStack stack;
        ResourceLocation inputResLoc;
        Item item;

        inputResLoc = ResourceLocation.tryParse(data.getInputResLoc());
        if (inputResLoc == null) {
            return;
        }
        if (!BuiltInRegistries.ITEM.containsKey(inputResLoc)) {
            return;
        }
        item = BuiltInRegistries.ITEM.get(inputResLoc);
        stack = new ItemStack(item, data.getInputAmount());

        blockPos = new BlockPos(data.getBlockPosX(), data.getBlockPosY(), data.getBlockPosZ());
        player = context.player();
        level = player.level();
        blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof BaseMachineBlockEntity machine) {
            handler = machine.getMachineInputItemHandler();
            if (handler instanceof IItemHandlerModifiable modifiableHandler) {
                modifiableHandler.setStackInSlot(data.getInputIndex(), stack);
            }
        }
    }
}
