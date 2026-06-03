package com.github.jeffyjamzhd.renewed.mixins.difficulty.entity;

import com.github.jeffyjamzhd.renewed.api.IEntityLivingBase;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.*;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.Block.soundGrassFootstep;

@Mixin(EntityLivingBase.class)
public abstract class EntityLivingBaseMixin extends Entity implements IEntityLivingBase {
    @Unique
    private boolean insideLeaves = false;

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
        int x = MathHelper.floor_double(this.posX);
        int y = MathHelper.floor_double(this.boundingBox.minY);
        int z = MathHelper.floor_double(this.posZ);
        int block = this.worldObj.getBlockId(x, y, z);

        if (block == Block.vine.blockID) {
            Difficulty difficulty = Difficulty.getFromWorld(this.getWorld()).orElseThrow();
            boolean shouldClimb = difficulty.getParamValue(RenewedDifficulties.CLIMBABLE_VINES);
            return shouldClimb && original;
        }
        return original;
    }

    @Inject(method = "onEntityUpdate", at = @At(value = "HEAD"))
    private void slowEntitiesInLeaves(CallbackInfo ci) {
        Difficulty difficulty = Difficulty.getFromWorld(this.worldObj).orElseThrow();
        boolean canPassThrough = difficulty.getParamValue(RenewedDifficulties.NON_SOLID_LEAVES);

        if (!canPassThrough) {
            return;
        }

        boolean hasMoved = this.posX != this.prevPosX || this.posY != this.prevPosY || this.posZ != this.prevPosZ;
        if (this.ticksExisted % 10 == 0 && this.insideLeaves && hasMoved) {
            World world = this.getWorld();
            this.playSound(soundGrassFootstep.getPlaceSound(), .2F + world.rand.nextFloat() * .2F, .4F + world.rand.nextFloat() * .2F);
        }

        this.insideLeaves = false;
    }

    @Override
    public void mr$setInsideLeaves(boolean value) {
        this.insideLeaves = value;
    }
}
