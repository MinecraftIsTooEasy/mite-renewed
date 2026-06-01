package com.github.jeffyjamzhd.renewed.mixins.difficulty.entity;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.*;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity {
    public EntityLivingBaseMixin(World par1World) {
        super(par1World);
    }

    @ModifyExpressionValue(method = "onDeath", at = @At(value = "FIELD", target = "Lnet/minecraft/EntityLivingBase;has_taken_massive_fall_damage:Z", opcode = Opcodes.GETFIELD))
    private boolean forceDropsOnFallDamage(boolean original) {
        Difficulty difficulty = Difficulty.getFromWorld(this.getWorld()).orElseThrow();
        boolean dropLoot = difficulty.getParamValue(RenewedDifficulties.ENTITIES_DROP_LOOT_ALWAYS);
        return !dropLoot && original;
    }

    @ModifyExpressionValue(method = "moveEntityWithHeading", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityLivingBase;isOnLadder()Z", ordinal = 1))
    private boolean blockVineClimbing(boolean original) {
        Difficulty difficulty = Difficulty.getFromWorld(this.getWorld()).orElseThrow();
        boolean shouldClimb = difficulty.getParamValue(RenewedDifficulties.CLIMBABLE_VINES);
        return shouldClimb && original;
    }
}
