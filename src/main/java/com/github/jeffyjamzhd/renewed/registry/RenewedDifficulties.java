package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyParameter;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyParameter.*;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyProvider;
import net.minecraft.ResourceLocation;

import java.util.ArrayList;

public class RenewedDifficulties {
    public static final ArrayList<Difficulty> LIST = new ArrayList<>();

    public static DifficultyParameter<Integer> MINIMUM_HEALTH;
    public static DifficultyParameter<Integer> MAXIMUM_HEALTH;
    public static DifficultyParameter<Integer> MINIMUM_HUNGER;
    public static DifficultyParameter<Integer> MAXIMUM_HUNGER;

    private static ResourceLocation loc(String str) {
        return new ResourceLocation(MiTERenewed.RESOURCE_ID, str);
    }

    public static void init() {}

    static {
        MINIMUM_HEALTH = DifficultyProvider.registerParameter(new DPInteger(loc( "PlayerMinimumHealth"), 1, 10), 3);
        MAXIMUM_HEALTH = DifficultyProvider.registerParameter(new DPInteger(loc( "PlayerMaximumHealth"), 1, 10), 10);
        MINIMUM_HUNGER = DifficultyProvider.registerParameter(new DPInteger(loc( "PlayerMinimumHunger"), 1, 10), 3);
        MAXIMUM_HUNGER = DifficultyProvider.registerParameter(new DPInteger(loc( "PlayerMaximumHunger"), 1, 10), 10);
    }
}
