package top.srcres258.shanxiskeleton.util;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

public class RegistryHelper {
    @NotNull
    public static ResourceLocation getBlockKey(@NotNull Block block) {
        return BuiltInRegistries.BLOCK.getKey(block);
    }

    @NotNull
    public static String getBlockKeyString(@NotNull Block block) {
        return getBlockKey(block).toString();
    }
}
