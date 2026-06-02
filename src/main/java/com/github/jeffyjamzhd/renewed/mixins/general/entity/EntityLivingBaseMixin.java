package com.github.jeffyjamzhd.renewed.mixins.general.entity;

import com.github.jeffyjamzhd.renewed.registry.RenewedSounds;
import moddedmite.xylose.bettergamesetting.util.Mth;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Entity;
import net.minecraft.EntityLivingBase;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityLivingBase.class)
@Environment(EnvType.CLIENT)
public abstract class EntityLivingBaseMixin extends Entity {
    public EntityLivingBaseMixin(World par1World) {
        super(par1World);
    }

    @Inject(method = "onDeathUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityLivingBase;setDead()V"))
    private void playDeathSound(CallbackInfo ci) {
        float pitch = (float) Mth.lerp(.25F, 2F, Math.sqrt((this.width * this.height) / 2.5F));
        this.worldObj.playSound(this.posX, this.posY, this.posZ, RenewedSounds.DEATH_POOF.toString(), .5F, pitch, false);
    }
}
