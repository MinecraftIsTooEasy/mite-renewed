package com.github.jeffyjamzhd.renewed.mixins.difficulty.entity;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityMob.class)
public abstract class EntityMobMixin extends EntityCreature {
    public EntityMobMixin(World par1World) {
        super(par1World);
    }

    @Inject(method = "attackEntityAsMob(Lnet/minecraft/Entity;)Lnet/minecraft/EntityDamageResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/EnchantmentHelper;getFireAspectModifier(Lnet/minecraft/EntityLivingBase;)I"))
    private void multiplyMobAttack(Entity target, CallbackInfoReturnable<EntityDamageResult> cir, @Local Damage damage) {
        Difficulty difficulty = Difficulty.getFromWorld(this.getWorld()).orElseThrow();
        damage.scaleAmount(difficulty.getParamValue(RenewedDifficulties.MOB_DAMAGE_FACTOR));
    }

    @Inject(method = "attackEntityAsMob(Lnet/minecraft/EntityLiving;Lnet/minecraft/Entity;)Lnet/minecraft/EntityDamageResult;", at = @At(value = "INVOKE", target = "Lnet/minecraft/EnchantmentHelper;getFireAspectModifier(Lnet/minecraft/EntityLivingBase;)I"))
    private static void multiplyMobAttack2(EntityLiving attacker, Entity target, CallbackInfoReturnable<EntityDamageResult> cir, @Local Damage damage) {
        Difficulty difficulty = Difficulty.getFromWorld(attacker.getWorld()).orElseThrow();
        damage.scaleAmount(difficulty.getParamValue(RenewedDifficulties.MOB_DAMAGE_FACTOR));
    }
}
