package com.github.jeffyjamzhd.renewed.mixins.block;

import com.github.jeffyjamzhd.renewed.registry.RenewedItems;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockWeb.class)
public abstract class BlockWebMixin extends Block {
    protected BlockWebMixin(int par1, Material par2Material, BlockConstants constants) {
        super(par1, par2Material, constants);
    }

    @Inject(method = "dropBlockAsEntityItem", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/BlockBreakInfo;getHarvesterItem()Lnet/minecraft/Item;"), cancellable = true)
    void addTangledWeb(BlockBreakInfo info, CallbackInfoReturnable<Integer> cir, @Local Item item) {
        if (!(item instanceof ItemSword || item instanceof ItemShears))
            cir.setReturnValue(super.dropBlockAsEntityItem(info, RenewedItems.tangled_web));
    }
}
