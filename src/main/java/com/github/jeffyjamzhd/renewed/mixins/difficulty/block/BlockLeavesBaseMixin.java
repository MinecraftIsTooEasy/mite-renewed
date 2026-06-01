package com.github.jeffyjamzhd.renewed.mixins.difficulty.block;

import com.github.jeffyjamzhd.renewed.api.IEntityLivingBase;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlockLeavesBase.class)
public abstract class BlockLeavesBaseMixin extends Block {
    protected BlockLeavesBaseMixin(int par1, Material par2Material, BlockConstants constants) {
        super(par1, par2Material, constants);
    }

    @ModifyReturnValue(method = "canCollideWithEntity", at = @At("RETURN"))
    private boolean modifyLeafCollision(boolean original, @Local(argsOnly = true) Entity entity) {
        return cantPassthrough(entity.getWorld()) && original;
    }

    @ModifyReturnValue(method = "canBePathedInto", at = @At("RETURN"))
    private boolean modifyLeafPathing(boolean original, @Local(argsOnly = true) World world) {
        return !cantPassthrough(world) || original;
    }

    @Override
    public Object getCollisionBounds(World world, int x, int y, int z, Entity entity) {
        if (!cantPassthrough(world)) {
            return null;
        }

        return super.getCollisionBounds(world, x, y, z, entity);
    }

    @Override
    public void onEntityCollidedWithBlock(World world, int x, int y, int z, Entity entity) {
        if (entity instanceof IProjectile || (world.isRemote && entity instanceof EntityFX)) {
            return;
        }

        float modifier = 0.5F;
        if (entity instanceof EntityLivingBase) {
            ((IEntityLivingBase)entity).mr$setInsideLeaves(true);
            if (!entity.onGround) {
                modifier = 1 - modifier / 8;
            }
        }

        entity.motionX *= modifier;
        entity.motionZ *= modifier;

        if (entity.fallDistance > 10F && !world.isRemote) {
            world.blockFX(EnumBlockFX.valueOf("transform"), x, y, z, new SignalData().setInteger(this.blockID));
            world.setBlockToAir(x, y, z);

            // Entity will lose slightly less velocity than normal
            entity.motionY *= 0.7F;
            entity.fallDistance *= 0.7F;
            return;
        }

        if (entity.motionY < 0F) {
            entity.motionY *= 0.5F;
        }

        entity.fallDistance = 0F;
    }

    @Unique
    private boolean cantPassthrough(World world) {
        Difficulty difficulty = Difficulty.getFromWorld(world).orElseThrow();
        return !difficulty.getParamValue(RenewedDifficulties.NON_SOLID_LEAVES);
    }
}
