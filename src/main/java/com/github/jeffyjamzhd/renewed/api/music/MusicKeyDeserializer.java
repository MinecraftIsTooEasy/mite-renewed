package com.github.jeffyjamzhd.renewed.api.music;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.google.gson.*;
import net.minecraft.ResourceLocation;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MusicKeyDeserializer
        implements JsonDeserializer<Map<ResourceLocation, MusicMetadata>> {
    @Override
    public Map<ResourceLocation, MusicMetadata> deserialize(
            JsonElement json,
            Type typeOfT,
            JsonDeserializationContext context)
            throws JsonParseException
    {
        JsonObject object = json.getAsJsonObject();
        Map<ResourceLocation, MusicMetadata> result = new HashMap<>();

        // Enter each music entry
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            // Music entry key, and values
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            // Parse path from key, replicate data to MusicMetadata
            ResourceLocation location = fromPath(key);
            MusicMetadata data = context.deserialize(value, MusicMetadata.class);

            // Parse conditions, if any
            if (value.getAsJsonObject().has("conditions")) {
                JsonObject conditionObject = (JsonObject) value.getAsJsonObject().get("conditions");
                data.supplyConditions(conditionObject);
            }
            result.put(location, data);
        }

        return result;
    }

    private ResourceLocation fromPath(String path) {
        String[] split = path.split(":", 2);
        if (split.length < 2) {
            if (split.length == 1) {
                MiTERenewed.LOGGER.info("Music metadata key does not follow proper schema: {}", path);
                MiTERenewed.LOGGER.info("Assuming asset namespace as \"minecraft\"...");
                return new ResourceLocation(split[0]);
            } else {
                MiTERenewed.LOGGER.info("Music metadata key is blank or malformed");
            }
            return new ResourceLocation("");
        }
        return new ResourceLocation(split[0], split[1]);
    }
}
