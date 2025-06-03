package top.srcres258.shanxiskeleton.util;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.CoreShaders;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.NotNull;

public class RenderHelper {
    public static int getScaledProgress(int progress, int maxProgress, int progressBarPixelSize) {
        return progress > 0 && maxProgress > 0 && progressBarPixelSize > 0 ?
                progress * progressBarPixelSize / maxProgress : 0;
    }

    public static void setShaderTexture(int textureId, @NotNull ResourceLocation texture) {
        RenderSystem.setShader(CoreShaders.POSITION_TEX);
        RenderSystem.setShaderColor(1F, 1F, 1F, 1F);
        RenderSystem.setShaderTexture(textureId, texture);
    }

    public static void resetLivingEntityForRendering(@NotNull LivingEntity entity) {
        entity.hurtTime = 0;
        entity.yBodyRot = 0;
        entity.yBodyRotO = 0;
        entity.yHeadRot = 0;
        entity.yHeadRotO = 0;
    }

    public static int calculateLightLevel(@NotNull BlockAndTintGetter level, @NotNull BlockPos pos) {
        int blockLight, skyLight;

        blockLight = level.getBrightness(LightLayer.BLOCK, pos);
        skyLight = level.getBrightness(LightLayer.SKY, pos);

        return LightTexture.pack(blockLight, skyLight);
    }
}
