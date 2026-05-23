package com.github.jeffyjamzhd.renewed.api.difficulty;

import net.minecraft.ResourceLocation;

import java.util.HashMap;

public class DifficultyProvider {
    public static final HashMap<ResourceLocation, DifficultyParameter<?>> identifierToParam = new HashMap<>();
    static final HashMap<DifficultyParameter<?>, Object> defaults = new HashMap<>();

    /**
     * Registers provided difficulty parameter with a default value
     */
    public static <T> DifficultyParameter<T> registerParameter(DifficultyParameter<T> parameter, T defaultValue) {
        if (identifierToParam.containsKey(parameter.id)) {
            throw new IllegalArgumentException("Parameter %s has already been registered".formatted(parameter.toString()));
        }

        identifierToParam.put(parameter.id, parameter);
        defaults.put(parameter, defaultValue);

        return parameter;
    }

    public static DifficultyParameter<?> getParameter(ResourceLocation location) {
        if (!identifierToParam.containsKey(location)) {
            throw new IllegalArgumentException("Parameter %s is not registered".formatted(location.toString()));
        }
        return identifierToParam.get(location);
    }

    public static ConfigurationBuilder getBuilder(ResourceLocation id) {
        if (defaults.isEmpty()) {
            throw new IllegalStateException("No default parameters set!");
        }

        return new ConfigurationBuilder(id);
    }

    public static class ConfigurationBuilder {
        private final ResourceLocation id;
        private final HashMap<ResourceLocation, Object> params = new HashMap<>();

        private ConfigurationBuilder(ResourceLocation id) {
            this.id = id;
        }

        public ConfigurationBuilder withBase() {
            // Todo: fill this out
            return this;
        }

        public <T> ConfigurationBuilder withParam(ResourceLocation paramID, T value) {
            if (!identifierToParam.containsKey(paramID)) {
                throw new IllegalArgumentException("Parameter %s has not been registered".formatted(paramID.toString()));
            }

            params.put(paramID, value);
            return this;
        }


    }
}
