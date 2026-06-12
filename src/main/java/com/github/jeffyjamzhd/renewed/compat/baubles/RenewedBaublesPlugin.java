package com.github.jeffyjamzhd.renewed.compat.baubles;

import baubles.api.BaubleType;
import baubles.api.IBaublePlugin;
import com.github.jeffyjamzhd.renewed.item.ItemWithInventory;
import com.github.jeffyjamzhd.renewed.registry.RenewedEnchantments;
import net.minecraft.EntityLivingBase;
import net.minecraft.ItemStack;

public class RenewedBaublesPlugin implements IBaublePlugin {
    @Override
    public boolean canPutBaubleSlot(ItemStack itemStack, BaubleType type) {
        if (itemStack == null) return false;
        boolean isBackpack = itemStack.getItem() instanceof ItemWithInventory;
        boolean isBack = type == BaubleType.BACK;

        return isBackpack && isBack;
    }

    @Override
    public void onWornTick(ItemStack itemstack, EntityLivingBase player) {
    }

    @Override
    public void onEquipped(ItemStack itemstack, EntityLivingBase player) {
    }

    @Override
    public void onUnequipped(ItemStack itemstack, EntityLivingBase player) {
    }

    @Override
    public boolean dropBaubleOnDeath(ItemStack itemStack, EntityLivingBase player) {
        if (itemStack == null) return true;
        boolean isBackpack = itemStack.getItem() instanceof ItemWithInventory;
        boolean hasSoulBind = RenewedEnchantments.ENCHANTMENT_SOUL_BOUND.getLevel(itemStack) > 0;

        return !(isBackpack && hasSoulBind);
    }
}
