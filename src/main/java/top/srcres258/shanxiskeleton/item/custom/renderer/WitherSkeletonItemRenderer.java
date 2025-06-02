package top.srcres258.shanxiskeleton.item.custom.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.WitherSkeletonRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.client.ItemRenderer;
import top.srcres258.shanxiskeleton.client.RendererProviders;
import top.srcres258.shanxiskeleton.util.RenderHelper;

import java.lang.ref.WeakReference;
import java.util.Map;

public class WitherSkeletonItemRenderer extends ItemRenderer {
    private static final EntityType<WitherSkeleton> WITHER_SKELETON_ENTITY_TYPE = EntityType.WITHER_SKELETON;

    @Nullable
    private WitherSkeletonRenderer skeletonRenderer = null;
    private WeakReference<WitherSkeleton> skeletonMobCache = new WeakReference<>(null);

    @Override
    public void renderByItem(
            @NotNull ItemStack stack,
            @NotNull ItemDisplayContext displayContext,
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource buffer,
            int packedLight,
            int packedOverlay
    ) {
        Level level;
        EntityRendererProvider.Context renderContext;
        Map<EntityType<?>, EntityRenderer<?>> entityRendererMap;
        EntityRenderer<?> entityRenderer;
        ItemStack missingStack;
        WitherSkeleton skeletonMob;

        level = minecraft.level;
        if (level != null) {
            skeletonMob = skeletonMobCache.get();
            if (skeletonMob == null) {
                ShanxiSkeleton.LOGGER.trace("WitherSkeleton for rendering is not cached, creating a new one.");
                createSkeletonMob(level);
            }
            skeletonMob = skeletonMobCache.get();
            if (skeletonMob != null) {
                if (skeletonRenderer == null) {
                    renderContext = RendererProviders.createEntityRendererContext();
                    entityRendererMap = EntityRenderers.createEntityRenderers(renderContext);
                    entityRenderer = entityRendererMap.get(WITHER_SKELETON_ENTITY_TYPE);
                    if (entityRenderer instanceof WitherSkeletonRenderer) {
                        skeletonRenderer = (WitherSkeletonRenderer) entityRenderer;
                    }
                }

                if (skeletonRenderer != null) {
                    poseStack.pushPose();
                    skeletonRenderer.render(skeletonMob, 0F, 1F, poseStack, buffer, packedLight);
                    poseStack.popPose();
                    return;
                }
            }
        }

        // 渲染实体失败，渲染 missing 纹理。
        missingStack = new ItemStack(Items.BARRIER);
        minecraft.getItemRenderer().render(missingStack, displayContext, false, poseStack, buffer, packedLight,
                packedOverlay, minecraft.getModelManager().getMissingModel());
    }

    private void createSkeletonMob(@NotNull Level level) {
        WitherSkeleton skeleton;

        skeleton = WITHER_SKELETON_ENTITY_TYPE.create(level);
        if (skeleton != null) {
            RenderHelper.resetLivingEntityForRendering(skeleton);
            skeletonMobCache = new WeakReference<>(skeleton);
        }
    }
}
