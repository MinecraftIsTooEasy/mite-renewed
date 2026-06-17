package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import moddedmite.rustedironcore.property.ItemProperties;
import net.minecraft.*;

public class RenewedItemProperties implements Runnable {
    @Override
    public void run() {
        MiTERenewed.LOGGER.info("Registering item properties!");

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
                int components = ((IDamageableItem) item).getRepairCost();
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


                ItemProperties.HeatLevelRequired.register(item, heatLevel);
                FurnaceRecipes.smelting().addSmelting(item.itemID, new ItemStack(nugget, components));
            }
        }

        ItemProperties.BurnTime.register(Item.getItem(Block.woodenButton), 10);

        // Add experience to shards
        ItemProperties.RockExperience.register(Item.shardDiamond, 50);
        ItemProperties.RockExperience.register(Item.shardEmerald, 25);
        ItemProperties.RockExperience.register(Item.shardNetherQuartz, 5);
    }
}
