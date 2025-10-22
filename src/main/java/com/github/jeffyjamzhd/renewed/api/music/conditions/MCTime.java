package com.github.jeffyjamzhd.renewed.api.music.conditions;

import com.github.jeffyjamzhd.renewed.api.music.IMusicCondition;
import com.google.gson.annotations.SerializedName;
import net.minecraft.EntityPlayer;
import net.minecraft.World;

import javax.annotation.Nullable;

public class MCTime implements IMusicCondition {
    @SerializedName("from")
    private int lowerBound = Integer.MIN_VALUE;
    @SerializedName("to")
    private int upperBound = Integer.MIN_VALUE;

    @Override
    public boolean check(@Nullable World world, @Nullable EntityPlayer player) {
        if (world != null) {
            int currentTime = world.getTimeOfDay();
            return currentTime >= this.lowerBound && currentTime <= this.upperBound;
        }
        return false;
    }

    @Override
    public void validate() throws Exception {
        if (this.lowerBound == Integer.MIN_VALUE || this.upperBound == Integer.MIN_VALUE) {
            throw new Exception("Condition metadata improperly set (missing keys or misspelled keys)");
        }

        if (this.lowerBound > this.upperBound) {
            throw new Exception("From is greater than To in time check!");
        }

        if (this.lowerBound < 0 || this.upperBound > 24000) {
            throw new Exception("Out of valid range (0 - 24000)");
        }
    }

    @Override
    public String getIdentifier() {
        return "miterenewed:time";
    }
}
