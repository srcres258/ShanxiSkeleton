package top.srcres258.shanxiskeleton.compat.theoneprobe;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.entity.custom.BaseMachineBlockEntity;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonBreederBlockEntity;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonProducerBlockEntity;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonSlaughtererBlockEntity;

public class ModProbeInfoProvider implements IProbeInfoProvider {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(ShanxiSkeleton.MOD_ID, "probeinfoprovider");

    @Override
    @NotNull
    public ResourceLocation getID() {
        return ID;
    }

    @Override
    public void addProbeInfo(
            @NotNull ProbeMode probeMode,
            @NotNull IProbeInfo probeInfo,
            @NotNull Player player,
            @NotNull Level level,
            @NotNull BlockState blockState,
            @NotNull IProbeHitData probeHitData
    ) {
        BlockEntity blockEntity;
        IItemHandler inputs, outputs;
        int i;
        ItemStack stack;

        blockEntity = level.getBlockEntity(probeHitData.getPos());
        if (blockEntity == null) {
            return;
        }
        if (blockEntity instanceof BaseMachineBlockEntity machine) {
            inputs = machine.getMachineInputItemHandler();
            outputs = machine.getMachineOutputItemHandler();
            if (inputs != null) {
                for (i = 0; i < inputs.getSlots(); i++) {
                    stack = inputs.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        probeInfo.item(stack);
                    }
                }
            }
            if (outputs != null) {
                for (i = 0; i < outputs.getSlots(); i++) {
                    stack = outputs.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        probeInfo.item(stack);
                    }
                }
            }
        }
        switch (blockEntity) {
            case WitherSkeletonProducerBlockEntity producer:
                probeInfo.progress(producer.getProgress(), producer.getMaxProgress());
                probeInfo.progress(producer.getRoseProgress(), producer.getRoseMaxProgress());
                break;
            case WitherSkeletonBreederBlockEntity breeder:
                probeInfo.progress(breeder.getProgress(), breeder.getMaxProgress());
                break;
            case WitherSkeletonSlaughtererBlockEntity slaughterer:
                probeInfo.progress(slaughterer.getProgress(), slaughterer.getMaxProgress());
                break;
            default:
        }
    }
}
