package top.srcres258.shanxiskeleton.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BedRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.WitherSkeletonRenderer;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BedBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.custom.BaseMachineBlock;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonBreederBlockEntity;
import top.srcres258.shanxiskeleton.client.RendererProviders;

import java.lang.ref.WeakReference;

public class WitherSkeletonBreederBlockEntityRenderer extends WitherSkeletonBlockEntityRenderer<WitherSkeletonBreederBlockEntity> {
    private static final Vec3[] WITHER_SKELETON_TRANSLATE_VECS = new Vec3[] {
            new Vec3(-4.0 / 16.0, 0.0, 3.0 / 16.0),
            new Vec3(4.0 / 16.0, 0.0, 3.0 / 16.0)
    };
    private static final float[] WITHER_SKELETON_ROTATION_ANGLES = new float[] { 90F, -90F };

    private WeakReference<BedRenderer> bedRendererCache = new WeakReference<>(null);
    private WeakReference<BedBlockEntity> bedCache = new WeakReference<>(null);

    public WitherSkeletonBreederBlockEntityRenderer(@NotNull BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(
            @NotNull WitherSkeletonBreederBlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay
    ) {
        WitherSkeletonRenderer witherSkeletonRenderer;
        SkeletonRenderState renderState;
        int renderCount, i;
        Vec3 vec;
        Direction direction;
        Level level;
        ItemStack seedItemStack;
        Item seedItem;
        BlockEntityRendererProvider.Context context;
        BlockState stateToRender;
        BedRenderer bedRenderer;
        BedBlockEntity bed;

        poseStack.pushPose();

        direction = blockEntity.getBlockState().getValue(BaseMachineBlock.FACING);
        context = RendererProviders.createBlockEntityRendererContext();

        // --- Render wither skeletons ---
        if (blockEntity.hasWitherSkeleton()) {
            witherSkeletonRenderer = getWitherSkeletonRenderer();
            renderState = getWitherSkeletonRenderState();

            renderCount = getRenderingWitherSkeletonCount(blockEntity);

            for (i = 0; i < renderCount; i++) {
                vec = WITHER_SKELETON_TRANSLATE_VECS[i];
                poseStack.pushPose();
                poseStack.translate(0.5, 1.0 / 16.0, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
                poseStack.translate(vec.x, vec.y, vec.z);
                poseStack.scale(0.3F, 0.3F, 0.3F);
                poseStack.mulPose(Axis.YP.rotationDegrees(WITHER_SKELETON_ROTATION_ANGLES[i]));
                witherSkeletonRenderer.render(renderState, poseStack, bufferSource, packedLight);
                poseStack.popPose();
            }
        }

        // --- Render seed ---
        if (blockEntity.hasSeed()) {
            level = blockEntity.getLevel();
            if (level != null) {
                seedItemStack = blockEntity.seedInputItemHandler.getStackInSlot(0);
                seedItem = seedItemStack.getItem();
                poseStack.pushPose();
                transformPoseStackForTinyBlockRendering(poseStack, direction);
                poseStack.translate(0.5 / 0.4 - 0.25, 0.0, 0.5 / 0.4 - 0.5);
                if (seedItem instanceof BlockItem blockItem) {
                    // 能渲染方块的话，渲染方块模型。
                    stateToRender = blockItem.getBlock().defaultBlockState();
                    context.getBlockRenderDispatcher().renderBatched(stateToRender, blockEntity.getBlockPos(), level,
                            poseStack, bufferSource.getBuffer(RenderType.CUTOUT), false, level.getRandom());
                } // 省去非方块的渲染逻辑（考虑到凋零玫瑰必定具有方块模型）。
                poseStack.popPose();
            }
        }

        // --- Render bed ---
        bedRenderer = bedRendererCache.get();
        if (bedRenderer == null) {
            ShanxiSkeleton.LOGGER.trace("BedRenderer for rendering is not cached, creating a new one.");
            bedRenderer = new BedRenderer(context);
            bedRendererCache = new WeakReference<>(bedRenderer);
        }

        bed = bedCache.get();
        if (bed == null) {
            ShanxiSkeleton.LOGGER.trace("BedBlockEntity for rendering is not cached, creating a new one.");
            bed = new BedBlockEntity(BlockPos.ZERO, Blocks.RED_BED.defaultBlockState());
            bedCache = new WeakReference<>(bed);
        }

        poseStack.pushPose();
        transformPoseStackForTinyBlockRendering(poseStack, direction);
        poseStack.mulPose(Axis.YP.rotationDegrees(90F));
        poseStack.translate(-0.5 / 0.4 + 0.5, 0.0, 0.5 / 0.4);
        bedRenderer.render(bed, 1F, poseStack, bufferSource, packedLight, packedOverlay);
        poseStack.popPose();

        poseStack.popPose();
    }

    private static int getRenderingWitherSkeletonCount(@NotNull WitherSkeletonBreederBlockEntity blockEntity) {
        if (blockEntity.getWitherSkeletonCount() == 0) {
            return 0;
        } else if (blockEntity.getWitherSkeletonCount() == 1) {
            return 1;
        }
        return 2;
    }
}
