package com.github.jeffyjamzhd.renewed.render.gui;

import net.minecraft.GuiButton;
import net.minecraft.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiStepSlider extends GuiButton {
    private float min = 0F;
    private float max = 0F;
    private float step = .1F;
    public float value = 1F;

    public boolean dragging = false;

    public GuiStepSlider(int id, int x, int y, float value, float step) {
        super(id, x, y, 100, 20, "");
        this.value = value;
        this.step = step;
    }

    @Override
    protected int getHoverState(boolean par1) {
        return 0;
    }

    @Override
    public boolean mousePressed(Minecraft mc, int x, int y) {
        if (!super.mousePressed(mc, x, y)) {
            return false;
        }

        setValueByMouse(x);
        return this.dragging = true;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int x, int y) {
        if (!this.enabled || !this.drawButton) {
            return;
        }

        if (this.dragging) {
            setValueByMouse(x);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.drawTexturedModalRect(this.xPosition + (int) (this.value * (float) (this.width - 8)), this.yPosition, 0, 66, 4, 20);
        this.drawTexturedModalRect(this.xPosition + (int) (this.value * (float) (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
    }

    @Override
    public void mouseReleased(int par1, int par2) {
        this.dragging = false;
    }

    private void setValueByMouse(int mouseX) {
        this.value = (float) (mouseX - (this.xPosition + 4)) / (float) (this.width - 8) * (max - min);
        this.value = Math.round(value / step) * step;

        if (this.value < min) {
            this.value = min;
        }

        if (this.value > max) {
            this.value = max;
        }
    }

    public void setRange(float min, float max) {
        if (min > max) {
            throw new IllegalArgumentException("Supplied min %f and max %f are illogical".formatted(min, max));
        }

        this.min = min;
        this.max = max;
    }

    public void setStep(float step) {
        this.step = step;
    }


}
