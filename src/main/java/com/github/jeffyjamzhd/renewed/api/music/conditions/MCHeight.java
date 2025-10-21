package com.github.jeffyjamzhd.renewed.api.music.conditions;

import com.github.jeffyjamzhd.renewed.api.music.IMusicCondition;
import com.google.gson.annotations.SerializedName;
import net.minecraft.EntityPlayer;
import net.minecraft.World;

import javax.annotation.Nullable;

public class MCHeight implements IMusicCondition {
    @SerializedName("from")
    private int lowerBound = Integer.MIN_VALUE;
    @SerializedName("to")
    private int upperBound = Integer.MIN_VALUE;

    @Override
    public boolean check(@Nullable World world, @Nullable EntityPlayer player) {
        if (player != null) {
            double currentY = player.posY;
            return currentY >= this.lowerBound && currentY <= this.upperBound;
        }
        return false;
    }

    @Override
    public void validate() throws Exception {
        if (this.lowerBound == Integer.MIN_VALUE || this.upperBound == Integer.MIN_VALUE) {
            throw new Exception("Condition metadata improperly set (missing keys or misspelled keys)");
        }

        if (this.lowerBound > this.upperBound) {
            throw new Exception("From is greater than To in height check!");
        }

        if (this.lowerBound < 0 || this.upperBound > 256) {
            throw new Exception("Out of valid range (y0 - y256)");
        }

        if (this.lowerBound == 0) {
            this.lowerBound = Integer.MIN_VALUE;
        }
        if (this.upperBound == 256) {
            this.upperBound = Integer.MAX_VALUE;
        }
    }

    @Override
    public String getIdentifier() {
        return "miterenewed:height";
    }
}
