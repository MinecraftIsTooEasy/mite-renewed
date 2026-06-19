package com.github.jeffyjamzhd.renewed.mixins.difficulty;

import com.github.jeffyjamzhd.renewed.mixins.RenewedMixinConfig;

public class DifficultyMixinConfig extends RenewedMixinConfig {
    static {
        MIXIN_PREDICATES.add(s -> hasITE && s.contains("EntitySkeletonMixin"));
    }
}
