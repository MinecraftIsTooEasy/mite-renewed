package com.github.jeffyjamzhd.renewed.item;

import net.minecraft.ItemKnife;
import net.minecraft.ItemStack;
import net.minecraft.Material;

public class ItemBoneKnife extends ItemKnife {
    protected ItemBoneKnife(int par1, Material material) {
        super(par1, material);
    }

    @Override
    public int getBurnTime(ItemStack item_stack) {
        return 0;
    }

    @Override
    public boolean canBurnAsFuelSource() {
        return false;
    }
}
