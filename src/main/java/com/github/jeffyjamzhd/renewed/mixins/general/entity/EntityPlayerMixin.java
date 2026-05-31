package com.github.jeffyjamzhd.renewed.mixins.general.entity;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.api.IEntityPlayer;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.item.ItemHandpan;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityLivingBase implements IEntityPlayer {
    public EntityPlayerMixin(World par1World) {
        super(par1World);
    }
    @Shadow public abstract void playSound(String par1Str, float par2, float par3);
    @Shadow protected int itemInUseCount;
    @Shadow public abstract void stopUsingItem();

    @Shadow protected FoodStats foodStats;

    @Shadow
    public abstract int getExperienceLevel();

    @Inject(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityPlayer;updateItemUse(Lnet/minecraft/ItemStack;I)V", shift = At.Shift.BY, by = 2))
    protected void addUpdateForHandpan(CallbackInfo ci, @Local(name = "var1") ItemStack stack) {
        if (stack.getItem() instanceof ItemHandpan) {
            // Particles
            if (this.itemInUseCount % 5 == 0) {
                short id = stack.getTagCompound().getShort("handpanContent");
                Vec3 var4 = this.worldObj.getWorldVec3Pool().getVecFromPool(((double)this.rand.nextFloat() - (double)0.5F) * 0.1, Math.random() * 0.1 + 0.1, (double)0.0F);
                var4.rotateAroundX(-this.rotationPitch * (float)Math.PI / 180.0F);
                var4.rotateAroundY(-this.rotationYaw * (float)Math.PI / 180.0F);
                Vec3 var5 = this.worldObj.getWorldVec3Pool().getVecFromPool(((double)this.rand.nextFloat() - (double)0.5F) * 0.3, (double)(-this.rand.nextFloat()) * 0.6 - 0.3, 0.6);
                var5.rotateAroundX(-this.rotationPitch * (float)Math.PI / 180.0F);
                var5.rotateAroundY(-this.rotationYaw * (float)Math.PI / 180.0F);
                var5 = var5.addVector(this.posX, this.getEyePosY(), this.posZ);
                this.worldObj.spawnParticleEx(EnumParticle.iconcrack, id, 0, var5.xCoord, var5.yCoord, var5.zCoord, var4.xCoord, var4.yCoord + 0.05, var4.zCoord);

                // Worth checking if in water still
                if (!this.worldObj.isRemote && !this.isInWater()) {
                    this.stopUsingItem();
                }
            }

            // Sound effects
            if (this.itemInUseCount % 12 == 0 && !this.worldObj.isRemote) {
                this.worldObj.playSoundAtEntity(this, MiTERenewed.RESOURCE_ID + "item.handpan.splash", 0.2F + this.rand.nextFloat() * 0.1F, (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F + .8F);
                this.entityFX(EnumEntityFX.splash);
            }
        }
    }

    @Inject(method = "getHealthLimit()F", at = @At("HEAD"), cancellable = true)
    private void getHealthLimit(CallbackInfoReturnable<Float> cir) {
        Difficulty difficulty = this.getWorld().mr$getDifficulty();
        if (difficulty == null) {
            return;
        }

        int experienceLevel = this.getExperienceLevel();
        int levelsPer = difficulty.getParamValue(RenewedDifficulties.LEVELS_NEEDED_FOR_STAT_UP);
        int minimum = difficulty.getParamValue(RenewedDifficulties.MINIMUM_HEALTH) * 2;
        int maximum = difficulty.getParamValue(RenewedDifficulties.MAXIMUM_HEALTH) * 2;

        float upper = Math.min(minimum + experienceLevel / levelsPer * 2, maximum);
        float value = Math.max(upper, minimum);
        cir.setReturnValue(value);
    }

    @ModifyReturnValue(method = "getDamageVsBlock", at = @At("RETURN"))
    public float miningSpeedFactor(float original) {
        Difficulty difficulty = this.getWorld().mr$getDifficulty();
        if (difficulty == null) {
            return original;
        }

        float factor = difficulty.getParamValue(RenewedDifficulties.MINING_FACTOR);
        return original * factor;
    }


    @ModifyConstant(method = "updateItemUse", constant = @Constant(intValue = 0, ordinal = 1))
    int modifyData(int constant, @Local(argsOnly = true) ItemStack stack) {
        return stack.getItemSubtype();
    }

    @Override
    public void mr$addFoodValueSubtype(Item item, int subtype) {
        this.foodStats.mr$addFoodValueSubtype(item, subtype);
    }
}
