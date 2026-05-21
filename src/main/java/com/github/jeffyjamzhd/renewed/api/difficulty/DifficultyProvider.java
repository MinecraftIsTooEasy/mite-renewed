package com.github.jeffyjamzhd.renewed.api.difficulty;

import net.minecraft.ResourceLocation;

import java.util.HashMap;

public class DifficultyProvider {
    static final HashMap<Class<? extends DifficultyParameter<?>>, Object> defaults = new HashMap<>();

    public static <T> void setDefaultForParameter(Class<? extends DifficultyParameter<?>> param, T value) {
        if (defaults.containsKey(param)) {
            throw new IllegalArgumentException("Parameter %s already set.".formatted(param.toString()));
        }

        defaults.put(param, value);
    }

    public static ConfigurationBuilder getBuilder(ResourceLocation id) {
        if (defaults.isEmpty()) {
            throw new IllegalStateException("No default parameters set!");
        }

        return new ConfigurationBuilder(id);
    }

    public static class ConfigurationBuilder {
        private ResourceLocation id;
        private HashMap<Class<? extends DifficultyParameter<?>>, Object> params = new HashMap<>();

        private ConfigurationBuilder(ResourceLocation id) {
            this.id = id;
        }

        public ConfigurationBuilder withBase() {
            // Todo: fill this out
            return this;
        }

        public <T> ConfigurationBuilder withParam(Class<? extends DifficultyParameter<T>> param, T value) {
            params.put(param, value);
            return this;
        }
    }
}
