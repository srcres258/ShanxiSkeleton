package top.srcres258.shanxiskeleton.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.item.ModItems;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(
            @NotNull PackOutput output,
            @NotNull ExistingFileHelper existingFileHelper
    ) {
        super(output, ShanxiSkeleton.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.WITHER_SKELETON_CATCHER.get());
        basicItem(ModItems.TINY_COAL.get());
    }
}
