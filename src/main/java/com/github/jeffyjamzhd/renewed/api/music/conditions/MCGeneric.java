package com.github.jeffyjamzhd.renewed.api.music.conditions;

import com.github.jeffyjamzhd.renewed.api.music.IMusicCondition;
import net.minecraft.EntityPlayer;
import net.minecraft.World;

public class MCGeneric implements IMusicCondition {
    @Override
    public boolean check(World world, EntityPlayer player) {
        return true;
    }

    @Override
    public String getIdentifier() {
        return "miterenewed:generic";
    }
}
