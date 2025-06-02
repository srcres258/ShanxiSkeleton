package top.srcres258.shanxiskeleton.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.GlobalLootModifierProvider;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;

import java.util.concurrent.CompletableFuture;

public class ModGlobalLootModifierProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifierProvider(
            @NotNull PackOutput output,
            @NotNull CompletableFuture<HolderLookup.Provider> registries
    ) {
        super(output, registries, ShanxiSkeleton.MOD_ID);
    }

    @Override
    protected void start() {

    }
}
