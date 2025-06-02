package top.srcres258.shanxiskeleton.block.entity;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.ModBlocks;
import top.srcres258.shanxiskeleton.block.entity.custom.BaseMachineBlockEntity;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonBreederBlockEntity;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonProducerBlockEntity;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonSlaughtererBlockEntity;
import top.srcres258.shanxiskeleton.block.entity.renderer.WitherSkeletonBreederBlockEntityRenderer;
import top.srcres258.shanxiskeleton.block.entity.renderer.WitherSkeletonProducerBlockEntityRenderer;
import top.srcres258.shanxiskeleton.block.entity.renderer.WitherSkeletonSlaughtererBlockEntityRenderer;

public class ModBlockEntityTypes {
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, ShanxiSkeleton.MOD_ID);

    // Just pass `null` to `dataType` parameter since it is not used by our BlockEntity.
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WitherSkeletonProducerBlockEntity>>
    WITHER_SKELETON_PRODUCER = BLOCK_ENTITIES.register("wither_skeleton_producer",
            () -> BlockEntityType.Builder.of(WitherSkeletonProducerBlockEntity::new,
                            ModBlocks.WITHER_SKELETON_PRODUCER.get())
                    .build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WitherSkeletonBreederBlockEntity>>
    WITHER_SKELETON_BREEDER = BLOCK_ENTITIES.register("wither_skeleton_breeder",
            () -> BlockEntityType.Builder.of(WitherSkeletonBreederBlockEntity::new,
                            ModBlocks.WITHER_SKELETON_BREEDER.get())
                    .build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WitherSkeletonSlaughtererBlockEntity>>
    WITHER_SKELETON_SLAUGHTERER = BLOCK_ENTITIES.register("wither_skeleton_slaughterer",
            () -> BlockEntityType.Builder.of(WitherSkeletonSlaughtererBlockEntity::new,
                            ModBlocks.WITHER_SKELETON_SLAUGHTERER.get())
                    .build(null));

    public static void register(@NotNull IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

    public static void registerRenderers(@NotNull EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(WITHER_SKELETON_PRODUCER.get(), WitherSkeletonProducerBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(WITHER_SKELETON_BREEDER.get(), WitherSkeletonBreederBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(WITHER_SKELETON_SLAUGHTERER.get(), WitherSkeletonSlaughtererBlockEntityRenderer::new);
    }

    public static void registerCapabilities(@NotNull RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, WITHER_SKELETON_PRODUCER.get(),
                new BaseMachineBlockEntity.CapabilityProvider());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, WITHER_SKELETON_BREEDER.get(),
                new BaseMachineBlockEntity.CapabilityProvider());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, WITHER_SKELETON_SLAUGHTERER.get(),
                new BaseMachineBlockEntity.CapabilityProvider());
    }
}
