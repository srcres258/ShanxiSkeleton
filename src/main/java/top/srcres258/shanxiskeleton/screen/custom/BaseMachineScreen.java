package top.srcres258.shanxiskeleton.screen.custom;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;
import top.srcres258.shanxiskeleton.block.entity.custom.BaseMachineBlockEntity;

public abstract class BaseMachineScreen<T extends BaseMachineBlockEntity, M extends BaseMachineMenu<T>>
        extends AbstractContainerScreen<M> {
    protected BaseMachineScreen(
            @NotNull M menu,
            @NotNull Inventory playerInventory,
            @NotNull Component title,
            int guiWidth,
            int guiHeight
    ) {
        super(menu, playerInventory, title);

        imageWidth = guiWidth;
        imageHeight = guiHeight;
    }

    /**
     * 在鼠标位置处绘制进度提示信息。
     */
    protected void renderProgressPercentageTooltip(
            @NotNull GuiGraphics guiGraphics,
            int mouseX,
            int mouseY,
            float progressPercentage
    ) {
        guiGraphics.renderTooltip(font, Component.translatable("gui.shanxiskeleton.progress",
                String.format("%.2f", progressPercentage)), mouseX, mouseY);
    }
}
