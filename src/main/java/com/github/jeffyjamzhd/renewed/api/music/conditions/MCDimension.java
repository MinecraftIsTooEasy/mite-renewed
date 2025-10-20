package com.github.jeffyjamzhd.renewed.api.music.conditions;

import com.github.jeffyjamzhd.renewed.api.music.IMusicCondition;
import com.google.gson.annotations.SerializedName;
import net.minecraft.EntityPlayer;
import net.minecraft.World;

import javax.annotation.Nullable;

public class MCDimension implements IMusicCondition {
    @SerializedName("dimension")
    private String dimension;

    @Override
    public boolean check(@Nullable World world, @Nullable EntityPlayer player) {
        if (world != null) {
            return dimension.equals(world.getDimensionName().toLowerCase());
        }
        return false;
    }

    @Override
    public String getIdentifier() {
        return "miterenewed:dimension";
    }

    @Override
    public int getPriority() {
        return 3;
    }
}
