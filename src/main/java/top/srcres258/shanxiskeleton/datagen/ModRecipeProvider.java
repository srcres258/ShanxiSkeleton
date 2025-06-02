package top.srcres258.shanxiskeleton.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.ModBlocks;
import top.srcres258.shanxiskeleton.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(
            @NotNull PackOutput output,
            @NotNull CompletableFuture<HolderLookup.Provider> registries
    ) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput recipeOutput) {
        // --- Fuels ---
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.COAL)
                .pattern("AAA")
                .pattern("A A")
                .pattern("AAA")
                .define('A', ModItems.TINY_COAL.get())
                .group("coal")
                .unlockedBy("has_tiny_coal", has(ModItems.TINY_COAL.get()))
                .save(recipeOutput, String.format("%s:coal_from_tiny_coal", ShanxiSkeleton.MOD_ID));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.TINY_COAL.get(), 8)
                .requires(Items.COAL)
                .group("coal")
                .unlockedBy("has_coal", has(Items.COAL))
                .save(recipeOutput);

        // --- Tools ---
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.WITHER_SKELETON_CATCHER.get())
                .pattern("AAA")
                .pattern(" B ")
                .pattern(" B ")
                .define('A', Items.IRON_INGOT)
                .define('B', Items.STICK)
                .group("wither_skeleton")
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(recipeOutput);

        // --- Machines ---
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.FRAME_BLOCK.get())
                .pattern("ABA")
                .pattern("B B")
                .pattern("ABA")
                .define('A', Items.IRON_INGOT)
                .define('B', Items.GLASS_PANE)
                .group("wither_skeleton")
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.WITHER_SKELETON_PRODUCER.get())
                .requires(ModBlocks.FRAME_BLOCK.get())
                .requires(Blocks.COAL_BLOCK)
                .requires(Blocks.CHEST)
                .requires(Blocks.DIRT)
                .group("wither_skeleton")
                .unlockedBy("has_frame_block", has(ModBlocks.FRAME_BLOCK.get()))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.WITHER_SKELETON_BREEDER.get())
                .requires(ModBlocks.FRAME_BLOCK.get())
                .requires(Blocks.COAL_BLOCK)
                .requires(ItemTags.BEDS)
                .group("wither_skeleton")
                .unlockedBy("has_frame_block", has(ModBlocks.FRAME_BLOCK.get()))
                .save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModBlocks.WITHER_SKELETON_SLAUGHTERER.get())
                .requires(ModBlocks.FRAME_BLOCK.get())
                .requires(Blocks.DIRT)
                .requires(Items.SWEET_BERRIES)
                .requires(Blocks.CHEST)
                .group("wither_skeleton")
                .unlockedBy("has_frame_block", has(ModBlocks.FRAME_BLOCK.get()))
                .save(recipeOutput);
    }
}
