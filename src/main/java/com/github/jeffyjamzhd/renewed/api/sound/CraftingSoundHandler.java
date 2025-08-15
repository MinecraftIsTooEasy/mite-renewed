package com.github.jeffyjamzhd.renewed.api.sound;

import net.minecraft.*;

import static com.github.jeffyjamzhd.renewed.api.registry.CraftingSoundRegistry.ITEM_SOUNDS;
import static com.github.jeffyjamzhd.renewed.api.registry.CraftingSoundRegistry.MATERIAL_SOUNDS;

public class CraftingSoundHandler {
    public static void onCraft(ItemStack output, IRecipe recipe, World world, EntityPlayer player) {
        Item item = output.getItem();
        // Check most specific first (itemid)
        if (ITEM_SOUNDS.containsKey(item.itemID)) {
            CraftingSound entry = ITEM_SOUNDS.get(item.itemID);
            entry.attemptSound(output, recipe, world, player);
            return;
        }

        // Check dura/repair material
        if (item.getMaterialForRepairs() != null) {
            Material mat = item.getMaterialForRepairs();
            if (MATERIAL_SOUNDS.containsKey(mat)) {
                CraftingSound entry = MATERIAL_SOUNDS.get(mat);
                entry.attemptSound(output, recipe, world, player);
                return;
            }
        } else if (item.getMaterialForDurability() != null) {
            Material mat = item.getMaterialForDurability();
            if (MATERIAL_SOUNDS.containsKey(mat)) {
                CraftingSound entry = MATERIAL_SOUNDS.get(mat);
                entry.attemptSound(output, recipe, world, player);
                return;
            }
        }

        // Check most generic last (material)
        int index = 0;
        while (item.getMaterial(index) != null) {
            Material mat = item.getMaterial(index);
            if (MATERIAL_SOUNDS.containsKey(mat)) {
                CraftingSound entry = MATERIAL_SOUNDS.get(mat);
                entry.attemptSound(output, recipe, world, player);
            }
            index++;
        }
    }
}
