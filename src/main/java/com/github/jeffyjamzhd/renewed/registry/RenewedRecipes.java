package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.item.ItemPolearm;
import com.github.jeffyjamzhd.renewed.item.recipe.ShapelessToolRecipe;
import com.github.jeffyjamzhd.renewed.registry.RenewedItems;
import net.minecraft.*;
import net.xiaoyu233.fml.reload.event.RecipeModifyEvent;
import net.xiaoyu233.fml.reload.event.RecipeRegistryEvent;
import net.xiaoyu233.fml.reload.event.recipe.ShapelessRecipeModifier;

import java.util.Arrays;

public class RenewedRecipes {
    public static void registerRecipes(RecipeRegistryEvent registry) {
        registerIterativeRecipes(registry);
    }

//    public static void modifyRecipes(RecipeModifyEvent registry) {
//        registry.addModifier(ShapelessRecipeModifier.Builder
//                .of(new ItemStack(Item.sinew, 4))
//                .ingredient(Item.leather)
//                .difficulty(400F)
//                .build()
//        );
//    }

    private static void registerIterativeRecipes(RecipeRegistryEvent registry) {
        for (Item item : Item.itemsList) {
            if (item instanceof ItemPolearm) {
                // Get tool material
                Material material = ((ItemPolearm) item).getToolMaterial();
                Item materialItem = parseItemFromMaterial(material);

                // Create recipe
                registry.registerShapedRecipe(new ItemStack(item), true,
                        "MS ",
                        " I ",
                        "  I",
                        'M', materialItem,
                        'S', Item.sinew,
                        'I', Item.stick
                ).difficulty(425F * difficultyMod(material));
            }
        }
    }

    public static ShapelessToolRecipe registerToolRecipe(ItemStack output, CraftingManager man, Item... input) {
        ShapelessToolRecipe recipe = new ShapelessToolRecipe(output, Arrays.stream(input).map(ItemStack::new).toList());
        man.getRecipeList().add(recipe);
        return recipe;
    }

    /**
     * Returns an item based on provided material
     * @param mat Material to provide
     * @return An item that best matches the material
     */
    private static Item parseItemFromMaterial(Material mat) {
        return switch (mat.getTranslationKey()) {
            case "bone" -> RenewedItems.sharp_bone;
            case "flint" -> Item.flint;
            default -> null;
        };
    }

    /**
     * Returns a difficulty modifier/factor based on provided material
     * @param mat Material to provide
     * @return Difficulty modifier/factor
     */
    private static float difficultyMod(Material mat) {
        return switch (mat.getTranslationKey()) {
            case "bone" -> 1.33F;
            case "flint" -> 0.8F;
            default -> 1.0F;
        };
    }

    /**
     * Registers crafting recipes that use a unique solver (e.g. ShapelessToolRecipe)
     * @param manager Crafting manager to register with
     */
    public static void registerSpecialRecipes(CraftingManager manager) {
        // Iterate and add knife -> sharp bone recipes
        for (Item item : Item.itemsList)
            if (item instanceof ItemDagger) {
                // Get item material
                Material mat = ((ItemDagger) item).getToolMaterial();

                // Register knife -> sharp bone
                if (item.itemID != RenewedItems.sharp_bone.itemID)
                    registerToolRecipe(new ItemStack(RenewedItems.sharp_bone), manager, item, Item.bone).setDamage(80).setDifficulty(1200F).scaleDifficulty(10F * (float)(1D / Math.sqrt(item.getCraftingDifficultyAsComponent(new ItemStack(item)))));

                // Register generic extraction recipes
                registerToolRecipe(new ItemStack(Item.sinew, 2), manager, item, Item.leather).setDamage(10).setDifficulty(450F).scaleDifficulty(10F * (float)(1D / Math.sqrt(item.getCraftingDifficultyAsComponent(new ItemStack(item)))));
                registerToolRecipe(new ItemStack(Item.silk), manager, item, RenewedItems.tangled_web).setDamage(30).setDifficulty(180F).scaleDifficulty(10F * (float)(1D / Math.sqrt(item.getCraftingDifficultyAsComponent(new ItemStack(item)))));
            }
    }
}
