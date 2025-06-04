package top.srcres258.shanxiskeleton.compat.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.ModBlocks;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonSlaughtererBlockEntity;
import top.srcres258.shanxiskeleton.item.ModItems;

public class WitherSkeletonSlaughtererRecipeCategory implements IRecipeCategory<ItemStack> {
    private static final int OUTPUT_SLOTS_COUNT = WitherSkeletonSlaughtererBlockEntity.OUTPUT_SLOTS_COUNT;

    private final IGuiHelper helper;
    private final IDrawable background;

    public WitherSkeletonSlaughtererRecipeCategory(@NotNull IGuiHelper helper) {
        this.helper = helper;

        background = helper.createDrawable(ResourceLocation.fromNamespaceAndPath(ShanxiSkeleton.MOD_ID,
                "textures/gui/wither_skeleton_slaughterer_recipe.png"), 0, 0, 176, 57);
    }

    @Override
    public int getWidth() {
        return background.getWidth();
    }

    @Override
    public int getHeight() {
        return background.getHeight();
    }

    @Override
    public void draw(
            @NotNull ItemStack recipe,
            @NotNull IRecipeSlotsView recipeSlotsView,
            @NotNull GuiGraphics guiGraphics,
            double mouseX,
            double mouseY
    ) {
        background.draw(guiGraphics);
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return helper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(ModBlocks.WITHER_SKELETON_SLAUGHTERER.get()));
    }

    @Override
    public void setRecipe(
            @NotNull IRecipeLayoutBuilder builder,
            @NotNull ItemStack recipe,
            @NotNull IFocusGroup focuses
    ) {
        IRecipeSlotBuilder[] outputSlots;
        int i;

        builder.addSlot(RecipeIngredientRole.INPUT, 26, 20).add(VanillaTypes.ITEM_STACK,
                new ItemStack(ModItems.WITHER_SKELETON.get()));

        outputSlots = new IRecipeSlotBuilder[OUTPUT_SLOTS_COUNT];
        for (i = 0; i < OUTPUT_SLOTS_COUNT; i++) {
            outputSlots[i] = builder.addSlot(RecipeIngredientRole.OUTPUT, 80 + i * 18, 20);
        }
        outputSlots[0].add(VanillaTypes.ITEM_STACK, new ItemStack(Items.COAL));
        outputSlots[1].add(VanillaTypes.ITEM_STACK, new ItemStack(ModItems.TINY_COAL.get()));
        outputSlots[2].add(VanillaTypes.ITEM_STACK, new ItemStack(Items.BONE));
        outputSlots[3].add(VanillaTypes.ITEM_STACK, new ItemStack(Items.WITHER_SKELETON_SKULL));
    }

    @Override
    @NotNull
    public Component getTitle() {
        return Component.translatable("jei.shanxiskeleton.recipe.wither_skeleton_slaughterer");
    }

    @Override
    @NotNull
    public IRecipeType<ItemStack> getRecipeType() {
        return ModJEIPlugin.CATEGORY_WITHER_SKELETON_SLAUGHTERER;
    }
}
