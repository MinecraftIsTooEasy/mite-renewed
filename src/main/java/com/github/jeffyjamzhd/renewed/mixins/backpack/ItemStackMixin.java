package com.github.jeffyjamzhd.renewed.mixins.backpack;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.Item;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {
    @Shadow
    public abstract Item getItem();

    @ModifyReturnValue(method = "isRepairItem", at = @At("RETURN"))
    private boolean addLeatherAsRepair(boolean original) {
        return original || this.getItem() == Item.leather;
    }
}
