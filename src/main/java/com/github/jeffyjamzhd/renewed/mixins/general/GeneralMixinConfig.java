package com.github.jeffyjamzhd.renewed.mixins.general;

import com.bawnorton.mixinsquared.MixinSquaredBootstrap;
import com.github.jeffyjamzhd.renewed.mixins.RenewedMixinConfig;
import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import moddedmite.xylose.bettergamesetting.util.BGSConfig;

import static com.github.jeffyjamzhd.renewed.util.Compatibility.*;

public class GeneralMixinConfig extends RenewedMixinConfig {
    @Override
    public void onLoad(String s) {
        MixinSquaredBootstrap.init();
        MixinExtrasBootstrap.init();
    }

    static {
        MIXIN_PREDICATES.add(s -> !MOD_MENU_LOADED && s.contains("GuiMainMenuMMMixin"));
        MIXIN_PREDICATES.add(s -> BGS_LOADED && s.contains("GuiCreateWorldBGSMixin") && !BGSConfig.useModernCreateWorldGui.get());
        MIXIN_PREDICATES.add(s -> !BGS_LOADED && s.contains("GuiCreateWorldBGSMixin"));
        MIXIN_PREDICATES.add(s -> BGS_LOADED && s.contains("GuiCreateWorldMixin") && BGSConfig.useModernCreateWorldGui.get());
        MIXIN_PREDICATES.add(s -> !OFFHAND_LOADED && s.contains("GuiIngameOffhandMixin"));
    }
}
