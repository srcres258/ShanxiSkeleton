package top.srcres258.shanxiskeleton.block.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.ChestRenderer;
import net.minecraft.client.renderer.entity.WitherSkeletonRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.custom.BaseMachineBlock;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonSlaughtererBlockEntity;
import top.srcres258.shanxiskeleton.client.RendererProviders;

import java.lang.ref.WeakReference;

public class WitherSkeletonSlaughtererBlockEntityRenderer extends WitherSkeletonBlockEntityRenderer<WitherSkeletonSlaughtererBlockEntity> {
    private static final Vec3[] WITHER_SKELETON_TRANSLATE_VECS = new Vec3[] {
            new Vec3(-4.0 / 16.0, 0.0, 3.0 / 16.0),
            new Vec3(0.0, 0.0, 3.0 / 16.0),
            new Vec3(4.0 / 16.0, 0.0, 3.0 / 16.0)
    };
    private static final Vec3[] BUSH_TRANSLATE_VECS = new Vec3[] {
            new Vec3(0.5 / 0.4, 0.0, 0.5 / 0.4 - 0.5),
            new Vec3(0.5 / 0.4 - 1.0, 0.0, 0.5 / 0.4 - 0.5)
    };

    private WeakReference<ChestRenderer<ChestBlockEntity>> chestRendererCache = new WeakReference<>(null);
    private WeakReference<ChestBlockEntity> chestCache = new WeakReference<>(null);

    public WitherSkeletonSlaughtererBlockEntityRenderer(@NotNull BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(
            @NotNull WitherSkeletonSlaughtererBlockEntity blockEntity,
            float partialTick,
            @NotNull PoseStack poseStack,
            @NotNull MultiBufferSource multiBufferSource,
            int packedLight,
            int packedOverlay
    ) {
        Direction direction;
        BlockEntityRendererProvider.Context context;
        WitherSkeletonRenderer witherSkeletonRenderer;
        WitherSkeleton witherSkeleton;
        int renderCount, i;
        Vec3 vec;
        ChestRenderer<ChestBlockEntity> chestRenderer;
        ChestBlockEntity chest;
        Level level;
        BlockState stateToRender;

        poseStack.pushPose();

        direction = blockEntity.getBlockState().getValue(BaseMachineBlock.FACING);
        context = RendererProviders.createBlockEntityRendererContext();

        // --- Render wither skeletons ---
        if (blockEntity.hasInput()) {
            witherSkeletonRenderer = getWitherSkeletonRenderer();
            witherSkeleton = getWitherSkeleton();
            // 模拟凋灵骷髅的受伤动画。
            if (blockEntity.isWorking()) {
                witherSkeleton.hurtTime = blockEntity.witherSkeletonHurtTime;
                if (blockEntity.witherSkeletonHurtTime > 0) {
                    blockEntity.witherSkeletonHurtTime--;
                }
            } else {
                witherSkeleton.hurtTime = 0;
            }

            renderCount = getRenderingWitherSkeletonCount(blockEntity);

            for (i = 0; i < renderCount; i++) {
                vec = WITHER_SKELETON_TRANSLATE_VECS[i];
                poseStack.pushPose();
                poseStack.translate(0.5, 1.0 / 16.0, 0.5);
                poseStack.mulPose(Axis.YP.rotationDegrees(-direction.toYRot()));
                poseStack.translate(vec.x, vec.y, vec.z);
                poseStack.scale(0.3F, 0.3F, 0.3F);
                poseStack.mulPose(Axis.YP.rotationDegrees(-90F));
                witherSkeletonRenderer.render(witherSkeleton, 0F, 1F, poseStack, multiBufferSource, packedLight);
                poseStack.popPose();
            }
        }

        // --- Render bushes ---
        level = blockEntity.getLevel();
        if (level != null) {
            for (i = 0; i < BUSH_TRANSLATE_VECS.length; i++) {
                vec = BUSH_TRANSLATE_VECS[i];
                poseStack.pushPose();
                transformPoseStackForTinyBlockRendering(poseStack, direction);
                poseStack.translate(vec.x, vec.y, vec.z);
                stateToRender = Blocks.SWEET_BERRY_BUSH.defaultBlockState()
                        .setValue(SweetBerryBushBlock.AGE, SweetBerryBushBlock.MAX_AGE);
                context.getBlockRenderDispatcher().renderBatched(stateToRender, blockEntity.getBlockPos(), level,
                        poseStack, multiBufferSource.getBuffer(RenderType.CUTOUT), false, level.getRandom());
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

    private static int getRenderingWitherSkeletonCount(@NotNull WitherSkeletonSlaughtererBlockEntity blockEntity) {
        int wsCount, maxCount;

        wsCount = blockEntity.getWitherSkeletonCount();
        maxCount = blockEntity.getMaxWitherSkeletonCount();
        return Math.min(Mth.ceil(((float) wsCount) / maxCount * WITHER_SKELETON_TRANSLATE_VECS.length),
                WITHER_SKELETON_TRANSLATE_VECS.length);
    }
}
