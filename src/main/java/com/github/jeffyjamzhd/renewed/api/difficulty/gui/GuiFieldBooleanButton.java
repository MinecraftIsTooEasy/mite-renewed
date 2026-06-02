package com.github.jeffyjamzhd.renewed.api.difficulty.gui;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyParameter;
import net.minecraft.GuiButton;
import net.minecraft.I18n;
import net.minecraft.Minecraft;

public class GuiFieldBooleanButton extends GuiButton implements IParameterField<Boolean> {
    public static final String[] strings = {"difficulty.disabled", "difficulty.enabled"};
    private boolean enabled = false;

    public Difficulty difficulty;
    public DifficultyParameter<Boolean> parameter;

    public GuiFieldBooleanButton(Boolean enabled, Difficulty difficulty, DifficultyParameter<Boolean> param) {
        super(0, 0, 0, 100, 20, "");
        this.enabled = enabled;
        this.difficulty = difficulty;
        this.parameter = param;
    }

    @Override
    public boolean onMousePressed(Minecraft mc, int mouseEvent, int mX, int mY) {
        if (super.mousePressed(mc, mX, mY)) {
            setValue(!this.enabled);
            return true;
        }
        return false;
    }

    @Override
    public void setPosition(int x, int y) {
        this.xPosition = x;
        this.yPosition = y;
    }

    @Override
    public void setValue(Boolean value) {
        this.enabled = parameter.sanitizeValue(difficulty, value);
        this.displayString = I18n.getString(strings[value ? 1 : 0]);
    }

    @Override
    public Boolean getValue() {
        return this.enabled;
    }

    @Override
    public void onDraw(Minecraft minecraft, int mX, int mY) {
        super.drawButton(minecraft, mX, mY);
    }
}
