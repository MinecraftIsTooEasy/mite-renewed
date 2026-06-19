package com.github.jeffyjamzhd.renewed.mixins.difficulty;

import com.github.jeffyjamzhd.renewed.mixins.RenewedMixinConfig;
import com.github.jeffyjamzhd.renewed.util.Compatibility;

public class DifficultyMixinConfig extends RenewedMixinConfig {
    static {
        MIXIN_PREDICATES.add(s -> Compatibility.ITE_LOADED && s.contains("EntitySkeletonMixin"));
    }
}
