package com.github.jeffyjamzhd.renewed.api.music.conditions;

import com.github.jeffyjamzhd.renewed.api.music.IMusicCondition;
import com.google.gson.annotations.SerializedName;
import net.minecraft.EntityPlayer;
import net.minecraft.World;

import javax.annotation.Nullable;

public class MCHumidity implements IMusicCondition {
    @SerializedName("from")
    private float lowerBound;
    @SerializedName("to")
    private float upperBound;

    @Override
    public boolean check(@Nullable World world, @Nullable EntityPlayer player) {
        if (world != null && player != null) {
            float humidity = world.getBiomeGenForCoords(player.getBlockPosX(), player.getBlockPosZ())
                    .getFloatTemperature();
            return humidity >= lowerBound && humidity <= upperBound;
        }
        return false;
    }

    @Override
    public String getIdentifier() {
        return "miterenewed:humidity";
    }
}
