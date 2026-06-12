package com.github.jeffyjamzhd.renewed.enchantment;

import com.github.jeffyjamzhd.renewed.item.ItemWithInventory;
import net.minecraft.CreativeTabs;
import net.minecraft.Enchantment;
import net.minecraft.EnumRarity;
import net.minecraft.Item;

public class EnchantmentHolding extends Enchantment {
    public EnchantmentHolding(int id) {
        super(id, EnumRarity.uncommon, 10);
    }

    @Override
    public int getNumLevels() {
        return 3;
    }

    @Override
    public String getNameSuffix() {
        return "holding";
    }

    @Override
    public boolean canEnchantItem(Item item) {
        return item instanceof ItemWithInventory;
    }

    @Override
    public boolean canApplyTogether(Enchantment enchantment) {
        return !(enchantment instanceof EnchantmentSoulbound) && super.canApplyTogether(enchantment);
    }

    @Override
    public boolean isOnCreativeTab(CreativeTabs creativeTabs) {
        return creativeTabs == CreativeTabs.tabTools;
    }
}
