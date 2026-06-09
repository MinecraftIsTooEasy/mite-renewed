package com.github.jeffyjamzhd.renewed.mixins.general.entity;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(EntityLiving.class)
public abstract class EntityLivingMixin extends EntityLivingBase {
    public EntityLivingMixin(World par1World) {
        super(par1World);
    }

    @WrapOperation(method = "tryDisableNearbyLightSource", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityLiving;playSound(Ljava/lang/String;FF)V"))
    private void setSoundToBreath(EntityLiving instance, String sound, float volume, float pitch, Operation<Void> original) {
        Entity self = this;
        if (self instanceof EntityShadow || self instanceof EntityInvisibleStalker) {
            original.call(instance, "random.breath", .5F,  1F + (this.rand.nextFloat() - this.rand.nextFloat()) * .2F);
            return;
        }
        original.call(instance, sound, volume, pitch);
    }
}
