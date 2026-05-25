package com.github.jeffyjamzhd.renewed.api.difficulty;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import net.minecraft.*;

import java.util.HashMap;
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
        NBTTagList list = tag.getTagList("Parameters");
        for (int i = 0; i < list.tagCount(); i++) {
            NBTBase tagAt = list.tagAt(i);
            ResourceLocation parameterID = new ResourceLocation(tagAt.getName());
            DifficultyParameter<?> parameter = DifficultyProvider.identifierToParam.get(parameterID);
            params.put(parameter, parameter.readNBT((NBTTagCompound) tagAt));
        }
        return new Difficulty(CUSTOM, params);
    }

    public NBTTagCompound asTagCompound() {
        NBTTagCompound difficultyTag = new NBTTagCompound();
        NBTTagList parameterTagList = new NBTTagList();

        difficultyTag.setString("Preset", id.toString());
        difficultyTag.setTag("Parameters", parameterTagList);

        for (DifficultyParameter<?> parameter : DifficultyProvider.identifierToParam.values()) {
            NBTTagCompound parameterTag = new NBTTagCompound();
            parameter.writeNBT(parameterTag, getParamValue(parameter.id));
            parameterTagList.appendTag(parameterTag);
        }

        return difficultyTag;
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
}
