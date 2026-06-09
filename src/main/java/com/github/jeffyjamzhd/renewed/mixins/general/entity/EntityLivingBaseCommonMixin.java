package com.github.jeffyjamzhd.renewed.mixins.general.entity;

import net.minecraft.Entity;
import net.minecraft.EntityInvisibleStalker;
import net.minecraft.EntityLivingBase;
import net.minecraft.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseCommonMixin extends Entity {
    @Shadow public int deathTime;

    public EntityLivingBaseCommonMixin(World par1World) {
        super(par1World);
    }

    @Inject(method = "onDeathUpdate", at = @At(value = "FIELD", target = "Lnet/minecraft/EntityLivingBase;deathTime:I", ordinal = 0, opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER))
    private void setInvisibleStalkerDead(CallbackInfo ci) {
        if ((Entity) this instanceof EntityInvisibleStalker) {
            this.deathTime = 20;
        }
    }
}
