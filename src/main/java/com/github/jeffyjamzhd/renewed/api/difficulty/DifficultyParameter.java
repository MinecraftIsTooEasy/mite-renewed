package com.github.jeffyjamzhd.renewed.api.difficulty;

import com.github.jeffyjamzhd.renewed.api.difficulty.gui.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;

public abstract class DifficultyParameter<T> {
    public Category category;
    public ResourceLocation id;
    public DifficultyParameterSanitizer<T> sanitizer = (difficulty, value) -> value;

    public DifficultyParameter(ResourceLocation id, Category category) {
        this.id = id;
        this.category = category;
    }

    public DifficultyParameter<T> withSanitizer(DifficultyParameterSanitizer<T> sanitizer) {
        this.sanitizer = sanitizer;
        return this;
    }

    abstract public void writeNBT(NBTTagCompound tag, T value);
    abstract public T readNBT(NBTTagCompound tag);

    @Environment(EnvType.CLIENT)
    abstract public IParameterField<T> getField(T value, Difficulty difficulty);

    abstract public T sanitizeValue(Difficulty difficulty, T value);

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

    public static void init() {
    }

    public static class DPIntegerEnum extends DifficultyParameter<Integer> {
        public final int range;

        public DPIntegerEnum(ResourceLocation id, Category category, int range) {
            super(id, category);
            this.range = range;
        }

        @Override
        public IParameterField<Integer> getField(Integer value, Difficulty difficulty) {
            String[] strings = new String[this.range];
            for (int i = 0; i < range; i++) {
                strings[i] = getNameKey() + ".%d".formatted(i);
            }
            return new GuiFieldEnumButton(strings, difficulty, this);
        }

        @Override
        public Integer sanitizeValue(Difficulty difficulty, Integer value) {
            int result = sanitizer.sanitize(difficulty, value);
            return result % range;
        }

        @Override
        public String getDescription(Integer value) {
            if (value == null) {
                return super.getDescription(null);
            }

            StringBuilder desc = new StringBuilder(super.getDescription(value));
            desc.append(' ');

            String elementDescKey = super.getDescriptionKey(value) + ".%d".formatted(value);
            String elementDesc = I18n.getString(elementDescKey);
            desc.append(elementDesc);

            return desc.toString();
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

    public static class DPBoolean extends DifficultyParameter<Boolean> {
        public DPBoolean(ResourceLocation id, Category category) {
            super(id, category);
        }

        @Override
        public IParameterField<Boolean> getField(Boolean value, Difficulty difficulty) {
            return new GuiFieldBooleanButton(value, difficulty, this);
        }

        @Override
        public Boolean sanitizeValue(Difficulty difficulty, Boolean value) {
            return sanitizer.sanitize(difficulty, value);
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
        public IParameterField<Float> getField(Float value, Difficulty difficulty) {
            GuiFieldSlider<Float> slider = new GuiFieldSlider<>(value, this.step);
            slider.setRange(this.min, this.max);
            slider.setSuffix(this.suffix);
            slider.difficulty = difficulty;
            slider.parameter = this;
            return slider;
        }

        @Override
        public Float sanitizeValue(Difficulty difficulty, Float value) {
            float result = sanitizer.sanitize(difficulty, value);
            result = Math.min(Math.max(result, min), max);
            result = Math.round(result / this.step) * this.step;
            return result;
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
        public IParameterField<Integer> getField(Integer value, Difficulty difficulty) {
            GuiFieldSlider<Integer> slider = new GuiFieldSlider<>(value, this.step);
            slider.setRange(this.min, this.max);
            slider.setSuffix(this.suffix);
            slider.difficulty = difficulty;
            slider.parameter = this;
            return slider;
        }

        @Override
        public Integer sanitizeValue(Difficulty difficulty, Integer value) {
            int result = sanitizer.sanitize(difficulty, value);
            result = Math.min(Math.max(result, min), max);
            result = Math.round((float) result / this.step) * this.step;
            return result;
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
