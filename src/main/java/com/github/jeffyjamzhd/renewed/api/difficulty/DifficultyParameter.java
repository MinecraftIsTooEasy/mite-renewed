package com.github.jeffyjamzhd.renewed.api.difficulty;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.render.gui.GuiStepSlider;
import net.minecraft.*;

public abstract class DifficultyParameter<T> {
    public ResourceLocation id;

    public DifficultyParameter(ResourceLocation id) {
        this.id = id;
    }

    abstract public void writeNBT(NBTTagCompound tag, T value);
    abstract public T readNBT(NBTTagCompound tag);

    abstract public T sanitizeValue(T value);

    abstract public GuiButton getFieldButton(T value, int id, int x, int y);
    abstract public String getValueString(T value);

    public String getNameKey() {
        return "difficulty.parameter.%s.name".formatted(id.getResourcePath());
    }

    public String getName() {
        return I18n.getString(getNameKey());
    }

    public String getDescriptionKey() {
        return "difficulty.parameter.%s.desc".formatted(id.getResourcePath());
    }

    public String getDescription() {
        return I18n.getString(getDescriptionKey());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    /*
            Player interaction
     */

//    public static class PlayerDamageFactor extends DifficultyParameter<Float> {
//    }
//    public static class MobDamageFactor extends DifficultyParameter<Float> {}
//    public static class BlockHarvestMultiplier extends DifficultyParameter<Float> {}
//    public static class LevelsPerPip extends DifficultyParameter<Integer> {}
//    public static class PlayerMinimumHearts extends DifficultyParameter<Integer> {}
//    public static class PlayerMaximumHearts extends DifficultyParameter<Integer> {}
//    public static class PlayerMinimumHunger extends DifficultyParameter<Integer> {}
//    public static class PlayerMaximumHunger extends DifficultyParameter<Integer> {}
//
//    static {
//        DifficultyProvider.setDefaultForParameter(PlayerDamageFactor.class, 1F);
//        DifficultyProvider.setDefaultForParameter(MobDamageFactor.class, 1F);
//        DifficultyProvider.setDefaultForParameter(BlockHarvestMultiplier.class, 1F);
//        DifficultyProvider.setDefaultForParameter(LevelsPerPip.class, 5);
//        DifficultyProvider.setDefaultForParameter(PlayerMinimumHearts.class, 3);
//        DifficultyProvider.setDefaultForParameter(PlayerMaximumHearts.class, 10);
//        DifficultyProvider.setDefaultForParameter(PlayerMinimumHunger.class, 3);
//        DifficultyProvider.setDefaultForParameter(PlayerMaximumHunger.class, 10);
//    }

    /*
            World
     */

//    public static class DayMinuteLength extends DifficultyParameter<Integer> {}
//    public static class NightMinuteLength extends DifficultyParameter<Integer> {}
//    public static class PassThroughLeaves extends DifficultyParameter<Boolean> {}
//    public static class ClimbableVines extends DifficultyParameter<Boolean> {}
//    public static class DirtHasGravity extends DifficultyParameter<Boolean> {}
//    public static class AnimalSickness extends DifficultyParameter<Boolean> {}
//    public static class CropSickness extends DifficultyParameter<Boolean> {}
//    public static class SensitiveGravityBlocks extends DifficultyParameter<Boolean> {}
//    public static class CropGrowthBaseChance extends DifficultyParameter<Float> {}
//
//    static {
//        DifficultyProvider.setDefaultForParameter(PassThroughLeaves.class, false);
//        DifficultyProvider.setDefaultForParameter(ClimbableVines.class, true);
//        DifficultyProvider.setDefaultForParameter(DirtHasGravity.class, true);
//        DifficultyProvider.setDefaultForParameter(SensitiveGravityBlocks.class, true);
//        DifficultyProvider.setDefaultForParameter(AnimalSickness.class, true);
//        DifficultyProvider.setDefaultForParameter(CropSickness.class, true);
//        DifficultyProvider.setDefaultForParameter(CropGrowthBaseChance.class, 0F);
//        DifficultyProvider.setDefaultForParameter(DayMinuteLength.class, 10);
//        DifficultyProvider.setDefaultForParameter(NightMinuteLength.class, 10);
//    }

    public static void init() {
    }

    public static class DPInteger extends DifficultyParameter<Integer> {
        public final int minimum;
        public final int maximum;

        public DPInteger(ResourceLocation id, int min, int max) {
            super(id);
            this.minimum = min;
            this.maximum = max;
        }

        @Override
        public Integer sanitizeValue(Integer value) {
            return Math.min(Math.max(value, minimum), maximum);
        }

        @Override
        public GuiButton getFieldButton(Integer value, int id, int x, int y) {
            return new GuiButton(id, x, y, 100, 20, getValueString(value));
        }

        @Override
        public String getValueString(Integer value) {
            return "%d".formatted(value);
        }

        @Override
        public void writeNBT(NBTTagCompound tag, Integer value) {
            tag.setInteger(id.toString(), value);
        }

        @Override
        public Integer readNBT(NBTTagCompound tag) {
            return tag.getInteger(id.toString());
        }
    }

    public static class DPIntegerEnum extends DifficultyParameter<Integer> {
        public final int range;

        public DPIntegerEnum(ResourceLocation id, int range) {
            super(id);
            this.range = range - 1;
        }

        @Override
        public GuiButton getFieldButton(Integer value, int id, int x, int y) {
            return new GuiButton(id, x, y, 100, 20, getValueString(value));
        }

        @Override
        public Integer sanitizeValue(Integer value) {
            return Math.min(Math.max(0, value), range);
        }

        @Override
        public String getValueString(Integer value) {
            return super.getName() + ".%d".formatted(value);
        }

        @Override
        public String getDescription() {
            StringBuilder desc = new StringBuilder(super.getDescription() + "\n\n");
            for (int i = 0; i <= range; i++) {
                String elementDescKey = super.getDescriptionKey() + ".%d".formatted(i);
                String elementDesc = I18n.getString(elementDescKey);
                String elementNameKey = super.getNameKey() + ".%d".formatted(i);
                String elementName = I18n.getString(elementNameKey);

                desc.append("%s - %s".formatted(elementName, elementDesc));
            }
            return desc.toString();
        }

        @Override
        public void writeNBT(NBTTagCompound tag, Integer value) {

        }

        @Override
        public Integer readNBT(NBTTagCompound tag) {
            return 0;
        }
    }

    public static class DPBoolean extends DifficultyParameter<Boolean> {
        public DPBoolean(ResourceLocation id) {
            super(id);
        }

        @Override
        public GuiButton getFieldButton(Boolean value, int id, int x, int y) {
            return new GuiButton(id, x, y, 100, 20, getValueString(value));
        }

        @Override
        public Boolean sanitizeValue(Boolean value) {
            return value;
        }

        @Override
        public String getValueString(Boolean value) {
            return I18n.getString(value ? "difficulty.enabled" : "difficulty.disabled");
        }

        @Override
        public void writeNBT(NBTTagCompound tag, Boolean value) {
            tag.setBoolean(id.toString(), value);
        }

        @Override
        public Boolean readNBT(NBTTagCompound tag) {
            return tag.getBoolean(id.toString());
        }
    }

    public static class DPFloatPercent extends DifficultyParameter<Float> {
        final public float min;
        final public float max;
        final public float step;

        public DPFloatPercent(ResourceLocation id, float min, float max) {
            this(id, min, max, 0.5F);
        }

        public DPFloatPercent(ResourceLocation id, float min, float max, float step) {
            super(id);
            this.min = min;
            this.max = max;
            this.step = step;
        }

        @Override
        public GuiButton getFieldButton(Float value, int id, int x, int y) {
            return new GuiStepSlider(id, x, y, value, this.step);
        }

        @Override
        public Float sanitizeValue(Float value) {
            return 0f;
        }

        @Override
        public String getValueString(Float value) {
            return "";
        }

        @Override
        public void writeNBT(NBTTagCompound tag, Float value) {

        }

        @Override
        public Float readNBT(NBTTagCompound tag) {
            return 0f;
        }
    }

}
