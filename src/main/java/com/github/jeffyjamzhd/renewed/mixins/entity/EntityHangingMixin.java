package com.github.jeffyjamzhd.renewed.mixins.entity;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.Block;
import net.minecraft.BlockChest;
import net.minecraft.EntityHanging;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityHanging.class)
public class EntityHangingMixin {
    @Inject(method = "onValidSurface", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/Block;isFaceFlatAndSolid(ILnet/minecraft/EnumFace;)Z"), cancellable = true)
    private void checkForChest(CallbackInfoReturnable<Boolean> cir, @Local(ordinal = 0) Block block) {
        if (block instanceof BlockChest)
            cir.setReturnValue(true);
    }
}
