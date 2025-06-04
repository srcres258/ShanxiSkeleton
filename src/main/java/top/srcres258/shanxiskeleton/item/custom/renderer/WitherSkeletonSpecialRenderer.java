package top.srcres258.shanxiskeleton.item.custom.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.WitherSkeletonRenderer;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.srcres258.shanxiskeleton.client.RendererProviders;

import java.lang.ref.WeakReference;

public class WitherSkeletonSpecialRenderer implements SpecialModelRenderer<SkeletonRenderState> {
    private static final EntityType<WitherSkeleton> WITHER_SKELETON_ENTITY_TYPE = EntityType.WITHER_SKELETON;

    private final Minecraft minecraft = Minecraft.getInstance();

    private WeakReference<WitherSkeletonRenderer> rendererCache = new WeakReference<>(null);
    private WeakReference<WitherSkeleton> entityCache = new WeakReference<>(null);
    private WeakReference<SkeletonRenderState> renderStateCache = new WeakReference<>(null);

    public WitherSkeletonSpecialRenderer(@NotNull EntityModelSet modelSet) {}

    @NotNull
    private WitherSkeletonRenderer getRenderer() {
        WitherSkeletonRenderer renderer;

        renderer = rendererCache.get();
        if (renderer == null) {
            renderer = new WitherSkeletonRenderer(RendererProviders.createEntityRendererContext());
            rendererCache = new WeakReference<>(renderer);
        }

        return renderer;
    }

    @NotNull
    private WitherSkeleton getEntity() {
        WitherSkeleton entity;
        Level level;

        entity = entityCache.get();
        if (entity == null) {
            level = minecraft.level;
            if (level == null) {
                throw new IllegalStateException("Trying to get entity when there is no client level.");
            }
            entity = WITHER_SKELETON_ENTITY_TYPE.create(level, EntitySpawnReason.LOAD);
            if (entity == null) {
                throw new IllegalStateException("Failed to create entity of type " + WITHER_SKELETON_ENTITY_TYPE);
            }
            entityCache = new WeakReference<>(entity);
        }

        return entity;
    }

    @NotNull
    private SkeletonRenderState getRenderState() {
        SkeletonRenderState renderState;

        renderState = renderStateCache.get();
        if (renderState == null) {
            renderState = getRenderer().createRenderState();
            getRenderer().extractRenderState(getEntity(), renderState, 0F);
            renderStateCache = new WeakReference<>(renderState);
        }

        return renderState;
    }

    @Override
    @Nullable
    public SkeletonRenderState extractArgument(@NotNull ItemStack itemStack) {
        return getRenderState();
    }

    @Override
    public void render(
            @Nullable SkeletonRenderState skeletonRenderState,
            @NotNull ItemDisplayContext itemDisplayContext,
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource bufferSource,
            int light,
            int overlay,
            boolean b
    ) {
        if (skeletonRenderState == null) {
            return;
        }
        getRenderer().render(skeletonRenderState, poseStack, bufferSource, light);
    }

    public static class Unbaked implements SpecialModelRenderer.Unbaked {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);

        @Override
        @NotNull
        public MapCodec<? extends SpecialModelRenderer.Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public @Nullable SpecialModelRenderer<?> bake(@NotNull EntityModelSet entityModelSet) {
            return new WitherSkeletonSpecialRenderer(entityModelSet);
        }
    }
}
