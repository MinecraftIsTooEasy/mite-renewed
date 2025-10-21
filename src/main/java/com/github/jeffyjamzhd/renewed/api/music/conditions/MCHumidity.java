package com.github.jeffyjamzhd.renewed.api.music.conditions;

import com.github.jeffyjamzhd.renewed.api.music.IMusicCondition;
import com.google.gson.annotations.SerializedName;
import net.minecraft.EntityPlayer;
import net.minecraft.World;

import javax.annotation.Nullable;

public class MCHumidity implements IMusicCondition {
    @SerializedName("from")
    private float lowerBound = Float.NaN;
    @SerializedName("to")
    private float upperBound = Float.NaN;

    @Override
    public boolean check(@Nullable World world, @Nullable EntityPlayer player) {
        if (world != null && player != null) {
            float humidity = world.getBiomeGenForCoords(player.getBlockPosX(), player.getBlockPosZ())
                    .getFloatTemperature();
            return humidity >= this.lowerBound && humidity <= this.upperBound;
        }
        return false;
    }

    @Override
    public void validate() throws Exception {
        if (Float.isNaN(this.lowerBound) || Float.isNaN(this.upperBound)) {
            throw new Exception("Condition metadata improperly set (missing values or misspelled keys)");
        }

        if (this.lowerBound > this.upperBound) {
            throw new Exception("From is greater than To in humidity check!");
        }

        if (this.lowerBound < 0.0 || this.upperBound > 2.0) {
            throw new Exception("Out of valid range (0.0 - 2.0)");
        }
    }

    @Override
    public String getIdentifier() {
        return "miterenewed:humidity";
    }
}
