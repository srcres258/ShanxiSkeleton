package top.srcres258.shanxiskeleton.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.NotNull;

public class RendererProviders {
    private static final Minecraft MC = Minecraft.getInstance();

    @NotNull
    public static BlockEntityRendererProvider.Context createBlockEntityRendererContext() {
        return new BlockEntityRendererProvider.Context(MC.getBlockEntityRenderDispatcher(), MC.getBlockRenderer(),
                MC.getItemRenderer(), MC.getEntityRenderDispatcher(), MC.getEntityModels(), MC.font);
    }

    @NotNull
    public static EntityRendererProvider.Context createEntityRendererContext() {
        return new EntityRendererProvider.Context(MC.getEntityRenderDispatcher(), MC.getItemRenderer(),
                MC.getMapRenderer(), MC.getBlockRenderer(), MC.getResourceManager(), MC.getEntityModels(),
                MC.getEquipmentModels(), MC.font);
    }
}
