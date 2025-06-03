package top.srcres258.shanxiskeleton.screen.custom;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonBreederBlockEntity;
import top.srcres258.shanxiskeleton.util.RenderHelper;

public class WitherSkeletonBreederScreen extends BaseMachineScreen<WitherSkeletonBreederBlockEntity, WitherSkeletonBreederMenu> {
    public static final int PROGRESS_BAR_PIXEL_WIDTH = 26;
    public static final int PROGRESS_BAR_PIXEL_HEIGHT = 12;
    public static final int GUI_WIDTH = 176;
    public static final int GUI_HEIGHT = 133;

    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(ShanxiSkeleton.MOD_ID,
            "textures/gui/wither_skeleton_breeder.png");

    public WitherSkeletonBreederScreen(
            @NotNull WitherSkeletonBreederMenu menu,
            @NotNull Inventory playerInventory,
            @NotNull Component title
    ) {
        super(menu, playerInventory, title, GUI_WIDTH, GUI_HEIGHT);

        inventoryLabelX = 8;
        inventoryLabelY = 39;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderHelper.setShaderTexture(0, GUI_TEXTURE);
        guiGraphics.blit(RenderType::guiTextured, GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
        renderProgressBar(guiGraphics);
    }

    private void renderProgressBar(@NotNull GuiGraphics guiGraphics) {
        if (getMenu().isCrafting()) {
            RenderHelper.setShaderTexture(0, GUI_TEXTURE);
            guiGraphics.blit(RenderType::guiTextured, GUI_TEXTURE, leftPos + 84, topPos + 22, 176F, 0F,
                    getMenu().getScaledProgress(), PROGRESS_BAR_PIXEL_HEIGHT, 256, 256);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        float progressPercentage;
        int guiMouseX, guiMouseY;

        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);

        // 计算GUI坐标系中的鼠标位置。
        guiMouseX = mouseX - leftPos;
        guiMouseY = mouseY - topPos;

        // 进度箭头区域：(84, 22) -> (110, 34)
        // 当鼠标在此区域时，绘制进度提示信息。
        if (guiMouseX >= 84 && guiMouseX <= 110 && guiMouseY >= 22 && guiMouseY <= 34) {
            progressPercentage = getMenu().getProgress() * 100F;
            renderProgressPercentageTooltip(guiGraphics, mouseX, mouseY, progressPercentage);
        }
    }
}
