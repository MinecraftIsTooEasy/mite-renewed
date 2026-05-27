package com.github.jeffyjamzhd.renewed.api.difficulty;

import com.github.jeffyjamzhd.renewed.api.difficulty.gui.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;

import java.util.Arrays;

public abstract class DifficultyParameter<T> {
    public Category category;
    public ResourceLocation id;

    public DifficultyParameter(ResourceLocation id, Category category) {
        this.id = id;
        this.category = category;
    }

    abstract public void writeNBT(NBTTagCompound tag, T value);
    abstract public T readNBT(NBTTagCompound tag);

    @Environment(EnvType.CLIENT)
    abstract public IParameterField<T> getField(T value, int id, int x, int y);

    abstract public T sanitizeValue(T value);

    public String getNameKey() {
        return "difficulty.parameter.%s.name".formatted(id.getResourcePath());
    }
    public String getName() {
        return I18n.getString(getNameKey());
    }
    public String getDescriptionKey(T value) {
        return "difficulty.parameter.%s.desc".formatted(id.getResourcePath());
    }
    public String getDescription(T value) {
        return I18n.getString(getDescriptionKey(value));
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

        public DPInteger(ResourceLocation id, Category category, int min, int max) {
            super(id, category);
            this.minimum = min;
            this.maximum = max;
        }

        @Override
        public Integer sanitizeValue(Integer value) {
            return Math.min(Math.max(value, minimum), maximum);
        }

        @Override
        public IParameterField<Integer> getField(Integer value, int id, int x, int y) {
            return new GuiFieldSlider<>(value, 1);
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

        public DPIntegerEnum(ResourceLocation id, Category category, int range) {
            super(id, category);
            this.range = range;
        }

        @Override
        public IParameterField<Integer> getField(Integer value, int id, int x, int y) {
            String[] strings = new String[this.range];
            for (int i = 0; i < range; i++) {
                strings[i] = getNameKey() + ".%d".formatted(i);
            }
            return new GuiFieldEnumButton(strings);
        }

        @Override
        public Integer sanitizeValue(Integer value) {
            return Math.min(Math.max(0, value), range);
        }

        @Override
        public String getDescription(Integer value) {
            StringBuilder desc = new StringBuilder(super.getDescription(value));
            desc.append(' ');

            String elementDescKey = super.getDescriptionKey(value) + ".%d".formatted(value);
            String elementDesc = I18n.getString(elementDescKey);
            desc.append(elementDesc);

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
        public DPBoolean(ResourceLocation id, Category category) {
            super(id, category);
        }

        @Override
        public IParameterField<Boolean> getField(Boolean value, int id, int x, int y) {
            return new GuiFieldBooleanButton(value);
        }

        @Override
        public Boolean sanitizeValue(Boolean value) {
            return value;
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

    public static class DPFloatSlider extends DifficultyParameter<Float> {
        public final float min;
        public final float max;
        public final float step;
        private FieldSuffix suffix;

        public DPFloatSlider(ResourceLocation id, Category category, float min, float max) {
            this(id, category, min, max, 0.5F);
        }

        public DPFloatSlider(ResourceLocation id, Category category, float min, float max, float step) {
            super(id, category);
            this.min = min;
            this.max = max;
            this.step = step;
        }

        public DPFloatSlider(ResourceLocation id, Category category, FieldSuffix suffix, float min, float max, float step) {
            this(id, category, min, max, step);
            this.suffix = suffix;
        }

        @Override
        public IParameterField<Float> getField(Float value, int id, int x, int y) {
            GuiFieldSlider<Float> slider = new GuiFieldSlider<>(value, this.step);
            slider.setRange(this.min, this.max);
            slider.setSuffix(this.suffix);
            return slider;
        }

        @Override
        public Float sanitizeValue(Float value) {
            return value;
        }

        @Override
        public void writeNBT(NBTTagCompound tag, Float value) {
            tag.setFloat(id.toString(), value);
        }

        @Override
        public Float readNBT(NBTTagCompound tag) {
            return tag.getFloat(id.toString());
        }
    }

    public static class DPIntegerSlider extends DifficultyParameter<Integer> {
        public final int min;
        public final int max;
        public final int step;
        private FieldSuffix suffix;

        public DPIntegerSlider(ResourceLocation id, Category category, int min, int max) {
            this(id, category, min, max, 1);
        }

        public DPIntegerSlider(ResourceLocation id, Category category, int min, int max, int step) {
            super(id, category);
            this.min = min;
            this.max = max;
            this.step = step;
        }

        public DPIntegerSlider(ResourceLocation id, Category category, FieldSuffix suffix, int min, int max, int step) {
            this(id, category, min, max, step);
            this.suffix = suffix;
        }

        @Override
        public IParameterField<Integer> getField(Integer value, int id, int x, int y) {
            GuiFieldSlider<Integer> slider = new GuiFieldSlider<>(value, this.step);
            slider.setRange(this.min, this.max);
            slider.setSuffix(this.suffix);
            return slider;
        }

        @Override
        public Integer sanitizeValue(Integer value) {
            return value;
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

    public enum Category {
        GENERAL("general"),
        INTERACTION("interaction"),
        GAME_MECHANICS("game_mechanics");

        private final String name;

        Category(String name) {
            this.name = "difficulty.category.%s".formatted(name);
        }

        public String getName() {
            return I18n.getString(this.name);
        }
    }
}
