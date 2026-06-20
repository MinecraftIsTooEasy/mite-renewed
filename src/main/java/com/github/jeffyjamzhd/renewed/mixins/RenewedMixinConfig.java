package com.github.jeffyjamzhd.renewed.mixins;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

public class RenewedMixinConfig implements IMixinConfigPlugin {
    protected static final ArrayList<Predicate<String>> MIXIN_PREDICATES = new ArrayList<>();

    @Override
    public boolean shouldApplyMixin(String targetClass, String mixinClass) {
        for (Predicate<String> predicate : MIXIN_PREDICATES) {
            if (predicate.test(mixinClass)) {
                return false;
            }
        }

        return true;
    }

    // ======== UNUSED ===============================================

    @Override
    public void onLoad(String s) {
    }

    @Override
    public String getRefMapperConfig() {
        return "";
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
