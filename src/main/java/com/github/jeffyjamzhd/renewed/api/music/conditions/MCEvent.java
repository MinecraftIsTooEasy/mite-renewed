package com.github.jeffyjamzhd.renewed.api.music.conditions;

import com.github.jeffyjamzhd.renewed.api.music.IMusicCondition;
import com.google.gson.annotations.SerializedName;
import net.minecraft.EntityPlayer;
import net.minecraft.World;

import javax.annotation.Nullable;

public class MCEvent implements IMusicCondition {
    @SerializedName("blood_moon")
    private boolean bloodMoon;
    @SerializedName("blue_moon")
    private boolean blueMoon;
    @SerializedName("harvest_moon")
    private boolean harvestMoon;
    @SerializedName("rain")
    private boolean rain;
    @SerializedName("thunder")
    private boolean thunder;

    @Override
    public boolean check(@Nullable World world, @Nullable EntityPlayer player) {
        if (world != null) {
            if (bloodMoon && world.isBloodMoonNight())
                return true;
            if (blueMoon && world.isBlueMoonNight())
                return true;
            if (harvestMoon && world.isHarvestMoonNight())
                return true;
            if (rain && world.getRainStrength(0) > 0F)
                return true;
            return thunder && world.getWeightedThunderStrength(0) > 0F;
        }
        return false;
    }

    @Override
    public void validate() throws Exception {
        if (!bloodMoon && !harvestMoon && !blueMoon && !rain && !thunder) {
            throw new Exception("No event defined for condition");
        }
    }

    @Override
    public String getIdentifier() {
        return "miterenewed:event";
    }

    @Override
    public int getPriority() {
        return 3;
    }
}
