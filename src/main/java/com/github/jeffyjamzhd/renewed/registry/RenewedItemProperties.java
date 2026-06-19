package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.util.Compatibility;
import moddedmite.rustedironcore.property.ItemProperties;
import net.minecraft.*;

import static moddedmite.rustedironcore.property.ItemProperties.HeatLevelRequired;
import static moddedmite.rustedironcore.property.ItemProperties.RockExperience;

public class RenewedItemProperties implements Runnable {
    @Override
    public void run() {
        MiTERenewed.LOGGER.info("Registering item properties!");

        // If running ITF, don't register these as it conflicts with
        // some of that mod's gameplay
        if (Compatibility.ITF_LOADED) {
            return;
        }

        // Handle nugget smelting
        for (Item item : Item.itemsList) {
            if (item instanceof IDamageableItem && (item.isTool() || item.isArmor())) {
                Material material = item.getMaterialForRepairs();
                Item nugget = item.getRepairItem();

                if (material == null || nugget == null)
                    continue;

                if (!material.isMetal())
                    continue;

                int heatLevel;
                int components;

                if (item instanceof ItemArmor armor) {
                    components = armor.getRepairCost(false);
                    components /= armor.isChainMail() ? 2 : 1;
                } else {
                    components = item.getRepairCost();
                }

                if (item.hasMaterial(
                        Material.copper, Material.silver, Material.gold,
                        Material.iron, Material.rusted_iron)) {
                    heatLevel = 2;

                    if (item.hasMaterial(Material.rusted_iron)) {
                        components /= 2;
                    }
                } else if (item.hasMaterial(Material.ancient_metal, Material.mithril)) {
                    heatLevel = 3;
                } else {
                    heatLevel = 4;
                }


                HeatLevelRequired.register(item, heatLevel);
                FurnaceRecipes.smelting().addSmelting(item.itemID, new ItemStack(nugget, components));
            }
        }

        // Add experience to shards
        RockExperience.register(Item.shardDiamond, ItemRock.getExperienceValueWhenSacrificed(new ItemStack(Item.diamond)) / 10);
        RockExperience.register(Item.shardEmerald, ItemRock.getExperienceValueWhenSacrificed(new ItemStack(Item.emerald)) / (Compatibility.ITE_LOADED ? 4 : 10));
        RockExperience.register(Item.shardNetherQuartz, ItemRock.getExperienceValueWhenSacrificed(new ItemStack(Item.netherQuartz)) / 10);
    }
}
