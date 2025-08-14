package com.github.jeffyjamzhd.renewed.item;

import net.minecraft.*;

import java.util.List;

/**
 * Extension of chicken and its subtypes (cuttings, gizzards)
 */

public class ItemPoultry extends ItemMeat {
    public ItemPoultry(int id, float poisonChance, String texture) {
        super(id, 0, 0, false, false, texture);
        this.setCreativeTab(CreativeTabs.tabFood);
    }

    public int satiationFromSubtype(int sub) {
        return switch (sub) {
            case 1 -> 1;
            case 2 -> 2;
            default -> 3;
        };
    }

    public int nutritionFromSubtype(int sub) {
        return switch (sub) {
            case 1 -> 1;
            case 2 -> 2;
            default -> 3;
        };
    }

    @Override
    public void getSubItems(int id, CreativeTabs tabs, List list) {
        for (int sub = 0; sub < 3; sub++)
            list.add(new ItemStack(id, 1, sub));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return switch (stack.getItemSubtype()) {
            case 1 -> "item.raw_fillet.name";
            case 2 -> "item.raw_gizzard.name";
            default -> "item.raw_chicken.name";
        };
    }
}
