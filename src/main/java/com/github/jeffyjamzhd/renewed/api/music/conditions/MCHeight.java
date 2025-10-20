package com.github.jeffyjamzhd.renewed.api.music.conditions;

import com.github.jeffyjamzhd.renewed.api.music.IMusicCondition;
import com.google.gson.annotations.SerializedName;
import net.minecraft.EntityPlayer;
import net.minecraft.World;

import javax.annotation.Nullable;

public class MCHeight implements IMusicCondition {
    @SerializedName("from")
    private int lowerBound;
    @SerializedName("to")
    private int upperBound;

    @Override
    public boolean check(@Nullable World world, @Nullable EntityPlayer player) {
        if (player != null) {
            double currentY = player.posY;
            return currentY >= lowerBound && currentY <= upperBound;
        }
        return false;
    }

    @Override
    public String getIdentifier() {
        return "miterenewed:height";
    }
}
