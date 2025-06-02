package top.srcres258.shanxiskeleton.network;

import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.network.custom.ClientboundResetMachineHurtTimePayload;
import top.srcres258.shanxiskeleton.network.custom.ClientboundSyncMachineInputPayload;

public class ModNetworks {
    public static void registerPayloadHandlers(@NotNull PayloadRegistrar registrar) {
        registrar.playToClient(ClientboundResetMachineHurtTimePayload.TYPE,
                ClientboundResetMachineHurtTimePayload.STREAM_CODEC,
                ClientboundResetMachineHurtTimePayloadHandler::handleDataOnMain);
        registrar.playToClient(ClientboundSyncMachineInputPayload.TYPE,
                ClientboundSyncMachineInputPayload.STREAM_CODEC,
                ClientboundSyncMachineInputAmountPayloadHandler::handleDataOnMain);
    }
}
