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
public class ClientboundSyncMachineInputPayload implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<ClientboundSyncMachineInputPayload> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(ShanxiSkeleton.MOD_ID,
                    "sync_machine_input_amount"));

    public static final StreamCodec<ByteBuf, ClientboundSyncMachineInputPayload> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.VAR_INT,
                    ClientboundSyncMachineInputPayload::getBlockPosX,
                    ByteBufCodecs.VAR_INT,
                    ClientboundSyncMachineInputPayload::getBlockPosY,
                    ByteBufCodecs.VAR_INT,
                    ClientboundSyncMachineInputPayload::getBlockPosZ,
                    ByteBufCodecs.VAR_INT,
                    ClientboundSyncMachineInputPayload::getInputIndex,
                    ByteBufCodecs.VAR_INT,
                    ClientboundSyncMachineInputPayload::getInputAmount,
                    ByteBufCodecs.STRING_UTF8,
                    ClientboundSyncMachineInputPayload::getInputResLoc,
                    ClientboundSyncMachineInputPayload::new
            );

    public static void sendToAllPlayers(@NotNull BlockPos blockPos, int inputIndex, int inputAmount, @NotNull String inputResLoc) {
        PacketDistributor.sendToAllPlayers(new ClientboundSyncMachineInputPayload(blockPos.getX(),
                blockPos.getY(), blockPos.getZ(), inputIndex, inputAmount, inputResLoc));
    }

    private int blockPosX, blockPosY, blockPosZ, inputIndex, inputAmount;
    private String inputResLoc;

    @Override
    @NotNull
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
