package com.github.jeffyjamzhd.renewed.api.event;

import com.github.jeffyjamzhd.renewed.api.event.listener.HandpanRegisterListener;
import com.github.jeffyjamzhd.renewed.item.recipe.HandpanRecipeProcessor;

import java.util.ArrayList;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.LOGGER;

public class HandpanRegisterEvent {
    private static final ArrayList<HandpanRegisterListener> listeners = new ArrayList<>();

    public static void register(HandpanRegisterListener listener) {
        LOGGER.info("Registering handpan recipes in {}", listener.getClass().getSimpleName());
        listeners.add(listener);
    }

    public static void init() {
        for (HandpanRegisterListener listener : listeners) {
            listener.register(HandpanRecipeProcessor.INSTANCE);
        }
    }
}
