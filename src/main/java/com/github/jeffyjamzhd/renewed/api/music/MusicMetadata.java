package com.github.jeffyjamzhd.renewed.api.music;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.api.music.conditions.MCGeneric;
import com.github.jeffyjamzhd.renewed.api.registry.MusicConditionRegistry;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public class MusicMetadata {
    @SerializedName("title")
    private String title;
    @SerializedName("artist")
    private String artist;
    @SerializedName("prevent_pitching")
    private boolean preventPitching;
    @SerializedName("hide_display")
    private boolean hideDisplay;
    @SerializedName("partial")
    private boolean canBeCutoff;

    /**
     * Conditions for this music track to play
     */
    private transient ArrayList<IMusicCondition> conditions;

    public String getTitle() {
        return this.title != null ? this.title : "Unknown Track";
    }

    public String getArtist() {
        return this.artist != null ? this.artist : "Unknown Artist";
    }

    public boolean trackPreventsPitching() {
        return this.preventPitching;
    }

    public boolean canBeCutoff() {
        return this.canBeCutoff;
    }

    public boolean hidesDisplay() {
        return this.hideDisplay;
    }

    public ArrayList<IMusicCondition> getConditions() {
        return this.conditions != null ? this.conditions : getDefaultCondition();
    }

    private ArrayList<IMusicCondition> getDefaultCondition() {
        this.conditions = new ArrayList<>();
        this.conditions.add(new MCGeneric());
        return this.conditions;
    }

    public void supplyConditions(Optional<JsonObject> conditions) {
        // Initialize
        this.conditions = new ArrayList<>();

        // Parse conditions
        conditions.ifPresent(this::parseJsonConditions);

        // Add default if empty
        if (this.conditions.isEmpty()) {
            this.conditions.add(new MCGeneric());
        }
    }

    private void parseJsonConditions(JsonObject object) {
        // Begin parsing conditions
        ImmutableMap<String, Class<? extends IMusicCondition>> conditions = MusicConditionRegistry.getConditions();
        Map<String, JsonElement> json = object.asMap();

        json.forEach((key, element) ->  {
            try {
                if (!conditions.containsKey(key)) {
                    throw new Exception("Tried to use condition that isn't registered: %s".formatted(key));
                }

                Class<? extends IMusicCondition> condition = conditions.get(key);
                IMusicCondition parsed = new Gson().fromJson(element, condition);
                this.conditions.add(parsed);
            } catch (Exception e) {
                MiTERenewed.LOGGER.error(e.getMessage());
            }

        });
    }
}
