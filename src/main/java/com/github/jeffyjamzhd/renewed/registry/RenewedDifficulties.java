package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyParameter;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyParameter.*;
import com.github.jeffyjamzhd.renewed.api.difficulty.DifficultyProvider;
import com.github.jeffyjamzhd.renewed.api.difficulty.gui.FieldSuffix;
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
    public static DifficultyParameter<Integer> LEVELS_NEEDED_FOR_STAT_UP;
    public static DifficultyParameter<Float> MINING_FACTOR;
    public static DifficultyParameter<Float> PLAYER_DAMAGE_FACTOR;
    public static DifficultyParameter<Float> MOB_DAMAGE_FACTOR;
    public static DifficultyParameter<Float> FALL_DAMAGE_FACTOR;

    public static DifficultyParameter<Boolean> NON_SOLID_LEAVES;
    public static DifficultyParameter<Boolean> CLIMBABLE_VINES;
    public static DifficultyParameter<Boolean> CAN_DISTURB_GROUND;

    public static DifficultyParameter<Integer> DAY_MINUTE_LENGTH;
    public static DifficultyParameter<Integer> NIGHT_MINUTE_LENGTH;
    public static DifficultyParameter<Integer> WEATHER_GRACE_PERIOD;
    public static DifficultyParameter<Integer> ANIMAL_SICKNESS_BEHAVIOR;
    public static DifficultyParameter<Integer> CROP_SICKNESS_BEHAVIOR;
    public static DifficultyParameter<Boolean> ENTITIES_DROP_LOOT_ALWAYS;
    public static DifficultyParameter<Integer> CHICKEN_BREEDING_ITEM;

    public static final int ANIMAL_SICKNESS_DISABLED = 0;
    public static final int ANIMAL_SICKNESS_ORIGINAL = 1;
    public static final int ANIMAL_SICKNESS_RENEWED = 2;

    public static final int CROP_SICKNESS_DISABLED = 0;
    public static final int CROP_SICKNESS_ORIGINAL = 1;
    public static final int CROP_SICKNESS_RENEWED = 2;

    public static final int CHICKEN_USE_SEED = 0;
    public static final int CHICKEN_USE_WORM = 1;

    private static ResourceLocation loc(String str) {
        return new ResourceLocation(MiTERenewed.RESOURCE_ID + str);
    }

    public static void init() {}

    static {
        MINIMUM_HEALTH              = registerParameter(new DPIntegerSlider(loc( "PlayerMinimumHealth"), Category.GENERAL, 1, 20), 3);
        MINIMUM_HUNGER              = registerParameter(new DPIntegerSlider(loc( "PlayerMinimumHunger"), Category.GENERAL, 1, 20), 3);
        MAXIMUM_HEALTH              = registerParameter(new DPIntegerSlider(loc( "PlayerMaximumHealth"), Category.GENERAL, 1, 20), 10);
        MAXIMUM_HUNGER              = registerParameter(new DPIntegerSlider(loc( "PlayerMaximumHunger"), Category.GENERAL, 1, 20), 10);
        LEVELS_NEEDED_FOR_STAT_UP   = registerParameter(new DPIntegerSlider(loc("LevelsNeededForStatUp"), Category.GENERAL, FieldSuffix.LEVELS, 1, 10, 1), 5);

        MINING_FACTOR               = registerParameter(new DPFloatSlider(loc("MiningFactor"), Category.INTERACTION, .5F, 4F, .25F), 1F);
        PLAYER_DAMAGE_FACTOR        = registerParameter(new DPFloatSlider(loc("PlayerDamageFactor"), Category.INTERACTION, .25F, 2F, .25F), 1F);
        MOB_DAMAGE_FACTOR           = registerParameter(new DPFloatSlider(loc("MobDamageFactor"), Category.INTERACTION,.25F, 4F, .25F), 1F);
        FALL_DAMAGE_FACTOR          = registerParameter(new DPFloatSlider(loc("FallDamageFactor"), Category.INTERACTION,.25F, 4F, .25F), 1F);
        CAN_DISTURB_GROUND          = registerParameter(new DPBoolean(loc("CanDisturbGround"), Category.INTERACTION), true);
        NON_SOLID_LEAVES            = registerParameter(new DPBoolean(loc("NonSolidLeaves"), Category.INTERACTION), false);
        CLIMBABLE_VINES             = registerParameter(new DPBoolean(loc("ClimbableVines"), Category.INTERACTION), true);

        DAY_MINUTE_LENGTH           = registerParameter(new DPIntegerSlider(loc("DayMinuteLength"), Category.GAME_MECHANICS, FieldSuffix.MINUTES, 5, 30, 5), 10);
        NIGHT_MINUTE_LENGTH         = registerParameter(new DPIntegerSlider(loc("NightMinuteLength"), Category.GAME_MECHANICS, FieldSuffix.MINUTES, 5, 30, 5), 10);
        WEATHER_GRACE_PERIOD        = registerParameter(new DPIntegerSlider(loc("WeatherGracePeriod"), Category.GAME_MECHANICS, FieldSuffix.DAYS, 0, 16, 1), 8);
        ANIMAL_SICKNESS_BEHAVIOR    = registerParameter(new DPIntegerEnum(loc("AnimalSicknessBehavior"), Category.GAME_MECHANICS, 2), 1);
        ENTITIES_DROP_LOOT_ALWAYS   = registerParameter(new DPBoolean(loc("EntitiesDropLootAlways"), Category.GAME_MECHANICS), false);
        CHICKEN_BREEDING_ITEM       = registerParameter(new DPIntegerEnum(loc("ChickenBreedingItem"), Category.GAME_MECHANICS, 2), 1);
        CROP_SICKNESS_BEHAVIOR      = registerParameter(new DPIntegerEnum(loc("CropSicknessBehavior"), Category.GAME_MECHANICS, 2), 1);

        EXTREME = DifficultyProvider.getBuilder(loc("extreme"))
                .withBase(DifficultyProvider.defaults)
                .build();

        LEGENDARY = DifficultyProvider.getBuilder(loc("legendary"))
                .withBase(DifficultyProvider.defaults)
                .withParam(MAXIMUM_HEALTH, 8)
                .withParam(MAXIMUM_HUNGER, 8)
                .withParam(LEVELS_NEEDED_FOR_STAT_UP, 7)
                .withParam(MOB_DAMAGE_FACTOR, 1.5F)
                .withParam(PLAYER_DAMAGE_FACTOR, .75F)
                .withParam(WEATHER_GRACE_PERIOD, 0)
                .withParam(NON_SOLID_LEAVES, true)
                .withParam(CLIMBABLE_VINES, false)
                .build();

        HARD = DifficultyProvider.getBuilder(loc("hard"))
                .withBase(DifficultyProvider.defaults)
                .withParam(MINIMUM_HEALTH, 5)
                .withParam(MINIMUM_HUNGER, 5)
                .withParam(MINING_FACTOR, 1.5F)
                .withParam(MOB_DAMAGE_FACTOR, .75F)
                .withParam(WEATHER_GRACE_PERIOD, 16)
                .withParam(ENTITIES_DROP_LOOT_ALWAYS, true)
                .withParam(CHICKEN_BREEDING_ITEM, 0)
                .withParam(CAN_DISTURB_GROUND, false)
                .build();
    }
}
