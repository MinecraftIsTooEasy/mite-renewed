package com.github.jeffyjamzhd.renewed.api.music.conditions;

import com.github.jeffyjamzhd.renewed.api.music.IMusicCondition;
import net.minecraft.EntityPlayer;
import net.minecraft.World;

import javax.annotation.Nullable;

public class MCGeneric implements IMusicCondition {
    @Override
    public boolean check(@Nullable World world, @Nullable EntityPlayer player) {
        return false;
    }

    @Override
    public String getIdentifier() {
        return "miterenewed:generic";
    }
}
