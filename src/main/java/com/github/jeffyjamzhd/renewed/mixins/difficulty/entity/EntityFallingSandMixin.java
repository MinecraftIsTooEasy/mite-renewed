package com.github.jeffyjamzhd.renewed.mixins.difficulty.entity;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityFallingSand.class)
public abstract class EntityFallingSandMixin extends Entity {
    @Shadow
    public int blockID;

    public EntityFallingSandMixin(World par1World) {
        super(par1World);
    }

    @ModifyReturnValue(method = "canDislodgeOrCrushBlockAt", at = @At("RETURN"))
    private static boolean canBreakLeaves(boolean original, @Local(argsOnly = true) World world, @Local(ordinal = 1) Block block) {
        Difficulty difficulty = Difficulty.getFromWorld(world).orElseThrow();
        boolean canPassThrough = difficulty.getParamValue(RenewedDifficulties.NON_SOLID_LEAVES);
        if (block instanceof BlockLeavesBase) {
            return original || canPassThrough;
        }
        return original;
    }

    @WrapOperation(method = "checkForBlockOccupyingSameSpace", at = @At(value = "INVOKE", target = "Lnet/minecraft/World;setBlockToAir(III)Z"))
    private boolean fancyBreak(World world, int x, int y, int z, Operation<Boolean> original, @Local(ordinal = 0) Block block, @Local BlockBreakInfo info) {
        if (block != null) {
            world.blockFX(EnumBlockFX.valueOf("transform"), x, y, z, new SignalData().setInteger(block.blockID));
        }

        return original.call(world, x, y, z);
    }
}
