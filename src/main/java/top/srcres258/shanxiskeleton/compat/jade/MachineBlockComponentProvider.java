package top.srcres258.shanxiskeleton.compat.jade;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.impl.ui.ItemStackElement;
import snownee.jade.impl.ui.ProgressElement;
import snownee.jade.impl.ui.SimpleProgressStyle;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.entity.custom.BaseMachineBlockEntity;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonBreederBlockEntity;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonProducerBlockEntity;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonSlaughtererBlockEntity;

public class MachineBlockComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    public static MachineBlockComponentProvider INSTANCE = new MachineBlockComponentProvider();

    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(ShanxiSkeleton.MOD_ID, "machine");

    @Override
    public void appendTooltip(
            @NotNull ITooltip tooltip,
            @NotNull BlockAccessor blockAccessor,
            @NotNull IPluginConfig pluginConfig
    ) {
        BlockEntity blockEntity;
        IItemHandler inputs, outputs;
        CompoundTag serverData;

        blockEntity = blockAccessor.getBlockEntity();
        if (blockEntity == null) {
            return;
        }
        if (blockEntity instanceof BaseMachineBlockEntity machine) {
            inputs = machine.getMachineInputItemHandler();
            outputs = machine.getMachineOutputItemHandler();
            if (inputs != null) {
                addItemHandlerElements(tooltip, inputs);
            }
            if (outputs != null) {
                addItemHandlerElements(tooltip, outputs);
            }
        }

        serverData = blockAccessor.getServerData();
        if (serverData.contains("Progress") && serverData.contains("MaxProgress")) {
            tooltip.add(createProgressElement(((float) serverData.getInt("Progress")) / serverData.getInt("MaxProgress")));
        }
        if (blockEntity instanceof WitherSkeletonProducerBlockEntity) {
            if (serverData.contains("RoseProgress") && serverData.contains("RoseMaxProgress")) {
                tooltip.add(createProgressElement(((float) serverData.getInt("RoseProgress")) / serverData.getInt("RoseMaxProgress"),
                        Component.translatable("gui.shanxiskeleton.byproduct_progress_without_number")));
            }
        }
    }

    @Override
    public void appendServerData(@NotNull CompoundTag data, @NotNull BlockAccessor blockAccessor) {
        BlockEntity blockEntity;

        blockEntity = blockAccessor.getBlockEntity();
        switch (blockEntity) {
            case WitherSkeletonProducerBlockEntity producer:
                data.putInt("Progress", producer.getProgress());
                data.putInt("MaxProgress", producer.getMaxProgress());
                data.putInt("RoseProgress", producer.getRoseProgress());
                data.putInt("RoseMaxProgress", producer.getRoseMaxProgress());
                break;
            case WitherSkeletonBreederBlockEntity breeder:
                data.putInt("Progress", breeder.getProgress());
                data.putInt("MaxProgress", breeder.getMaxProgress());
                break;
            case WitherSkeletonSlaughtererBlockEntity slaughterer:
                data.putInt("Progress", slaughterer.getProgress());
                data.putInt("MaxProgress", slaughterer.getMaxProgress());
                break;
            default:
        }
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    private static void addItemHandlerElements(@NotNull ITooltip tooltip, @NotNull IItemHandler itemHandler) {
        ItemStack stack;
        int i;

        for (i = 0; i < itemHandler.getSlots(); i++) {
            stack = itemHandler.getStackInSlot(i);
            if (!stack.isEmpty()) {
                tooltip.add(ItemStackElement.of(stack));
            }
        }
    }

    @NotNull
    private static ProgressElement createProgressElement(float restrictedProgress) {
        return createProgressElement(restrictedProgress,
                Component.translatable("gui.shanxiskeleton.progress_without_number"));
    }

    @NotNull
    private static ProgressElement createProgressElement(float restrictedProgress, @NotNull Component component) {
        return new ProgressElement(restrictedProgress, component,
                new SimpleProgressStyle(), BoxStyle.getNestedBox(), true);
    }
}
