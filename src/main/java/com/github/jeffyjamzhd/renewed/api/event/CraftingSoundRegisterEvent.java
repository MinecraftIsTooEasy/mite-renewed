package com.github.jeffyjamzhd.renewed.api.event;

import com.github.jeffyjamzhd.renewed.api.event.listener.CraftingSoundRegisterListener;
import com.github.jeffyjamzhd.renewed.api.registry.CraftingSoundRegistry;

import java.util.ArrayList;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.LOGGER;

public class CraftingSoundRegisterEvent {
    private static final ArrayList<CraftingSoundRegisterListener> listeners = new ArrayList<>();

    public static void register(CraftingSoundRegisterListener listener) {
        LOGGER.info("Registering crafting sounds in {}", listener.getClass().getSimpleName());
        listeners.add(listener);
    }

    public static void init() {
        for (CraftingSoundRegisterListener listener : listeners) {
            listener.register(CraftingSoundRegistry.INSTANCE);
        }
    }
}
