package com.github.jeffyjamzhd.renewed.api.difficulty.gui;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyParameter;
import net.minecraft.GuiButton;
import net.minecraft.I18n;
import net.minecraft.Minecraft;
import net.minecraft.SoundManager;
import org.lwjgl.opengl.GL11;

public class GuiFieldSlider<T extends Number> extends GuiButton implements IParameterField<T> {
    private final float step;
    private FieldSuffix suffix;
    private float min = 0F;
    private float max = 1F;

    public Difficulty difficulty;
    public DifficultyParameter<T> parameter;
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

        setValueByMouse(mc, mX);
        return this.dragging = true;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mX, int mY) {
        float diff = getDifference();
        if (!this.enabled || !this.drawButton) {
            return;
        }

        if (this.dragging) {
            setValueByMouse(mc, mX);
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
    public void setValue(T value) {
        this.value = parameter.sanitizeValue(difficulty, value);
        if (value instanceof Integer) {
            if (this.value.intValue() > 0) {
                this.displayString = "%d".formatted(this.value.intValue());

                if (this.suffix != null) {
                    this.displayString += " %s".formatted(this.suffix.getSuffix(this.value));
                }
            } else {
                this.displayString = I18n.getString("difficulty.disabled");
            }
        } else {
            float percentValue = this.value.floatValue() * 100F;
            String raw = (percentValue % 1 == 0) ? "%.0f%%" : "%.1f%%";

            this.displayString = raw.formatted(percentValue);

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
    private void setValueByMouse(Minecraft mc, int mouseX) {
        float previousValue = this.value != null ? this.value.floatValue() : Float.NaN;
        float ratio = (mouseX - (this.xPosition + 4)) / (float) (this.width - 8);
        ratio = Math.max(0.0F, Math.min(1.0F, ratio));

        float rawValue = min + ratio * (max - min);
        float snappedValue = (float) Math.round(rawValue / this.step) * this.step;
        snappedValue = Math.max(min, Math.min(max, snappedValue));

        if (this.value instanceof Float) {
            setValue((T) Float.valueOf(snappedValue));
        } else {
            setValue((T) Integer.valueOf((int) snappedValue));
        }

        float newValue = this.value != null ? this.value.floatValue() : Float.NaN;
        if (!Float.isNaN(previousValue) && Math.abs(newValue - previousValue) > 0.0001F) {
            float pitchRatio = (snappedValue - min) / getDifference();
            float pitch = 0.5F + (pitchRatio * 1.5F);

            mc.sndManager.playSoundFX("random.click", 1F, pitch);
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
