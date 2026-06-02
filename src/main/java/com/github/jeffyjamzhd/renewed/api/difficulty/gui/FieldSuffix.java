package com.github.jeffyjamzhd.renewed.api.difficulty.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.I18n;

@Environment(EnvType.CLIENT)
public enum FieldSuffix {
    SECONDS("seconds"),
    MINUTES("minutes"),
    DAYS("days"),
    LEVELS("levels");

    private final String suffix;

    FieldSuffix(String name) {
        this.suffix = name;
    }

    public <T extends Number> String getSuffix(T value) {
        if (value.intValue() == 1) {
            return I18n.getString("difficulty.parameter.suffix.%s.singular".formatted(suffix));
        }
        return I18n.getString("difficulty.parameter.suffix.%s".formatted(suffix));
    }
}
