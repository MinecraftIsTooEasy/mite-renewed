package com.github.jeffyjamzhd.renewed.mixins.difficulty.entity;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow
    public World worldObj;

    @Shadow
    public abstract boolean isInsideOfMaterial(Material par1Material);

    @Shadow
    public double motionX;

    @Shadow
    public double motionY;

    @Shadow
    public double motionZ;

    @Inject(method = "pushOutOfBlocks", at = @At(value = "INVOKE", target = "Lnet/minecraft/World;isBlockFullSolidCube(III)Z", ordinal = 0), cancellable = true)
    private void allowEntitiesToPassIntoLeaves(CallbackInfoReturnable<Integer> cir, @Local(ordinal = 0) int x, @Local(ordinal = 1) int y, @Local(ordinal = 2) int z) {
        Difficulty difficulty = Difficulty.getFromWorld(this.worldObj).orElseThrow();
        Block blockAt = this.worldObj.getBlock(x, y, z);
        if (difficulty.getParamValue(RenewedDifficulties.NON_SOLID_LEAVES) && blockAt instanceof BlockLeavesBase) {
            cir.setReturnValue(0);
        }
    }
}
