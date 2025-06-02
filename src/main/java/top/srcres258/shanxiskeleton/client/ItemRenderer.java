package top.srcres258.shanxiskeleton.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class ItemRenderer {
    protected class Renderer extends BlockEntityWithoutLevelRenderer {
        public Renderer() {
            super(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
        }

        @Override
        public void renderByItem(
                @NotNull ItemStack stack,
                @NotNull ItemDisplayContext displayContext,
                @NotNull PoseStack poseStack,
                @NotNull MultiBufferSource buffer,
                int packedLight,
                int packedOverlay
        ) {
            ItemRenderer.this.renderByItem(stack, displayContext, poseStack, buffer, packedLight, packedOverlay);
        }
    }

    protected final Minecraft minecraft = Minecraft.getInstance();
    protected final Renderer renderer = new Renderer();

    @NotNull
    public Supplier<BlockEntityWithoutLevelRenderer> getRendererSupplier() {
        return () -> renderer;
    }

    public void renderByItem(
            @NotNull ItemStack stack,
            @NotNull ItemDisplayContext displayContext,
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource buffer,
            int packedLight,
            int packedOverlay
    ) {}
}
