package top.srcres258.shanxiskeleton.screen.custom;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.ShanxiSkeleton;
import top.srcres258.shanxiskeleton.block.entity.custom.WitherSkeletonProducerBlockEntity;
import top.srcres258.shanxiskeleton.util.RenderHelper;

public class WitherSkeletonProducerScreen extends BaseMachineScreen<WitherSkeletonProducerBlockEntity, WitherSkeletonProducerMenu> {
    public static final int PROGRESS_BAR_PIXEL_WIDTH = 26;
    public static final int PROGRESS_BAR_PIXEL_HEIGHT = 16;
    public static final int GUI_WIDTH = 176;
    public static final int GUI_HEIGHT = 147;

    private static final ResourceLocation GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(ShanxiSkeleton.MOD_ID,
            "textures/gui/wither_skeleton_producer.png");

    public WitherSkeletonProducerScreen(
            @NotNull WitherSkeletonProducerMenu menu,
            @NotNull Inventory playerInventory,
            @NotNull Component title
    ) {
        super(menu, playerInventory, title, GUI_WIDTH, GUI_HEIGHT);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // Override this method in order to let the screen only render the GUI title,
        // regardless of the player inventory title.
        guiGraphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float v, int i, int i1) {
        RenderHelper.setShaderTexture(0, GUI_TEXTURE);
        guiGraphics.blit(RenderType::guiTextured, GUI_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, 256, 256);
        renderProgressBar(guiGraphics);
    }

    private void renderProgressBar(@NotNull GuiGraphics guiGraphics) {
        if (getMenu().isCrafting()) {
            RenderHelper.setShaderTexture(0, GUI_TEXTURE);
            guiGraphics.blit(RenderType::guiTextured, GUI_TEXTURE, leftPos + 48, topPos + 20, 176, 0,
                    getMenu().getScaledProgress(), PROGRESS_BAR_PIXEL_HEIGHT, 256, 256);
            guiGraphics.blit(RenderType::guiTextured, GUI_TEXTURE, leftPos + 48, topPos + 39, 176, 0,
                    getMenu().getScaledRoseProgress(), PROGRESS_BAR_PIXEL_HEIGHT, 256, 256);
        }
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        float progressPercentage, roseProgressPercentage;
        int guiMouseX, guiMouseY;

        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);

        // 计算GUI坐标系中的鼠标位置。
        guiMouseX = mouseX - leftPos;
        guiMouseY = mouseY - topPos;

        // 凋灵骷髅进度箭头区域：(48, 20) -> (73, 35)
        // 当鼠标在此区域时，绘制进度提示信息。
        if (guiMouseX >= 48 && guiMouseX <= 73 && guiMouseY >= 20 && guiMouseY <= 35) {
            progressPercentage = getMenu().getProgress() * 100F;
            renderProgressPercentageTooltip(guiGraphics, mouseX, mouseY, progressPercentage);
        }

        // 凋零玫瑰进度箭头区域：(48, 39) -> (73, 53)
        // 当鼠标在此区域时，绘制进度提示信息。
        if (guiMouseX >= 48 && guiMouseX <= 73 && guiMouseY >= 39 && guiMouseY <= 53) {
            roseProgressPercentage = getMenu().getRoseProgress() * 100F;
            renderProgressPercentageTooltip(guiGraphics, mouseX, mouseY, roseProgressPercentage);
        }
    }
}
