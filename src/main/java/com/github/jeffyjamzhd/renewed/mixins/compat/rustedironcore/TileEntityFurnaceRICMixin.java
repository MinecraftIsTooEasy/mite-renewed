package com.github.jeffyjamzhd.renewed.mixins.compat.rustedironcore;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.Item;
import net.minecraft.ItemStack;
import net.minecraft.TileEntity;
import net.minecraft.TileEntityFurnace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = TileEntityFurnace.class, priority = 2000)
abstract class TileEntityFurnaceRICMixin extends TileEntity {
    @Shadow private ItemStack[] furnaceItemStacks;

    @TargetHandler(
            mixin = "moddedmite.rustedironcore.mixin.tileEntity.TileEntityFurnace",
            name = "dropBlockAsEntityItem"
    )
    @Redirect(method = "updateEntity", at = @At(value = "NEW", target = "net/minecraft/ItemStack", ordinal = 0))
    private ItemStack setDamage(Item item) {
        ItemStack furnaceItem = this.furnaceItemStacks[1];
        if (furnaceItem != null) {
            return new ItemStack(item).setItemDamage(furnaceItem.getItemDamage());
        }
        return new ItemStack(item);
    }
}
