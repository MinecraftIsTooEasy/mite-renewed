package com.github.jeffyjamzhd.renewed.api.difficulty.gui;

import net.minecraft.GuiButton;
import net.minecraft.I18n;
import net.minecraft.Minecraft;

public class GuiFieldEnumButton extends GuiButton implements IParameterField<Integer> {
    private final String[] strings;
    private int value = 0;

    public GuiFieldEnumButton(String[] strings) {
        super(0, 0, 0, 100, 20, "");
        this.strings = strings;
    }

    @Override
    public boolean onMousePressed(Minecraft mc, int mouseEvent, int mX, int mY) {
        if (super.mousePressed(mc, mX, mY)) {
            setValue(this.value + 1);
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
    public void setValue(Integer value) {
        this.value = value % strings.length;
        this.displayString = I18n.getString(strings[this.value]);
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public void onDraw(Minecraft minecraft, int mX, int mY) {
        super.drawButton(minecraft, mX, mY);
    }
}
