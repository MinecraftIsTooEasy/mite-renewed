package com.github.jeffyjamzhd.renewed.api.music.conditions;

import com.github.jeffyjamzhd.renewed.api.music.IMusicCondition;
import com.google.gson.annotations.SerializedName;
import net.minecraft.EntityPlayer;
import net.minecraft.World;

public class MCTime implements IMusicCondition {
    @SerializedName("from")
    private int lowerBound;
    @SerializedName("to")
    private int upperBound;

    @Override
    public boolean check(World world, EntityPlayer player) {
        int currentTime = world.getAdjustedTimeOfDay();
        return currentTime >= lowerBound && currentTime <= upperBound;
    }

    @Override
    public String getIdentifier() {
        return "miterenewed:time";
    }
}
