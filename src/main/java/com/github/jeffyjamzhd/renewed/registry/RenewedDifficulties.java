package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyParameter;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyParameter.*;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyProvider;
import net.minecraft.ResourceLocation;

import java.util.ArrayList;

import static com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyProvider.registerParameter;

public class RenewedDifficulties {
    public static final ArrayList<Difficulty> LIST = new ArrayList<>();

    public static Difficulty HARD;
    public static Difficulty EXTREME;
    public static Difficulty LEGENDARY;

    public static DifficultyParameter<Integer> MINIMUM_HEALTH;
    public static DifficultyParameter<Integer> MAXIMUM_HEALTH;
    public static DifficultyParameter<Integer> MINIMUM_HUNGER;
    public static DifficultyParameter<Integer> MAXIMUM_HUNGER;
    public static DifficultyParameter<Float> MINING_FACTOR;
    public static DifficultyParameter<Float> PLAYER_DAMAGE_FACTOR;
    public static DifficultyParameter<Float> MOB_DAMAGE_FACTOR;
    public static DifficultyParameter<Float> FALL_DAMAGE_FACTOR;

    public static DifficultyParameter<Boolean> NON_SOLID_LEAVES;
    public static DifficultyParameter<Boolean> CLIMBABLE_VINES;
    public static DifficultyParameter<Boolean> ANIMALS_ALWAYS_DROP_LOOT;
    public static DifficultyParameter<Boolean> DIRT_CAVES_IN;
    public static DifficultyParameter<Boolean> CAN_DISTURB_GROUND;

    private static ResourceLocation loc(String str) {
        return new ResourceLocation(MiTERenewed.RESOURCE_ID + str);
    }

    public static void init() {}

    static {
        MINIMUM_HEALTH = registerParameter(new DPInteger(loc( "PlayerMinimumHealth"), 1, 10), 3);
        MAXIMUM_HEALTH = registerParameter(new DPInteger(loc( "PlayerMaximumHealth"), 1, 10), 10);
        MINIMUM_HUNGER = registerParameter(new DPInteger(loc( "PlayerMinimumHunger"), 1, 10), 3);
        MAXIMUM_HUNGER = registerParameter(new DPInteger(loc( "PlayerMaximumHunger"), 1, 10), 10);
        MINING_FACTOR = registerParameter(new DPFloatPercent(loc("MiningFactor"), .5F, 4F, .25F), 1F);
        PLAYER_DAMAGE_FACTOR = registerParameter(new DPFloatPercent(loc("PlayerDamageFactor"), .25F, 4F, .25F), 1F);
        MOB_DAMAGE_FACTOR = registerParameter(new DPFloatPercent(loc("MobDamageFactor"), .25F, 4F, .25F), 1F);
        FALL_DAMAGE_FACTOR = registerParameter(new DPFloatPercent(loc("FallDamageFactor"), .25F, 4F, .25F), 1F);

        NON_SOLID_LEAVES = registerParameter(new DPBoolean(loc("NonSolidLeaves")), false);
        CLIMBABLE_VINES = registerParameter(new DPBoolean(loc("ClimbableVines")), true);
        ANIMALS_ALWAYS_DROP_LOOT = registerParameter(new DPBoolean(loc("AnimalsAlwaysDropLoot")), false);
        DIRT_CAVES_IN = registerParameter(new DPBoolean(loc("DirtCavesIn")), true);
        CAN_DISTURB_GROUND = registerParameter(new DPBoolean(loc("CanDisturbGround")), true);

        EXTREME = DifficultyProvider.getBuilder(loc("extreme"))
                .withBase(DifficultyProvider.defaults)
                .build();

        LEGENDARY = DifficultyProvider.getBuilder(loc("legendary"))
                .withBase(DifficultyProvider.defaults)
                .withParam(MAXIMUM_HEALTH, 8)
                .withParam(MAXIMUM_HUNGER, 8)
                .withParam(MINING_FACTOR, 0.75F)
                .withParam(MOB_DAMAGE_FACTOR, 1.5F)
                .withParam(NON_SOLID_LEAVES, true)
                .withParam(CLIMBABLE_VINES, false)
                .build();

        HARD = DifficultyProvider.getBuilder(loc("hard"))
                .withBase(DifficultyProvider.defaults)
                .withParam(MINIMUM_HEALTH, 5)
                .withParam(MINIMUM_HUNGER, 5)
                .withParam(MINING_FACTOR, 2F)
                .build();
    }
}
