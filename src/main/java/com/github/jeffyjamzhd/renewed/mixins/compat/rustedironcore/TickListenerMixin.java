package com.github.jeffyjamzhd.renewed.mixins.compat.rustedironcore;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moddedmite.rustedironcore.internal.event.listeners.TickListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(TickListener.class)
public class TickListenerMixin {
    @ModifyExpressionValue(method = "onClientTick", at =
    @At(value = "INVOKE", target = "Ljava/lang/String;equals(Ljava/lang/Object;)Z", ordinal = 0))
    private boolean dontDoTitleChange(boolean original) {
        return false;
    }
}
