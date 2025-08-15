package com.github.jeffyjamzhd.renewed.api.event;

import com.github.jeffyjamzhd.renewed.api.event.listener.TracklistRegisterListener;
import com.github.jeffyjamzhd.renewed.api.registry.TracklistRegistry;

import java.util.ArrayList;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.LOGGER;

public class TracklistRegisterEvent {
    private static final ArrayList<TracklistRegisterListener> listeners = new ArrayList<>();

    public static void register(TracklistRegisterListener listener) {
        LOGGER.info("Registering track names in {}", listener.getClass().getSimpleName());
        listeners.add(listener);
    }

    public static void init() {
        for (TracklistRegisterListener listener : listeners) {
            listener.register(TracklistRegistry.INSTANCE);
        }
    }
}
