package com.github.jeffyjamzhd.renewed.compat.modmenu;

import com.github.jeffyjamzhd.renewed.RenewedConfig;
import io.github.prospector.modmenu.api.ConfigScreenFactory;
import io.github.prospector.modmenu.api.ModMenuApi;

public class RenewedModMenu implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return RenewedConfig::getScreen;
    }
}
