package com.github.jeffyjamzhd.renewed.item.recipe;

import com.google.common.collect.BiMap;
import net.minecraft.Block;
import net.minecraft.ItemStack;
import net.minecraft.World;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.LOGGER;

public class HandpanRecipeProcessor {
    public static final HandpanRecipeProcessor INSTANCE = new HandpanRecipeProcessor();
    public static final HashMap<Key, HandpanRecipe> RECIPES = new HashMap<>();
    private static final Consumer<HandpanRecipe> REGISTRY =
            recipe -> RECIPES.put(new Key(recipe.getInput().itemID, recipe.getSubtype()), recipe);

    public void registerHandpanRecipe(Block input, HandpanOutput... outputs) {
        HandpanRecipe.Builder builder = HandpanRecipe.Builder.input(new ItemStack(input));
        for (HandpanOutput output : outputs)
            builder = builder.output(output);
        builder.build(REGISTRY);
    }

    public void registerHandpanRecipe(Block input, int damage, int speed, int subtype, HandpanOutput... outputs) {
        HandpanRecipe.Builder builder = HandpanRecipe.Builder.input(new ItemStack(input));
        for (HandpanOutput output : outputs)
            builder = builder.output(output);
        builder.damage(damage).speed(speed).subtype(subtype).build(REGISTRY);
    }

    public static List<ItemStack> processRecipe(World world, ItemStack pan, int id) {
        if (world.isRemote) {
            LOGGER.error("Handpan recipe processor run on client - why?");
            return List.of();
        }

        // Get recipe
        HandpanRecipe recipe = getRecipe(id, pan.getItemSubtype());
        if (recipe != null)
            return recipe.generateOutput(world);
        return  List.of();
    }

    public static HashMap<Key, HandpanRecipe> getRecipes() {
        return RECIPES;
    }

    public static HandpanRecipe getRecipe(int id, int subtype) {
        return RECIPES.get(new Key(id, subtype));
    }

    public static boolean hasRecipe(ItemStack input, int subtype) {
        return RECIPES.containsKey(new Key(input.itemID, subtype));
    }

    public static int getUseTime(ItemStack input) {
        HandpanRecipe recipe = getRecipe(input.itemID, input.getItemSubtype());
        return recipe != null ? recipe.getSpeed() : -1;
    }

    public static int getDamage(ItemStack input) {
        HandpanRecipe recipe = getRecipe(input.itemID, input.getItemSubtype());
        return recipe != null ? recipe.getDamage() : 0;
    }

    public static class Key {
        public int itemID;
        public byte subtype;

        public Key(int itemID, int subtype) {
            this.itemID = itemID;
            this.subtype = (byte) subtype;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            Key key = (Key) o;
            return itemID == key.itemID && subtype == key.subtype;
        }

        @Override
        public int hashCode() {
            return Objects.hash(itemID, subtype);
        }
    }
}
