package top.srcres258.shanxiskeleton.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.entity.WitherSkeletonRenderer;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.custom.BaseMachineBlock;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonProducerBlockEntity;
import top.srcres258.shanxiskeleton.client.RendererProviders;
import top.srcres258.shanxiskeleton.item.ModItems;
import top.srcres258.shanxiskeleton.util.RenderHelper;

import java.lang.ref.WeakReference;

public class WitherSkeletonProducerBlockEntityRenderer extends WitherSkeletonBlockEntityRenderer<WitherSkeletonProducerBlockEntity> {
    private static final Vec3[] WITHER_SKELETON_TRANSLATE_VECS = new Vec3[] {
            new Vec3(-4.0 / 16.0, 0.0, 3.0 / 16.0),
            new Vec3(0.0, 0.0, 3.0 / 16.0),
            new Vec3(4.0 / 16.0, 0.0, 3.0 / 16.0)
    };

    private WeakReference<ChestRenderer<ChestBlockEntity>> chestRendererCache = new WeakReference<>(null);
    private WeakReference<ChestBlockEntity> chestCache = new WeakReference<>(null);

    public WitherSkeletonProducerBlockEntityRenderer(@NotNull BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(
            @NotNull WitherSkeletonProducerBlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource multiBufferSource,
            int packedLight,
            int packedOverlay
    ) {
        WitherSkeletonRenderer witherSkeletonRenderer;
        SkeletonRenderState renderState;
        ChestRenderer<ChestBlockEntity> chestRenderer;
        ChestBlockEntity chest;
        Direction direction;
        BlockEntityRendererProvider.Context context;
        int renderCount, i;
        Vec3 vec;
        ItemStack roseItemStack;
        Item roseItem;
        BlockState stateToRender;
        Level level;

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
                witherSkeletonRenderer.render(renderState, poseStack, multiBufferSource, packedLight);
                poseStack.popPose();
            }
        }

        // --- Render rose ---
        if (blockEntity.hasRose()) {
            level = blockEntity.getLevel();
            if (level != null) {
                roseItemStack = blockEntity.roseInputItemHandler.getStackInSlot(0);
                roseItem = roseItemStack.getItem();
                poseStack.pushPose();
                transformPoseStackForTinyBlockRendering(poseStack, direction);
                poseStack.translate(0.5 / 0.4, 0.0, -0.5 / 0.4 + 1.0);
                if (roseItem instanceof BlockItem blockItem) {
                    // 能渲染方块的话优先渲染方块模型。
                    stateToRender = blockItem.getBlock().defaultBlockState();
                    context.getBlockRenderDispatcher().renderBatched(stateToRender, blockEntity.getBlockPos(), level,
                            poseStack, multiBufferSource.getBuffer(RenderType.CUTOUT), false, level.getRandom());
                } else {
                    // 否则渲染物品模型。
                    poseStack.translate(0.4, 0.0, 0.5);
                    poseStack.mulPose(Axis.XN.rotationDegrees(90F));
                    context.getItemRenderer().renderStatic(roseItemStack, ItemDisplayContext.FIXED,
                            RenderHelper.calculateLightLevel(level, blockEntity.getBlockPos()),
                            OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, level, 1);
                }
                poseStack.popPose();
            }
        }

        // --- Render chest ---
        chestRenderer = chestRendererCache.get();
        if (chestRenderer == null) {
            ShanxiSkeleton.LOGGER.trace("ChestRenderer for rendering is not cached, creating a new one.");
            chestRenderer = new ChestRenderer<>(context);
            chestRendererCache = new WeakReference<>(chestRenderer);
        }

        chest = chestCache.get();
        if (chest == null) {
            ShanxiSkeleton.LOGGER.trace("ChestBlockEntity for rendering is not cached, creating a new one.");
            chest = new ChestBlockEntity(BlockPos.ZERO, Blocks.CHEST.defaultBlockState());
            chestCache = new WeakReference<>(chest);
        }

        poseStack.pushPose();
        transformPoseStackForTinyBlockRendering(poseStack, direction);
        poseStack.translate(0.5 / 0.4 - 1.0, 0.0, -0.5 / 0.4 + 1.0);
        chestRenderer.render(chest, 1F, poseStack, multiBufferSource, packedLight, packedOverlay);
        poseStack.popPose();

        poseStack.popPose();
    }

    private static int getRenderingWitherSkeletonCount(@NotNull WitherSkeletonProducerBlockEntity blockEntity) {
        int wsCount, maxCount;

        wsCount = blockEntity.inputItemHandler.getStackInSlot(0).getCount();
        maxCount = ModItems.WITHER_SKELETON.get().getDefaultMaxStackSize();
        return Math.min(Mth.ceil((float) wsCount / maxCount * WITHER_SKELETON_TRANSLATE_VECS.length),
                WITHER_SKELETON_TRANSLATE_VECS.length);
    }
}
