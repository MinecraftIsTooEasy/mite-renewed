package com.github.jeffyjamzhd.renewed.api.difficulty.gui;

import net.minecraft.GuiButton;
import net.minecraft.I18n;
import net.minecraft.Minecraft;
import org.lwjgl.opengl.GL11;

public class GuiFieldSlider<T extends Number> extends GuiButton implements IParameterField<T> {
    private final float step;
    private FieldSuffix suffix;
    private float min = 0F;
    private float max = 1F;
    public T value;

    public boolean dragging = false;

    public GuiFieldSlider(T value, float step) {
        super(0, 0, 0, 100, 20, "");
        this.step = step;
        this.value = value;
    }

    @Override
    protected int getHoverState(boolean par1) {
        return 0;
    }

    @Override
    public boolean onMousePressed(Minecraft mc, int mouseEvent, int mX, int mY) {
        if (!super.mousePressed(mc, mX, mY)) {
            return false;
        }

        setValueByMouse(mX);
        return this.dragging = true;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mX, int mY) {
        float diff = getDifference();
        if (!this.enabled || !this.drawButton) {
            return;
        }

        if (this.dragging) {
            setValueByMouse(mX);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int factor = (int) ((this.value.floatValue() - this.min) / diff * (float) (this.width - 8));
        this.drawTexturedModalRect(this.xPosition + factor, this.yPosition, 0, 66, 4, 20);
        this.drawTexturedModalRect(this.xPosition + factor + 4, this.yPosition, 196, 66, 4, 20);
    }

    @Override
    public void setPosition(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
    }

    @Override
    public void onMouseReleased(Minecraft minecraft, int mouseEvent, int mX, int mY) {
        this.dragging = false;
    }

    @Override
    public void onDraw(Minecraft minecraft, int mX, int mY) {
        this.drawButton(minecraft, mX, mY);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void setValue(T value) {
        this.value = value;
        if (value instanceof Integer) {
            if (this.value.intValue() < min) {
                this.value = (T) Integer.valueOf((int) min);
            }

            if (this.value.intValue() > max) {
                this.value = (T) Integer.valueOf((int) max);
            }

            if (this.value.intValue() > 0) {
                this.displayString = "%d".formatted(this.value.intValue());

                if (this.suffix != null) {
                    this.displayString += " %s".formatted(this.suffix.getSuffix(this.value));
                }
            } else {
                this.displayString = I18n.getString("difficulty.disabled");
            }
        } else {
            if (this.value.floatValue() < min) {
                this.value = (T) Float.valueOf(min);
            }

            if (this.value.floatValue() > max) {
                this.value = (T) Float.valueOf(max);
            }

            this.displayString = "%d%%".formatted((int)(this.value.floatValue() * 100F));

            if (this.suffix != null) {
                this.displayString += " %s".formatted(this.suffix.getSuffix(this.value));
            }
        }
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @SuppressWarnings("unchecked")
    private void setValueByMouse(int mouseX) {
        float snapX = this.width / (getDifference() / this.step);

        if (this.value instanceof Float) {
            float newValue = (mouseX - (this.xPosition - snapX / 2)) / (float) (this.width - 8) * (max - min);
            newValue = (float) Math.round(newValue / this.step) * this.step;
            setValue((T) Float.valueOf(newValue));
        } else {
            int newValue = (int) ((mouseX - (this.xPosition - snapX / 2)) / (float) (this.width - 8) * (max - min));
            newValue = (int) Math.round(newValue / this.step) * (int) this.step;
            setValue((T) Integer.valueOf(newValue));
        }
    }

    public void setRange(float min, float max) {
        if (min > max) {
            throw new IllegalArgumentException("Supplied min %f and max %f are illogical".formatted(min, max));
        }

        this.min = min;
        this.max = max;
    }

    public void setSuffix(FieldSuffix suffix) {
        this.suffix = suffix;
    }

    private float getDifference() {
        return max - min;
    }
}
