package com.github.jeffyjamzhd.renewed.api.event;

import com.github.jeffyjamzhd.renewed.api.event.listener.MusicConditionRegisterListener;
import com.github.jeffyjamzhd.renewed.api.registry.MusicConditionRegistry;

import java.util.ArrayList;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.LOGGER;

public class MusicConditionRegisterEvent {
    private static final ArrayList<MusicConditionRegisterListener> listeners = new ArrayList<>();

    public static void register(MusicConditionRegisterListener listener) {
        LOGGER.info("Registering music conditions in {}", listener.getClass().getSimpleName());
        listeners.add(listener);
    }

    public static void init() {
        for (MusicConditionRegisterListener listener : listeners) {
            listener.register(MusicConditionRegistry.INSTANCE);
        }
    }
}
