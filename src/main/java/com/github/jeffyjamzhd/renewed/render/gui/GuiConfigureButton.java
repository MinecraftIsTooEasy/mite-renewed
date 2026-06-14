package com.github.jeffyjamzhd.renewed.render.gui;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import net.minecraft.GuiButton;
import net.minecraft.Minecraft;
import net.minecraft.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiConfigureButton extends GuiButton {
    private static final ResourceLocation RENEWED_WIDGET = new ResourceLocation(MiTERenewed.RESOURCE_ID + "textures/gui/widgets.png");

    public GuiConfigureButton(int id, int x, int y) {
        super(id, x, y, 20, 20, "");
    }

    @Override
    public void drawButton(Minecraft mc, int x, int y) {
        if (!this.drawButton) {
            return;
        }

        mc.getTextureManager().bindTexture(RENEWED_WIDGET);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        boolean hovered = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;
        int hoverState = getHoverState(hovered);
        this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, hoverState * 20, this.width, this.height);
    }
}
