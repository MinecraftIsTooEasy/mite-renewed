package com.github.jeffyjamzhd.renewed.mixins.general.item;

import net.minecraft.Block;
import net.minecraft.Item;
import net.minecraft.ItemBlock;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemBlock.class)
public abstract class ItemBlockMixin extends Item {
    @Shadow
    public abstract Block getBlock();

    @ModifyConstant(method = "getBurnTime", constant = @Constant(intValue = 800, ordinal = 0))
    public int nerfTorchTime(int constant) {
        return 400;
    }

    @Inject(method = "getBurnTime", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemBlock;getBlock()Lnet/minecraft/Block;"), cancellable = true)
    private void nerfButton(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (stack == null) return;

        Block block = this.getBlock();
        if (block == Block.woodenButton) {
            cir.setReturnValue(100);
        }
    }
}
