package com.github.jeffyjamzhd.renewed.mixins.item;

import com.github.jeffyjamzhd.renewed.item.ItemRenewedBucket;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemBucketMilk.class)
public class ItemBucketMilkMixin implements IDamageableItem {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void setDamage(int id, Material material, CallbackInfo ci) {
        ((ItemBucketMilk)(Object)(this)).setMaxDamage(ItemRenewedBucket.getDurabilityForMaterial(material));
    }

    @ModifyArg(method = "onItemRightClick", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/EntityPlayer;convertOneOfHeldItem(Lnet/minecraft/ItemStack;)V"))
    private ItemStack carryDamageOver(ItemStack newStack, @Local(argsOnly = true) EntityPlayer player) {
        ItemStack heldStack = player.getHeldItemStack();
        if (heldStack != null && newStack != null) {
            newStack.setItemDamage(heldStack.getItemDamage());
        }
        return newStack;
    }

    @Override
    public int getNumComponentsForDurability() {
        return 0;
    }

    @Override
    public int getRepairCost() {
        return 0;
    }
}
