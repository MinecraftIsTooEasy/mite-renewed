package com.github.jeffyjamzhd.renewed.api.difficulty.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Minecraft;

@Environment(EnvType.CLIENT)
public interface IParameterField<T> {
    void setValue(T value);
    T getValue();

    default void setPosition(int x, int y) {}
    default int getWidth() { return 100; }
    default int getHeight() { return 20; }

    default boolean onMousePressed(Minecraft mc, int mouseEvent, int mX, int mY) { return false; }
    default void onMouseReleased(Minecraft mc, int mouseEvent, int mX, int mY) {}
    default void onDraw(Minecraft minecraft, int mX, int mY) {}
}
