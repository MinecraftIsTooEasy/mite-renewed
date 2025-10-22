package com.github.jeffyjamzhd.renewed.util;

import com.github.jeffyjamzhd.renewed.api.ISoundManager;
import net.minecraft.Minecraft;
import net.minecraft.SoundManager;
import net.minecraft.WorldClient;

public class MusicHelper {
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
