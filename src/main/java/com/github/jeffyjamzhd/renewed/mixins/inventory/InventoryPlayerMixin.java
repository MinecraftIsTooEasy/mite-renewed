package com.github.jeffyjamzhd.renewed.mixins.inventory;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.InventoryPlayer;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(InventoryPlayer.class)
public class InventoryPlayerMixin {
    @ModifyExpressionValue(method = "convertAllItemsInSlot", at = @At(
            value = "NEW",
            target = "net/minecraft/ItemStack",
            ordinal = 0))
    private ItemStack addDamage(ItemStack original, @Local(ordinal = 0) ItemStack oldStack) {
        return original.setItemDamage(oldStack.getItemDamage());
    }
}
