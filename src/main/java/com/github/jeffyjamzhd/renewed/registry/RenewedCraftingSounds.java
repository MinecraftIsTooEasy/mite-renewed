package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.api.registry.CraftingSoundRegistry;
import com.github.jeffyjamzhd.renewed.api.sound.CraftingSound;

import static com.github.jeffyjamzhd.renewed.api.sound.CraftingSound.basicSound;
import static com.github.jeffyjamzhd.renewed.api.sound.CraftingSound.metalSound;

import com.github.jeffyjamzhd.renewed.api.event.listener.CraftingSoundRegisterListener;
import com.github.jeffyjamzhd.renewed.item.ItemRenewedFood;
import com.github.jeffyjamzhd.renewed.item.recipe.ShapelessToolRecipe;
import net.minecraft.Block;
import net.minecraft.Item;
import net.minecraft.ItemFood;
import net.minecraft.Material;

import java.util.Random;

public class RenewedCraftingSounds implements CraftingSoundRegisterListener {
    private static Material[] metals = new Material[]{Material.copper, Material.silver, Material.gold, Material.iron, Material.ancient_metal, Material.mithril, Material.adamantium};
    private static Random rand = new Random();

    @Override
    public void register(CraftingSoundRegistry register) {
        register.registerSafe(CraftingSound.of(Material.wood, (output, recipe, world, player) -> {
            // Wood edge cases
            if (output.itemID == Block.workbench.blockID && output.getItemSubtype() < 5)
                return;
            if (output.getItem() instanceof ItemFood || output.getItem() instanceof ItemRenewedFood)
                return;

            // Wood alternates
            if (output.itemID == Item.stick.itemID)
                world.playSoundAtEntity(player, "mob.zombie.woodbreak", .5F, 1.4F - (rand.nextFloat() * .2F));
            else if (output.itemID == Item.cudgelWood.itemID || output.itemID == Item.clubWood.itemID)
                world.playSoundAtEntity(player, "mob.zombie.woodbreak", .5F, .9F - (rand.nextFloat() * .2F));
            else if (output.itemID == Block.planks.blockID)
                world.playSoundAtEntity(player, "mob.zombie.wood", .5F, 1.4F - (rand.nextFloat() * .2F));
            else
                world.playSoundAtEntity(player, MiTERenewed.RESOURCE_ID + "crafting.wood", .5F, 1.1F - (rand.nextFloat() * .2F));
        }));
        register.registerSafe(CraftingSound.of(Material.meat, (output, recipe, world, player) -> {
            if (recipe instanceof ShapelessToolRecipe) {
                world.playSoundAtEntity(player, MiTERenewed.RESOURCE_ID + "crafting.chop", .5F, 1.2F - (rand.nextFloat() * .2F));
            }
        }));
        register.registerSafe(CraftingSound.of(Material.flint, (output, recipe, world, player) -> {
            if (output.itemID != Item.flint.itemID && output.itemID != Item.chipFlint.itemID)
                world.playSoundAtEntity(player, "random.anvil_land", .5F, 2F - (rand.nextFloat() * .1F));
        }));


        register.registerSafe(CraftingSound.of(Material.silk, basicSound("random.bow", .5F, 1F)));
        register.registerSafe(CraftingSound.of(Material.leather, (output, recipe, world, player) -> {
            if (recipe instanceof ShapelessToolRecipe) {
                world.playSoundAtEntity(player, "random.bow", .5F, .8F);
            }
        }));

        for (Material metal : metals)
            register.registerSafe(CraftingSound.of(metal, metalSound(metal)));
    }
}
