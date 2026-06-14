package com.github.jeffyjamzhd.renewed.mixins.general;

import com.bawnorton.mixinsquared.MixinSquaredBootstrap;
import com.llamalad7.mixinextras.MixinExtrasBootstrap;
import moddedmite.xylose.bettergamesetting.util.BGSConfig;
import net.xiaoyu233.fml.FishModLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinConfig implements IMixinConfigPlugin {
    @Override
    public void onLoad(String s) {
        MixinSquaredBootstrap.init();
        MixinExtrasBootstrap.init();
    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public boolean shouldApplyMixin(String targetClass, String mixinClass) {
        if (mixinClass.contains("GuiMainMenuMMMixin") && !FishModLoader.hasMod("modmenu")) return false;
        if (mixinClass.contains("GuiCreateWorldBGSMixin")) {
            if (FishModLoader.hasMod("better_game_setting")) return BGSConfig.useModernCreateWorldGui.get();
            else return false;
        }

        if (mixinClass.contains("GuiCreateWorldMixin")) {
            if (FishModLoader.hasMod("better_game_setting")) return !BGSConfig.useModernCreateWorldGui.get();
            else return true;
        }
        return true;
    }

    @Override
    public void acceptTargets(Set<String> set, Set<String> set1) {
    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
    }

    @Override
    public void postApply(String s, ClassNode classNode, String s1, IMixinInfo iMixinInfo) {
    }
}
