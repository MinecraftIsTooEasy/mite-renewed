package com.github.jeffyjamzhd.renewed.mixins.block;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockFurnaceSandstone.class)
abstract class BlockFurnaceSandstoneMixin extends BlockFurnace {
    protected BlockFurnaceSandstoneMixin(int par1, Material material, boolean par2) {
        super(par1, material, par2);
    }

    @ModifyReturnValue(method = "getMaxHeatLevel", at = @At("RETURN"))
    public int getMaxHeatLevelBuff(int original) {
        return Math.max(original, 2);
    }

    @ModifyReturnValue(method = "isOven", at = @At("RETURN"))
    public boolean isOvenBuff(boolean original) {
        return false;
    }

    @Override
    public String getUnlocalizedName() {
        return "tile.sandstoneFurnace";
    }
}
