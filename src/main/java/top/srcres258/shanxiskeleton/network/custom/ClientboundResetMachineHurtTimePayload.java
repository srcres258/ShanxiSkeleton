package top.srcres258.shanxiskeleton.network.custom;

import io.netty.buffer.ByteBuf;
import lombok.AllArgsConstructor;
import lombok.Data;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;

@Data
@AllArgsConstructor
public class ClientboundResetMachineHurtTimePayload implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundResetMachineHurtTimePayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ShanxiSkeleton.MOD_ID,
                    "reset_hurt_time"));

    public static final StreamCodec<ByteBuf, ClientboundResetMachineHurtTimePayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    ClientboundResetMachineHurtTimePayload::getBlockPosX,
                    ByteBufCodecs.VAR_INT,
                    ClientboundResetMachineHurtTimePayload::getBlockPosY,
                    ByteBufCodecs.VAR_INT,
                    ClientboundResetMachineHurtTimePayload::getBlockPosZ,
                    ByteBufCodecs.VAR_INT,
                    ClientboundResetMachineHurtTimePayload::getHurtTime,
                    ClientboundResetMachineHurtTimePayload::new
            );

    public static void sendToAllPlayers(@NotNull BlockPos blockPos, int hurtTime) {
        PacketDistributor.sendToAllPlayers(new ClientboundResetMachineHurtTimePayload(blockPos.getX(), blockPos.getY(),
                blockPos.getZ(), hurtTime));
    }

    private int blockPosX, blockPosY, blockPosZ, hurtTime;

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
