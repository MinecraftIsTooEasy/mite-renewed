package com.github.jeffyjamzhd.renewed.api.difficulty;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import net.minecraft.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

public class Difficulty {
    private static final ResourceLocation CUSTOM = new ResourceLocation(MiTERenewed.RESOURCE_ID + "custom");

    public final ResourceLocation id;
    private final HashMap<DifficultyParameter<?>, Object> params;

    public Difficulty(ResourceLocation id, HashMap<DifficultyParameter<?>, Object> params) {
        this.id = id;
        this.params = params;
    }

    /**
     * @param tag Tag compound to read from
     * @return Either an equivalent preset {@link Difficulty} object or a newly created one
     */
    public static Difficulty createFromTagCompound(NBTTagCompound tag) {
        // First, we check if we're pulling from a preset
        if (tag.hasKey("Preset")) {
            String presetID = tag.getString("Preset");
            ResourceLocation presetResource = new ResourceLocation(presetID);

            for (Difficulty difficulty : RenewedDifficulties.LIST) {
                if (difficulty.id.equals(presetResource)) return difficulty;
            }
        }

        // If we're not, we will construct the custom difficulty from
        // saved parameters
        HashMap<DifficultyParameter<?>, Object> params = new HashMap<>(DifficultyProvider.defaults);
        NBTTagCompound nbtParameters = tag.getCompoundTag("Parameters");
        for (DifficultyParameter<?> parameter : DifficultyProvider.orderedList) {
            if (nbtParameters.hasKey(parameter.id.toString())) {
                params.put(parameter, parameter.readNBT(nbtParameters));
            } else {
                params.put(parameter, DifficultyProvider.defaults.get(parameter));
            }
        }
        return new Difficulty(CUSTOM, params);
    }

    public static int getIndiceForDifficulty(Difficulty difficulty) {
        for (int i = 0; i < RenewedDifficulties.LIST.size(); i++) {
            Difficulty at = RenewedDifficulties.LIST.get(i);
            if (difficulty.id.equals(at.id)) {
                return i;
            }
        }
        return -1;
    }

    public NBTTagCompound asTagCompound() {
        NBTTagCompound difficultyTag = new NBTTagCompound();
        NBTTagCompound parameterTag = new NBTTagCompound();

        difficultyTag.setString("Preset", id.toString());
        difficultyTag.setCompoundTag("Parameters", parameterTag);

        for (DifficultyParameter<?> parameter : DifficultyProvider.identifierToParam.values()) {
            parameter.writeNBT(parameterTag, getParamValue(parameter.id));
        }

        return difficultyTag;
    }

    @SuppressWarnings("unchecked")
    public Difficulty cloneAsCustom() {
        return new Difficulty(CUSTOM, (HashMap<DifficultyParameter<?>, Object>) this.params.clone());
    }

    public static Optional<Difficulty> getFromWorld(World world) {
        Difficulty difficulty = world.mr$getDifficulty();
        return Optional.ofNullable(difficulty);
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
        return (T) getParamValue(parameter);
    }

    public <T> T getParamValue(DifficultyParameter<T> parameter) {
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

    public <T> void setParamValue(DifficultyParameter<T> parameter, T value) {
        if (!this.params.containsKey(parameter)) {
            throw new IllegalArgumentException("Difficulty parameter " + parameter.toString() + " does not exist!");
        }

        this.params.put(parameter, value);
    }
}
