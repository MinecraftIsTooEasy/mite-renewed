package com.github.jeffyjamzhd.renewed.api.difficulty;

import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import net.minecraft.I18n;
import net.minecraft.ResourceLocation;

import java.util.HashMap;
import java.util.Optional;

public class Difficulty {
    public final ResourceLocation id;
    public final int index;
    private final HashMap<DifficultyParameter<?>, Object> params;

    public Difficulty(ResourceLocation id, HashMap<DifficultyParameter<?>, Object> params) {
        this.id = id;
        this.params = params;
        this.index = RenewedDifficulties.LIST.size();
        RenewedDifficulties.LIST.add(this);
    }

    public String getLocalizedName() {
        return I18n.getString("difficulty.%s.name".formatted(this.getTranslationKey()));
    }

    public String getLocalizedDescription() {
        return I18n.getString("difficulty.%s.desc".formatted(this.getTranslationKey()));
    }

    public String getTranslationKey() {
        return this.id.getResourceDomain() + "." + this.id.getResourcePath();
    }

    public <T> T getParamValue(ResourceLocation location) {
        DifficultyParameter<?> parameter = DifficultyProvider.getParameter(location);
        var val = Optional.ofNullable((T) this.params.get(parameter));
        return val.orElseGet(() -> (T) DifficultyProvider.defaults.get(parameter));
    }

    public <T> Difficulty addParam(DifficultyParameter<T> parameter, T value) {
        if (this.params.containsKey(parameter)) {
            throw new IllegalArgumentException("Difficulty parameter " + parameter.toString() + " already set!");
        }

        this.params.put(parameter, value);
        return this;
    }
}
