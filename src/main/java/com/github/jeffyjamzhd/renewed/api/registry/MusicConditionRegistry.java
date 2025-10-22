package com.github.jeffyjamzhd.renewed.api.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.api.music.IMusicCondition;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.function.Consumer;

public class MusicConditionRegistry {
    public static MusicConditionRegistry INSTANCE = new MusicConditionRegistry();
    /**
     * Music condition registry
     */
    private static HashMap<String, Class<? extends IMusicCondition>> CONDITIONS = new HashMap<>();
    private static Consumer<Class<? extends IMusicCondition>> REGISTRY = MusicConditionRegistry::consume;

    public void register(Class<? extends IMusicCondition> condition) {
        REGISTRY.accept(condition);
    }

    public static ImmutableMap<String, Class<? extends IMusicCondition>> getConditions() {
        return ImmutableMap.<String, Class<? extends IMusicCondition>>builder()
                .putAll(CONDITIONS)
                .build();
    }

    private static void consume(Class<? extends IMusicCondition> condition) {
        try {
            // Create condition object and check
            IMusicCondition object = condition.getDeclaredConstructor().newInstance();
            String identifier = object.getIdentifier();
            parseIdentifier(identifier);

            // Do not register duplicates
            if (CONDITIONS.containsKey(identifier)) {
                throw new Exception("Condition already registered!");
            }

            // Put into hashmap
            CONDITIONS.put(identifier, condition);
        } catch (Exception e) {
            MiTERenewed.LOGGER.error(e.getMessage());
        }
    }

    /**
     * Checks if the identifier is valid
     */
    private static void parseIdentifier(String identifier) throws Exception {
        if (!identifier.contains(":"))
            throw new Exception("Identifier doesn't incorporate namespace!");

        String[] split = identifier.split(":");
        if (split.length != 2)
            throw new Exception("Unconventional identifier format, expected namespace and condition, got %d strings"
                    .formatted(split.length));
    }
}
