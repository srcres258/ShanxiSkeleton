package top.srcres258.shanxiskeleton.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.entity.custom.BaseMachineBlockEntity;

public abstract class BaseMachineBlockEntityRenderer<T extends BaseMachineBlockEntity> implements BlockEntityRenderer<T> {
    protected final Minecraft mc = Minecraft.getInstance();
    protected final BlockEntityRendererProvider.Context renderer;

    public BaseMachineBlockEntityRenderer(@NotNull BlockEntityRendererProvider.Context renderer) {
        this.renderer = renderer;
    }

    @Override
    public int getViewDistance() {
        return ShanxiSkeleton.getInstance().clientConfig.blockRenderingDistance.get();
    }

    protected static void transformPoseStackForTinyBlockRendering(@NotNull PoseStack poseStack, @NotNull Direction direction) {
        poseStack.translate(0.5, 1.0 / 16.0, 0.5);
        poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
        poseStack.translate(0.0, 0.0, 3.0 / 16.0);
        poseStack.translate(-0.5, 0.0, -0.5);
        poseStack.scale(0.4F, 0.4F, 0.4F);
    }
}
