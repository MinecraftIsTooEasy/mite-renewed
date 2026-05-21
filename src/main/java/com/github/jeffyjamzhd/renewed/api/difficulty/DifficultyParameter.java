package com.github.jeffyjamzhd.renewed.api.difficulty;

public abstract class DifficultyParameter<T> {
    public String getTranslationKey() {
        return "difficulty.param.%s".formatted(toString());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    /*
            Player interaction
     */

    public static class PlayerDamageFactor extends DifficultyParameter<Float> {}
    public static class MobDamageFactor extends DifficultyParameter<Float> {}
    public static class BlockHarvestMultiplier extends DifficultyParameter<Float> {}
    public static class LevelsPerPip extends DifficultyParameter<Integer> {}
    public static class PlayerMinimumHearts extends DifficultyParameter<Integer> {}
    public static class PlayerMaximumHearts extends DifficultyParameter<Integer> {}
    public static class PlayerMinimumHunger extends DifficultyParameter<Integer> {}
    public static class PlayerMaximumHunger extends DifficultyParameter<Integer> {}

    static {
        DifficultyProvider.setDefaultForParameter(PlayerDamageFactor.class, 1F);
        DifficultyProvider.setDefaultForParameter(MobDamageFactor.class, 1F);
        DifficultyProvider.setDefaultForParameter(BlockHarvestMultiplier.class, 1F);
        DifficultyProvider.setDefaultForParameter(LevelsPerPip.class, 5);
        DifficultyProvider.setDefaultForParameter(PlayerMinimumHearts.class, 3);
        DifficultyProvider.setDefaultForParameter(PlayerMaximumHearts.class, 10);
        DifficultyProvider.setDefaultForParameter(PlayerMinimumHunger.class, 3);
        DifficultyProvider.setDefaultForParameter(PlayerMaximumHunger.class, 10);
    }

    /*
            World
     */

    public static class DayMinuteLength extends DifficultyParameter<Integer> {}
    public static class NightMinuteLength extends DifficultyParameter<Integer> {}
    public static class PassThroughLeaves extends DifficultyParameter<Boolean> {}
    public static class ClimbableVines extends DifficultyParameter<Boolean> {}
    public static class DirtHasGravity extends DifficultyParameter<Boolean> {}
    public static class AnimalSickness extends DifficultyParameter<Boolean> {}
    public static class CropSickness extends DifficultyParameter<Boolean> {}
    public static class SensitiveGravityBlocks extends DifficultyParameter<Boolean> {}
    public static class CropGrowthBaseChance extends DifficultyParameter<Float> {}

    static {
        DifficultyProvider.setDefaultForParameter(PassThroughLeaves.class, false);
        DifficultyProvider.setDefaultForParameter(ClimbableVines.class, true);
        DifficultyProvider.setDefaultForParameter(DirtHasGravity.class, true);
        DifficultyProvider.setDefaultForParameter(SensitiveGravityBlocks.class, true);
        DifficultyProvider.setDefaultForParameter(AnimalSickness.class, true);
        DifficultyProvider.setDefaultForParameter(CropSickness.class, true);
        DifficultyProvider.setDefaultForParameter(CropGrowthBaseChance.class, 0F);
        DifficultyProvider.setDefaultForParameter(DayMinuteLength.class, 10);
        DifficultyProvider.setDefaultForParameter(NightMinuteLength.class, 10);
    }

    public static void init() {
    }


}
