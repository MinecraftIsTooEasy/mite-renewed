package com.github.jeffyjamzhd.renewed.api.difficulty;

import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import net.minecraft.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DifficultyProvider {
    public static final HashMap<ResourceLocation, DifficultyParameter<?>> identifierToParam = new HashMap<>();
    public static final HashMap<DifficultyParameter<?>, Object> defaults = new HashMap<>();
    public static final ArrayList<DifficultyParameter<?>> orderedList = new ArrayList<>();

    /**
     * Registers provided difficulty parameter with a default value
     */
    public static <T> DifficultyParameter<T> registerParameter(DifficultyParameter<T> parameter, T defaultValue) {
        if (identifierToParam.containsKey(parameter.id)) {
            throw new IllegalArgumentException("Parameter %s has already been registered".formatted(parameter.toString()));
        }

        identifierToParam.put(parameter.id, parameter);
        defaults.put(parameter, defaultValue);
        orderedList.add(parameter);

        return parameter;
    }

    public static DifficultyParameter<?> getParameter(ResourceLocation location) {
        if (!identifierToParam.containsKey(location)) {
            throw new IllegalArgumentException("Parameter %s is not registered".formatted(location.toString()));
        }
        return identifierToParam.get(location);
    }

    public static List<DifficultyParameter<?>> getParametersForCategory(DifficultyParameter.Category category) {
        return orderedList.stream()
                .filter(difficultyParameter -> difficultyParameter.category == category)
                .toList();
    }

    public static ConfigurationBuilder getBuilder(ResourceLocation id) {
        return new ConfigurationBuilder(id);
    }

    public static class ConfigurationBuilder {
        private final ResourceLocation id;
        private HashMap<DifficultyParameter<?>, Object> params = new HashMap<>();

        private ConfigurationBuilder(ResourceLocation id) {
            this.id = id;
        }

        public ConfigurationBuilder withBase(HashMap<DifficultyParameter<?>, Object> base) {
            this.params.putAll(base);
            return this;
        }

        public <T> ConfigurationBuilder withParam(DifficultyParameter<?> param, T value) {
            if (!defaults.containsKey(param)) {
                throw new IllegalArgumentException("Parameter %s has not been registered".formatted(param.toString()));
            }

            params.put(param, value);
            return this;
        }

        public Difficulty build() {
            Difficulty difficulty = new Difficulty(this.id, this.params);
            RenewedDifficulties.LIST.add(difficulty);
            return difficulty;
        }

    }
}
