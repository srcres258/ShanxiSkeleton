package top.srcres258.shanxiskeleton.compat.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.ModBlocks;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonProducerBlockEntity;
import top.srcres258.shanxiskeleton.item.ModItems;

import java.util.Arrays;
import java.util.List;

@JeiPlugin
public class ModJEIPlugin implements IModPlugin {
    public static final RecipeType<ItemStack> CATEGORY_WITHER_SKELETON_PRODUCER_FOR_WITHER_SKELETON = createRecipeType("wither_skeleton_producer_for_wither_skeleton");
    public static final RecipeType<ItemStack> CATEGORY_WITHER_SKELETON_PRODUCER_FOR_ROSES = createRecipeType("wither_skeleton_producer_for_roses");
    public static final RecipeType<ItemStack> CATEGORY_WITHER_SKELETON_BREEDER = createRecipeType("wither_skeleton_breeder");
    public static final RecipeType<ItemStack> CATEGORY_WITHER_SKELETON_SLAUGHTERER = createRecipeType("wither_skeleton_slaughterer");

    @NotNull
    private static RecipeType<ItemStack> createRecipeType(@NotNull String name) {
        return RecipeType.create(ShanxiSkeleton.MOD_ID, name, ItemStack.class);
    }

    @Override
    @NotNull
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(ShanxiSkeleton.MOD_ID, "main");
    }

    @Override
    public void registerRecipeCatalysts(@NotNull IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.WITHER_SKELETON_PRODUCER));
        registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.WITHER_SKELETON_BREEDER));
        registration.addRecipeCatalyst(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.WITHER_SKELETON_SLAUGHTERER));
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        IGuiHelper helper;

        helper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new WitherSkeletonProducerRecipeCategory.ForWitherSkeleton(helper));
        registration.addRecipeCategories(new WitherSkeletonProducerRecipeCategory.ForRoses(helper));
        registration.addRecipeCategories(new WitherSkeletonBreederRecipeCategory(helper));
        registration.addRecipeCategories(new WitherSkeletonSlaughtererRecipeCategory(helper));
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        registration.addRecipes(CATEGORY_WITHER_SKELETON_PRODUCER_FOR_WITHER_SKELETON,
                List.of(new ItemStack(ModItems.WITHER_SKELETON.get())));
        registration.addRecipes(CATEGORY_WITHER_SKELETON_PRODUCER_FOR_ROSES,
                Arrays.stream(WitherSkeletonProducerBlockEntity.DEFAULT_ROSES.get()).sequential()
                        .map(block -> new ItemStack(block.asItem())).toList());
        registration.addRecipes(CATEGORY_WITHER_SKELETON_BREEDER,
                List.of(new ItemStack(ModItems.WITHER_SKELETON.get()), new ItemStack(Items.WITHER_ROSE)));
        registration.addRecipes(CATEGORY_WITHER_SKELETON_SLAUGHTERER,
                List.of(new ItemStack(ModItems.WITHER_SKELETON.get())));
    }
}
