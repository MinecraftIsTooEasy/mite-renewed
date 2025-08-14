package com.github.jeffyjamzhd.renewed.item;

import net.minecraft.CreativeTabs;
import net.minecraft.Item;
import net.minecraft.ItemStack;
import net.minecraft.Material;

public class ItemBiomass extends Item {
    public ItemBiomass(int id) {
        super(id, Material.plants, "biomass");
        this.setMaxStackSize(8);
        this.setCraftingDifficultyAsComponent(250F);
        this.setCreativeTab(CreativeTabs.tabMaterials);
    }

    @Override
    public boolean canBurnAsFuelSource() {
        return true;
    }

    @Override
    public int getBurnTime(ItemStack item_stack) {
        return 100;
    }

    @Override
    public int getHeatLevel(ItemStack item_stack) {
        return 1;
    }
}
