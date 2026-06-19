package com.github.jeffyjamzhd.renewed.mixins.general;

import com.bawnorton.mixinsquared.MixinSquaredBootstrap;
import com.github.jeffyjamzhd.renewed.mixins.RenewedMixinConfig;
import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import moddedmite.xylose.bettergamesetting.util.BGSConfig;
import net.xiaoyu233.fml.FishModLoader;

public class GeneralMixinConfig extends RenewedMixinConfig {
    @Override
    public void onLoad(String s) {
        MixinSquaredBootstrap.init();
        MixinExtrasBootstrap.init();
    }

    static {
        MIXIN_PREDICATES.add(s -> !hasModMenu && s.contains("GuiMainMenuMMMixin"));
        MIXIN_PREDICATES.add(s -> hasBGS && s.contains("GuiCreateWorldBGSMixin") && !BGSConfig.useModernCreateWorldGui.get());
        MIXIN_PREDICATES.add(s -> !hasBGS && s.contains("GuiCreateWorldBGSMixin"));
        MIXIN_PREDICATES.add(s -> hasBGS && s.contains("GuiCreateWorldMixin") && BGSConfig.useModernCreateWorldGui.get());
    }
}
