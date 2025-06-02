package top.srcres258.shanxiskeleton.network;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonSlaughtererBlockEntity;
import top.srcres258.shanxiskeleton.network.custom.ClientboundResetMachineHurtTimePayload;

public class ClientboundResetMachineHurtTimePayloadHandler {
    public static void handleDataOnMain(
            @NotNull ClientboundResetMachineHurtTimePayload data,
            @NotNull IPayloadContext context
    ) {
        BlockPos blockPos;
        Player player;
        Level level;
        BlockEntity blockEntity;

        blockPos = new BlockPos(data.getBlockPosX(), data.getBlockPosY(), data.getBlockPosZ());
        player = context.player();
        level = player.level();
        blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof WitherSkeletonSlaughtererBlockEntity slaughterer) {
            slaughterer.witherSkeletonHurtTime = data.getHurtTime();
        }
    }
}
