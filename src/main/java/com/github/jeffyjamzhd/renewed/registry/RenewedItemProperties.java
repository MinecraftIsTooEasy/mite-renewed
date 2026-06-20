package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.item.ItemRenewedBucket;
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
            if (item == null) continue;

            Material material = item.getMaterialForRepairs();
            Item repairItem = item.getRepairItem();

            if (material == null || repairItem == null)
                continue;

            if (!material.isMetal())
                continue;

            if (item instanceof IDamageableItem) {
                int components;

                if (item instanceof ItemArmor armor) {
                    components = armor.getRepairCost(false);
                    components /= armor.isChainMail() ? 2 : 1;
                } else {
                    components = item.getRepairCost();
                }

                addItemRecycling(item, repairItem, components);
            }

            if (item instanceof ItemHorseArmor) {
                addItemRecycling(item, repairItem, 10);
            }
        }

        // Add experience to shards
        RockExperience.register(Item.shardDiamond, ItemRock.getExperienceValueWhenSacrificed(new ItemStack(Item.diamond)) / 10);
        RockExperience.register(Item.shardEmerald, ItemRock.getExperienceValueWhenSacrificed(new ItemStack(Item.emerald)) / (Compatibility.ITE_LOADED ? 4 : 10));
        RockExperience.register(Item.shardNetherQuartz, ItemRock.getExperienceValueWhenSacrificed(new ItemStack(Item.netherQuartz)) / 10);
    }

    private void addItemRecycling(Item item, Item to, int amount) {
        int heatLevel;

        if (item.hasMaterial(
                Material.copper, Material.silver, Material.gold,
                Material.iron, Material.rusted_iron)) {
            heatLevel = 2;

            if (item.hasMaterial(Material.rusted_iron)) {
                amount /= 2;
            }
        } else if (item.hasMaterial(Material.ancient_metal, Material.mithril)) {
            heatLevel = 3;
        } else {
            heatLevel = 4;
        }

        HeatLevelRequired.register(item, heatLevel);
        FurnaceRecipes.smelting().addSmelting(item.itemID, new ItemStack(to, amount));
    }
}
