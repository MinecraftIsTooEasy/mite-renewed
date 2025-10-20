package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.api.event.listener.MusicConditionRegisterListener;
import com.github.jeffyjamzhd.renewed.api.music.conditions.*;
import com.github.jeffyjamzhd.renewed.api.registry.MusicConditionRegistry;


public class RenewedMusicConditions implements MusicConditionRegisterListener {
    @Override
    public void register(MusicConditionRegistry registry) {
        registry.register(MCGeneric.class);
        registry.register(MCTitle.class);
        registry.register(MCDimension.class);

        registry.register(MCTime.class);
        registry.register(MCHeight.class);
        registry.register(MCHumidity.class);
        registry.register(MCEvent.class);
    }
}
