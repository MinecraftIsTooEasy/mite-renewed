package com.github.jeffyjamzhd.renewed.enchantment;

import com.github.jeffyjamzhd.renewed.item.ItemWithInventory;
import net.minecraft.CreativeTabs;
import net.minecraft.Enchantment;
import net.minecraft.EnumRarity;
import net.minecraft.Item;

public class EnchantmentVacuum extends Enchantment {
    public EnchantmentVacuum(int id) {
        super(id, EnumRarity.uncommon, 10);
    }

    @Override
    public int getNumLevels() {
        return 3;
    }

    @Override
    public String getNameSuffix() {
        return "vacuum";
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
