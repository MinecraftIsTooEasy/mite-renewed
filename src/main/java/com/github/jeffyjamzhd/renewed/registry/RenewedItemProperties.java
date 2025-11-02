package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import moddedmite.rustedironcore.property.ItemProperties;
import net.minecraft.*;

public class RenewedItemProperties implements Runnable {
    @Override
    public void run() {
        MiTERenewed.LOGGER.info("Registering item properties!");

        for (Item item : Item.itemsList) {
            if (item instanceof IDamageableItem && (item.isTool() || item.isArmor())) {
                Material material = item.getMaterialForRepairs();
                Item nugget = item.getRepairItem();

                if (material == null || nugget == null)
                    continue;

                if (!material.isMetal())
                    continue;

                int heatLevel;
                if (item.hasMaterial(
                        Material.copper, Material.silver, Material.gold,
                        Material.iron, Material.rusted_iron)) {
                    heatLevel = 2;
                } else if (item.hasMaterial(Material.ancient_metal, Material.mithril)) {
                    heatLevel = 3;
                } else {
                    heatLevel = 4;
                }

                ItemProperties.HeatLevelRequired.register(item, heatLevel);
                FurnaceRecipes.smelting().addSmelting(item.itemID, new ItemStack(nugget));
            }
        }
    }
}
