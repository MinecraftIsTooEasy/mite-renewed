package com.github.jeffyjamzhd.renewed.enchantment;

import com.github.jeffyjamzhd.renewed.item.ItemWithInventory;
import net.minecraft.CreativeTabs;
import net.minecraft.Enchantment;
import net.minecraft.EnumRarity;
import net.minecraft.Item;

public class EnchantmentFilling extends Enchantment {
    public EnchantmentFilling(int id) {
        super(id, EnumRarity.rare, 10);
    }

    @Override
    public int getNumLevels() {
        return 1;
    }

    @Override
    public String getNameSuffix() {
        return "filling";
    }

    @Override
    public boolean canEnchantItem(Item item) {
        return item instanceof ItemWithInventory;
    }

    @Override
    public boolean isOnCreativeTab(CreativeTabs creativeTabs) {
        return creativeTabs == CreativeTabs.tabTools;
    }
}
