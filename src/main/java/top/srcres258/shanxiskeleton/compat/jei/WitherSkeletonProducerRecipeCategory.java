package top.srcres258.shanxiskeleton.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.ModBlocks;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonProducerBlockEntity;
import top.srcres258.shanxiskeleton.item.ModItems;

import java.util.Arrays;

public abstract class WitherSkeletonProducerRecipeCategory implements IRecipeCategory<ItemStack> {
    private static final int OUTPUT_SLOTS_COUNT = WitherSkeletonProducerBlockEntity.OUTPUT_SLOTS_COUNT;
    private static final int ROSE_OUTPUT_SLOTS_COUNT = WitherSkeletonProducerBlockEntity.ROSE_OUTPUT_SLOTS_COUNT;

    private final IGuiHelper helper;

    public WitherSkeletonProducerRecipeCategory(@NotNull IGuiHelper helper) {
        this.helper = helper;
    }

    @Override
    @NotNull
    public IDrawable getBackground() {
        return helper.createDrawable(ResourceLocation.fromNamespaceAndPath(ShanxiSkeleton.MOD_ID,
                "textures/gui/wither_skeleton_producer_recipe.png"), 0, 0, 176, 70);
    }

    @Override
    @NotNull
    public IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(ModBlocks.WITHER_SKELETON_PRODUCER.get()));
    }

    @Override
    public void setRecipe(
            @NotNull IRecipeLayoutBuilder builder,
            @NotNull ItemStack recipe,
            @NotNull IFocusGroup focuses
    ) {
        IRecipeSlotBuilder[] outputSlots, roseOutputSlots;
        int i;

        setInputRecipe(builder, recipe, focuses);

        outputSlots = new IRecipeSlotBuilder[OUTPUT_SLOTS_COUNT];
        for (i = 0; i < OUTPUT_SLOTS_COUNT; i++) {
            outputSlots[i] = builder.addSlot(RecipeIngredientRole.OUTPUT, 80 + i * 18, 20);
        }
        outputSlots[0].addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.COAL));
        outputSlots[1].addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.TINY_COAL.get()));

        roseOutputSlots = new IRecipeSlotBuilder[ROSE_OUTPUT_SLOTS_COUNT];
        for (i = 0; i < ROSE_OUTPUT_SLOTS_COUNT; i++) {
            roseOutputSlots[i] = builder.addSlot(RecipeIngredientRole.OUTPUT, 80 + i * 18, 39);
        }
        roseOutputSlots[0].addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.WITHER_ROSE));
    }

    protected abstract void setInputRecipe(
            @NotNull IRecipeLayoutBuilder builder,
            @NotNull ItemStack recipe,
            @NotNull IFocusGroup focuses
    );

    @Override
    @NotNull
    public Component getTitle() {
        return Component.translatable("jei.shanxiskeleton.recipe.wither_skeleton_producer");
    }

    public static class ForWitherSkeleton extends WitherSkeletonProducerRecipeCategory {
        public ForWitherSkeleton(@NotNull IGuiHelper helper) {
            super(helper);
        }

        @Override
        protected void setInputRecipe(
                @NotNull IRecipeLayoutBuilder builder,
                @NotNull ItemStack recipe,
                @NotNull IFocusGroup focuses
        ) {
            builder.addSlot(RecipeIngredientRole.INPUT, 26, 20).addIngredient(VanillaTypes.ITEM_STACK, recipe);
            builder.addSlot(RecipeIngredientRole.INPUT, 26, 39).addIngredients(VanillaTypes.ITEM_STACK,
                    Arrays.stream(WitherSkeletonProducerBlockEntity.DEFAULT_ROSES.get()).sequential()
                            .map(block -> new ItemStack(block.asItem())).toList());
        }

        @Override
        @NotNull
        public RecipeType<ItemStack> getRecipeType() {
            return ModJEIPlugin.CATEGORY_WITHER_SKELETON_PRODUCER_FOR_WITHER_SKELETON;
        }
    }

    public static class ForRoses extends WitherSkeletonProducerRecipeCategory {
        public ForRoses(@NotNull IGuiHelper helper) {
            super(helper);
        }

        @Override
        protected void setInputRecipe(
                @NotNull IRecipeLayoutBuilder builder,
                @NotNull ItemStack recipe,
                @NotNull IFocusGroup focuses
        ) {
            builder.addSlot(RecipeIngredientRole.INPUT, 26, 20).addIngredient(VanillaTypes.ITEM_STACK,
                    new ItemStack(ModItems.WITHER_SKELETON.get()));
            builder.addSlot(RecipeIngredientRole.INPUT, 26, 39).addIngredient(VanillaTypes.ITEM_STACK, recipe);
        }

        @Override
        @NotNull
        public RecipeType<ItemStack> getRecipeType() {
            return ModJEIPlugin.CATEGORY_WITHER_SKELETON_PRODUCER_FOR_ROSES;
        }
    }
}
