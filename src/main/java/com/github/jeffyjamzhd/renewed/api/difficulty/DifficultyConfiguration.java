package com.github.jeffyjamzhd.renewed.api.difficulty;

import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import net.minecraft.I18n;
import net.minecraft.ResourceLocation;

import java.util.HashMap;
import java.util.Optional;

public class DifficultyConfiguration {
    public final ResourceLocation id;
    public final int index;
    private final HashMap<Class<? extends DifficultyParameter<?>>, Object> params;

    public DifficultyConfiguration(ResourceLocation id, HashMap<Class<? extends DifficultyParameter<?>>, Object> params) {
        this.id = id;
        this.params = params;
        this.index = RenewedDifficulties.LIST.size();
        RenewedDifficulties.LIST.add(this);
    }

    public String getLocalizedName() {
        return I18n.getString("difficulty." + this.getTranslationKey() + ".name");
    }

    public String getTranslationKey() {
        return this.id.getResourceDomain() + "." + this.id.getResourcePath();
    }

    public <T> T getParamValue(Class<? extends DifficultyParameter<T>> param) {
        var val = Optional.ofNullable((T) this.params.get(param));
        return val.orElseGet(() -> (T) DifficultyProvider.defaults.get(param));
    }

    public <T> DifficultyConfiguration addParam(Class<? extends DifficultyParameter<T>> param, T value) {
        if (this.params.containsKey(param)) {
            throw new IllegalArgumentException("Difficulty parameter " + param.toString() + " already set!");
        }

        this.params.put(param, value);
        return this;
    }
}
