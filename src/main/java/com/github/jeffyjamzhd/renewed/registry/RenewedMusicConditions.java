package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.api.event.listener.MusicConditionRegisterListener;
import com.github.jeffyjamzhd.renewed.api.music.conditions.MCGeneric;
import com.github.jeffyjamzhd.renewed.api.music.conditions.MCTime;
import com.github.jeffyjamzhd.renewed.api.registry.MusicConditionRegistry;


public class RenewedMusicConditions implements MusicConditionRegisterListener {
    @Override
    public void register(MusicConditionRegistry registry) {
        registry.register(MCGeneric.class);
        registry.register(MCTime.class);
    }
}
