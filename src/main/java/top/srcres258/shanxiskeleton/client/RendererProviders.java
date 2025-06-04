package top.srcres258.shanxiskeleton.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.resources.model.EquipmentAssetManager;
import org.jetbrains.annotations.NotNull;

public class RendererProviders {
    private static final Minecraft MC = Minecraft.getInstance();

    @NotNull
    public static BlockEntityRendererProvider.Context createBlockEntityRendererContext() {
        return new BlockEntityRendererProvider.Context(MC.getBlockEntityRenderDispatcher(), MC.getBlockRenderer(),
                MC.getItemModelResolver(), MC.getItemRenderer(), MC.getEntityRenderDispatcher(), MC.getEntityModels(),
                MC.font);
    }

    @NotNull
    public static EntityRendererProvider.Context createEntityRendererContext() {
        return new EntityRendererProvider.Context(MC.getEntityRenderDispatcher(), MC.getItemModelResolver(),
                MC.getMapRenderer(), MC.getBlockRenderer(), MC.getResourceManager(), MC.getEntityModels(),
                new EquipmentAssetManager(), MC.font);
    }
}
