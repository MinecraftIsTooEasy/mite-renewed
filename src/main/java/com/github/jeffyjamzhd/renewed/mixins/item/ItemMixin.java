package com.github.jeffyjamzhd.renewed.mixins.item;

import com.github.jeffyjamzhd.renewed.api.IItem;
import com.github.jeffyjamzhd.renewed.item.ItemRenewedBucket;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin implements IItem {
    @Shadow @Final public int itemID;
    @Shadow private int maxStackSize;

    @Inject(method = "getBurnTime", at = @At("HEAD"), cancellable = true)
    private void getBurnTimeRenewed(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (this.itemID == Item.feather.itemID)
            cir.setReturnValue(50);
    }

    @ModifyArg(method = "onItemUseFinish", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/EntityPlayer;convertOneOfHeldItem(Lnet/minecraft/ItemStack;)V"))
    private ItemStack carryOverDamage(ItemStack newStack, @Local(argsOnly = true) ItemStack oldStack) {
        if (oldStack != null && newStack != null) {
            newStack.setItemDamage(oldStack.getItemDamage());
        }
        return newStack;
    }

    @Redirect(method = "<clinit>", at = @At(value = "NEW", target = "net/minecraft/ItemBucket"))
    private static ItemBucket replaceAllBuckets(int id, Material material, Material contents) {
        return new ItemRenewedBucket(id, material, contents);
    }

    @Override
    public int mr$getMaxStackSize() {
        return this.maxStackSize;
    }
}
