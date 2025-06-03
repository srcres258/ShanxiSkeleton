package top.srcres258.shanxiskeleton.compat.jade;

import org.jetbrains.annotations.NotNull;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;
import top.srcres258.shanxiskeleton.block.custom.WitherSkeletonBreederBlock;
import top.srcres258.shanxiskeleton.block.custom.WitherSkeletonProducerBlock;
import top.srcres258.shanxiskeleton.block.custom.WitherSkeletonSlaughtererBlock;

@WailaPlugin
public class ModPlugin implements IWailaPlugin {
    @Override
    public void register(@NotNull IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(MachineBlockComponentProvider.INSTANCE, WitherSkeletonProducerBlock.class);
        registration.registerBlockDataProvider(MachineBlockComponentProvider.INSTANCE, WitherSkeletonBreederBlock.class);
        registration.registerBlockDataProvider(MachineBlockComponentProvider.INSTANCE, WitherSkeletonSlaughtererBlock.class);
    }

    @Override
    public void registerClient(@NotNull IWailaClientRegistration registration) {
        registration.registerBlockComponent(MachineBlockComponentProvider.INSTANCE, WitherSkeletonProducerBlock.class);
        registration.registerBlockComponent(MachineBlockComponentProvider.INSTANCE, WitherSkeletonBreederBlock.class);
        registration.registerBlockComponent(MachineBlockComponentProvider.INSTANCE, WitherSkeletonSlaughtererBlock.class);
    }
}
