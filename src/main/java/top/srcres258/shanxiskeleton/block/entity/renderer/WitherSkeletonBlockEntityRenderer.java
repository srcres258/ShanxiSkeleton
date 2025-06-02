package top.srcres258.shanxiskeleton.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WitherSkeletonRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.entity.custom.BaseMachineBlockEntity;
import top.srcres258.shanxiskeleton.client.RendererProviders;
import top.srcres258.shanxiskeleton.util.RenderHelper;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class WitherSkeletonBlockEntityRenderer<T extends BaseMachineBlockEntity> extends BaseMachineBlockEntityRenderer<T> {
    private WeakReference<WitherSkeletonRenderer> witherSkeletonRendererCache = new WeakReference<>(null);
    private WeakReference<WitherSkeleton> witherSkeletonCache = new WeakReference<>(null);

    public WitherSkeletonBlockEntityRenderer(@NotNull BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(
            @NotNull T blockEntity,
            float partialTick,
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {}

    @NotNull
    protected WitherSkeletonRenderer getWitherSkeletonRenderer() {
        WitherSkeletonRenderer witherSkeletonRenderer;
        EntityRendererProvider.Context context;

        witherSkeletonRenderer = witherSkeletonRendererCache.get();
        if (witherSkeletonRenderer == null) {
            ShanxiSkeleton.LOGGER.trace("WitherSkeletonRenderer for rendering is not cached, creating a new one.");
            context = RendererProviders.createEntityRendererContext();
            witherSkeletonRenderer = new WitherSkeletonRenderer(context);
            witherSkeletonRendererCache = new WeakReference<>(witherSkeletonRenderer);
        }
        return witherSkeletonRenderer;
    }

    @NotNull
    protected WitherSkeleton getWitherSkeleton() {
        WitherSkeleton witherSkeleton;
        Level level;

        witherSkeleton = witherSkeletonCache.get();
        if (witherSkeleton == null) {
            ShanxiSkeleton.LOGGER.trace("WitherSkeleton for rendering is not cached, creating a new one.");
            level = mc.level;
            if (level == null) {
                throw new IllegalStateException("Calling getWitherSkeleton when not in a level.");
            }
            witherSkeleton = Objects.requireNonNull(EntityType.WITHER_SKELETON.create(level),
                    "Failed to create WitherSkeleton entity.");
            RenderHelper.resetLivingEntityForRendering(witherSkeleton);
            witherSkeletonCache = new WeakReference<>(witherSkeleton);
        }
        return witherSkeleton;
    }
}
