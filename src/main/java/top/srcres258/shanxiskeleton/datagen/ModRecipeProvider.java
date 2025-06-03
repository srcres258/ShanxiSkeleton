package top.srcres258.shanxiskeleton.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.ModBlocks;
import top.srcres258.shanxiskeleton.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public static class Runner extends RecipeProvider.Runner {
        public Runner(@NotNull PackOutput output, @NotNull CompletableFuture<HolderLookup.Provider> registries) {
            super(output, registries);
        }

        @Override
        @NotNull
        protected RecipeProvider createRecipeProvider(
                @NotNull HolderLookup.Provider provider,
                @NotNull RecipeOutput recipeOutput
        ) {
            return new ModRecipeProvider(provider, recipeOutput);
        }

        @Override
        @NotNull
        public String getName() {
            return String.format("%s recipes", ShanxiSkeleton.MOD_ID);
        }
    }

    public ModRecipeProvider(@NotNull HolderLookup.Provider registries, @NotNull RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        // --- Fuels ---
        shaped(RecipeCategory.MISC, Items.COAL)
                .pattern("AAA")
                .pattern("A A")
                .pattern("AAA")
                .define('A', ModItems.TINY_COAL.get())
                .group("coal")
                .unlockedBy("has_tiny_coal", has(ModItems.TINY_COAL.get()))
                .save(output, String.format("%s:coal_from_tiny_coal", ShanxiSkeleton.MOD_ID));
        shapeless(RecipeCategory.MISC, ModItems.TINY_COAL.get(), 8)
                .requires(Items.COAL)
                .group("coal")
                .unlockedBy("has_coal", has(Items.COAL))
                .save(output);

        // --- Tools ---
        shaped(RecipeCategory.MISC, ModItems.WITHER_SKELETON_CATCHER.get())
                .pattern("AAA")
                .pattern(" B ")
                .pattern(" B ")
                .define('A', Items.IRON_INGOT)
                .define('B', Items.STICK)
                .group("wither_skeleton")
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output);

        // --- Machines ---
        shaped(RecipeCategory.MISC, ModBlocks.FRAME_BLOCK.get())
                .pattern("ABA")
                .pattern("B B")
                .pattern("ABA")
                .define('A', Items.IRON_INGOT)
                .define('B', Items.GLASS_PANE)
                .group("wither_skeleton")
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output);
        shapeless(RecipeCategory.MISC, ModBlocks.WITHER_SKELETON_PRODUCER.get())
                .requires(ModBlocks.FRAME_BLOCK.get())
                .requires(Blocks.COAL_BLOCK)
                .requires(Blocks.CHEST)
                .requires(Blocks.DIRT)
                .group("wither_skeleton")
                .unlockedBy("has_frame_block", has(ModBlocks.FRAME_BLOCK.get()))
                .save(output);
        shapeless(RecipeCategory.MISC, ModBlocks.WITHER_SKELETON_BREEDER.get())
                .requires(ModBlocks.FRAME_BLOCK.get())
                .requires(Blocks.COAL_BLOCK)
                .requires(ItemTags.BEDS)
                .group("wither_skeleton")
                .unlockedBy("has_frame_block", has(ModBlocks.FRAME_BLOCK.get()))
                .save(output);
        shapeless(RecipeCategory.MISC, ModBlocks.WITHER_SKELETON_SLAUGHTERER.get())
                .requires(ModBlocks.FRAME_BLOCK.get())
                .requires(Blocks.DIRT)
                .requires(Items.SWEET_BERRIES)
                .requires(Blocks.CHEST)
                .group("wither_skeleton")
                .unlockedBy("has_frame_block", has(ModBlocks.FRAME_BLOCK.get()))
                .save(output);
    }
}
