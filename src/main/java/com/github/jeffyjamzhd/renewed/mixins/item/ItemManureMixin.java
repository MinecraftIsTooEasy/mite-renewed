package com.github.jeffyjamzhd.renewed.mixins.item;

import net.minecraft.EnumParticle;
import net.minecraft.ItemManure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ItemManure.class)
public class ItemManureMixin {
    @ModifyArg(method = "particleEffect", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/World;spawnParticle(Lnet/minecraft/EnumParticle;DDDDDD)V"),
            index = 0)
    private static EnumParticle setParticle(EnumParticle enum_particle) {
        return EnumParticle.happyVillager;
    }
}
