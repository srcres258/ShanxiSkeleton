package top.srcres258.shanxiskeleton.datagen;

import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class ModTextureMappings {
    @NotNull
    public static TextureMapping machine(@NotNull Block front, @NotNull Block back) {
        return machine(TextureMapping.getBlockTexture(front), TextureMapping.getBlockTexture(back));
    }

    @NotNull
    public static TextureMapping machine(@NotNull ResourceLocation front, @NotNull ResourceLocation back) {
        return new TextureMapping().put(ModTextureSlots.FRONT, front).put(ModTextureSlots.BACK, back);
    }
}
