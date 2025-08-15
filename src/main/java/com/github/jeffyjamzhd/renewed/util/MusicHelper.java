package com.github.jeffyjamzhd.renewed.util;

import com.github.jeffyjamzhd.renewed.api.ISoundManager;
import net.fabricmc.loader.impl.util.StringUtil;
import net.minecraft.Minecraft;
import net.minecraft.SoundManager;
import net.minecraft.WorldClient;

public class MusicHelper {
    /**
     * Simulates the intended pitch of music based on provided time.
     * @param world Client's world
     * @param time Time of the world
     * @return The intended music pitch
     */
    public static float simulateIntendedPitch(WorldClient world, int time) {
        float offset = 0F;
        float target = targetOffset(world);

        // Calculate time factor
        if (time > WorldClient.getTimeOfSunset() - 1500 || time < WorldClient.getTimeOfSunrise()) {
            // Nighttime
            int speed = world.isBloodMoon(false) ? 2000 : 4000;
            time -= -1500 + WorldClient.getTimeOfSunset();

            if (time <= 0 || time > speed)
                offset += target;
            else
                offset += Math.min(target * (time / (float) speed), target);
        } else {
            int speed = 3000;
            time -= WorldClient.getTimeOfSunrise();
            offset += Math.max(target - (time / (float) speed), 0F);
        }

        // Calculate weather factor and return
        offset += (float) (0.05 * world.getRainStrength(0.001F));
        offset += (float) (0.05 * world.getWeightedThunderStrength(0.001F));
        return 1F - offset;
    }

    /**
     * Gets target offset for time based music dilation
     * @param world Client's world
     * @return Target offset
     */
    public static float targetOffset(WorldClient world) {
        SoundManager manager = Minecraft.getMinecraft().sndManager;
        ISoundManager access = (ISoundManager) manager;

        if (!getSimpleName(access.mr$getMusicTitle()).equals("magnetic")) {
            if (world.isBloodMoon(false))
                return .4F;
            return .13F;
        } else {
            return .05F;
        }
    }

    /**
     * Strips a music filename to it's simplest form
     */
    public static String getSimpleName(String fileName) {
        String result = fileName;
        String[] split1 = fileName.split(":");
        if (split1.length > 1)
            result = split1[1];
        return result.substring(0, result.length() - 4);
    }
}
