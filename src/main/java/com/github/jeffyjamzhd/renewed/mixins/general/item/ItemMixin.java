package com.github.jeffyjamzhd.renewed.mixins.general.item;

import com.github.jeffyjamzhd.renewed.api.IItem;
import com.github.jeffyjamzhd.renewed.item.ItemRenewedBucket;
import com.github.jeffyjamzhd.renewed.registry.RenewedItems;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
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

import java.io.PrintStream;

@Mixin(Item.class)
public class ItemMixin implements IItem {
    @Shadow @Final public int itemID;

    @WrapOperation(method = "<init>(ILjava/lang/String;I)V", at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"))
    private void doNotPushErrorIfOverwriting(PrintStream instance, String x, Operation<Void> original) {
        if (RenewedItems.IS_OVERWRITING_VANILLA) {
            return;
        }
        original.call(instance, x);
    }

    @Inject(method = "getBurnTime", at = @At("HEAD"), cancellable = true)
    private void getBurnTimeRenewed(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (stack == null) return;

        if (stack.getItem() == Item.feather)
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
}
