package com.github.jeffyjamzhd.renewed.mixins.compat.emi;

import dev.emi.emi.VanillaPlugin;
import net.minecraft.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(VanillaPlugin.class)
public class VanillaPluginMixin {
    @Redirect(method = "register", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/Item;isDamageable()Z",
            ordinal = 0))
    private boolean isRepairableInstead(Item instance) {
        return instance.isRepairable();
    }
}
