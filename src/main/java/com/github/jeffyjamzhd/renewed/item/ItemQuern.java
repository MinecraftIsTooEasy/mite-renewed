package com.github.jeffyjamzhd.renewed.item;

import net.minecraft.CreativeTabs;
import net.minecraft.IDamageableItem;
import net.minecraft.Item;
import net.minecraft.Material;

public class ItemQuern extends Item implements IDamageableItem {
    public ItemQuern(int id, String texture) {
        super(id, texture);
        this.setCraftingDifficultyAsComponent(400F);
        this.setMaxStackSize(1);
        this.setMaxDamage(400);
        this.setMaterial(Material.wood);
        this.setCreativeTab(CreativeTabs.tabTools);
    }

    @Override
    public boolean isTool() {
        return true;
    }

    @Override
    public int getNumComponentsForDurability() {
        return 1;
    }

    @Override
    public int getRepairCost() {
        return 1;
    }
}
