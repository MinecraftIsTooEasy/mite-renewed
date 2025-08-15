package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.api.IFurnaceRecipes;
import com.github.jeffyjamzhd.renewed.api.IMaterial;
import com.github.jeffyjamzhd.renewed.item.ItemHandpan;
import com.github.jeffyjamzhd.renewed.item.ItemPolearm;
import com.github.jeffyjamzhd.renewed.item.ItemQuern;
import com.github.jeffyjamzhd.renewed.item.recipe.HandpanOutput;
import com.github.jeffyjamzhd.renewed.item.recipe.HandpanRecipeProcessor;
import com.github.jeffyjamzhd.renewed.item.recipe.ShapelessToolRecipe;
import net.minecraft.*;
import net.xiaoyu233.fml.reload.event.RecipeRegistryEvent;

import java.util.ArrayList;
import java.util.Arrays;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.LOGGER;

public class RenewedRecipes {
    public static void registerRecipes(RecipeRegistryEvent registry) {
        LOGGER.info("Registering recipes!");
        registerIterativeRecipes(registry);
        registerShapedRecipes(registry);
        registerShapelessRecipes(registry);
        registerMeshRecipes(registry);
        registerFurnaceRecipes();
    }

    /**
     * Iterative recipes (e.g. tool classes)
     * @param registry event
     */
    private static void registerIterativeRecipes(RecipeRegistryEvent registry) {
        for (Item item : Item.itemsList) {
            if (item instanceof ItemPolearm) {
                // Get tool material
                Material material = ((ItemPolearm) item).getToolMaterial();
                ItemStack materialItem = parseItemFromMaterial(material);

                // Create recipe
                if (((ItemPolearm) item).isPrimitive()) {
                    registry.registerShapedRecipe(new ItemStack(item), true,
                            "MS ",
                            " I ",
                            "  I",
                            'M', materialItem,
                            'S', Item.sinew,
                            'I', Item.stick
                    ).difficulty(425F * difficultyMod(material));
                } else {
                    registry.registerShapedRecipe(new ItemStack(item), true,
                            "M  ",
                            " I ",
                            "  I",
                            'M', materialItem,
                            'I', Item.stick
                    );
                }

            }
        }
    }

    /**
     * General shapeless recipes
     * @param registry event
     */
    private static void registerShapelessRecipes(RecipeRegistryEvent registry) {
        // Simple pan -> mesh recipes
        registry.registerShapelessRecipe(
                new ItemStack(RenewedItems.handpan, 1, ItemHandpan.SINEW),
                true,
                RenewedItems.handpan, RenewedItems.sinew_mesh
        ).difficulty(100F);
        registry.registerShapelessRecipe(
                new ItemStack(RenewedItems.handpan, 1, ItemHandpan.SILK),
                true,
                RenewedItems.handpan, RenewedItems.silk_mesh
        ).difficulty(100F);
    }

    /**
     * General shaped recipes
     * @param registry event
     */
    private static void registerShapedRecipes(RecipeRegistryEvent registry) {
        registry.registerShapedRecipe(new ItemStack(RenewedItems.quern), true,
                "C",
                "B",
                'B', Item.bowlEmpty,
                'C', Item.cudgelWood
        ).difficulty(50F);
    }

    /**
     * Recipes for meshes
     * @param registry event
     */
    private static void registerMeshRecipes(RecipeRegistryEvent registry) {
        // Create mesh recipes
        registry.registerShapedRecipe(new ItemStack(RenewedItems.silk_mesh), true,
                "SSS",
                "SSS",
                'S', Item.silk
        ).difficulty(325F);
        registry.registerShapedRecipe(new ItemStack(RenewedItems.sinew_mesh), true,
                "SSS",
                "SSS",
                'S', Item.sinew
        ).difficulty(775F);
    }

    /**
     * Registers a shapeless tool recipe
     * @param output Item to output
     * @param man Crafting manager
     * @param input Inputs
     * @return ShapelessToolRecipe
     */
    public static ShapelessToolRecipe registerToolRecipe(ItemStack output, CraftingManager man, Item... input) {
        ShapelessToolRecipe recipe = new ShapelessToolRecipe(output, Arrays.stream(input).map(ItemStack::new).toList());
        man.getRecipeList().add(recipe);
        return recipe;
    }

    /**
     * Registers a shapeless tool recipe, with a tool specified
     * @param output Item to output
     * @param man Crafting manager
     * @param tool Tool as part of the ingredients
     * @param input Other ingredients
     * @return ShapelessToolRecipe
     */
    public static ShapelessToolRecipe registerToolRecipe(ItemStack output, CraftingManager man, Item tool, ItemStack... input) {
        ArrayList<ItemStack> items = new ArrayList<>();
        items.add(new ItemStack(tool));
        items.addAll(Arrays.stream(input).toList());

        ShapelessToolRecipe recipe = new ShapelessToolRecipe(output, items);
        man.getRecipeList().add(recipe);
        return recipe;
    }

    /**
     * Returns an item based on provided material
     * @param mat Material to provide
     * @return An item that best matches the material
     */
    private static ItemStack parseItemFromMaterial(Material mat) {
        return switch (mat.mr$getName()) {
            case "bone" -> new ItemStack(RenewedItems.sharp_bone);
            case "flint" -> new ItemStack(Item.flint);
            case "obsidian" -> new ItemStack(Block.obsidian);
            case "copper" -> new ItemStack(Item.ingotCopper);
            case "silver" -> new ItemStack(Item.ingotSilver);
            case "gold" -> new ItemStack(Item.ingotGold);
            case "iron" -> new ItemStack(Item.ingotIron);
            case "ancient_metal" -> new ItemStack(Item.ingotAncientMetal);
            case "mithril" -> new ItemStack(Item.ingotMithril);
            case "adamantium" -> new ItemStack(Item.ingotAdamantium);
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
        LOGGER.info("Registering special recipes!");

        // Iterate and add knife -> sharp bone recipes
        for (Item item : Item.itemsList) {
            if (item instanceof ItemDagger) {
                // Get item material and factor
                float difficulty = item.getLowestCraftingDifficultyToProduce();
                difficulty = Float.compare(difficulty, Float.MAX_VALUE) == 0 ? 70F : difficulty;
                float fac = 20F * (1F / MathHelper.sqrt_float(difficulty));

                // Register knife -> sharp bone
                if (item.itemID != RenewedItems.sharp_bone.itemID)
                    registerToolRecipe(new ItemStack(RenewedItems.sharp_bone), manager, item, Item.bone)
                            .setDamage(60).setDifficulty(1200F).scaleDifficulty(fac);

                // Register generic extraction recipes
                registerToolRecipe(new ItemStack(Item.sinew, 2), manager, item, Item.leather)
                        .setDamage(20).setDifficulty(450F).scaleDifficulty(fac);
                registerToolRecipe(new ItemStack(Item.sinew, 2), manager, item, Item.rottenFlesh)
                        .setDamage(30).setDifficulty(600F).scaleDifficulty(fac);
                registerToolRecipe(new ItemStack(Item.silk), manager, item, RenewedItems.tangled_web)
                        .setDamage(10).setDifficulty(180F).scaleDifficulty(fac);
                registerToolRecipe(new ItemStack(Item.silk, 2), manager, item, new ItemStack(Block.cloth, 1, Short.MAX_VALUE))
                        .setDamage(40).setDifficulty(1200F).scaleDifficulty(fac);

                // Register planks knife -> handpan
                registerToolRecipe(new ItemStack(RenewedItems.handpan), manager, item, new ItemStack(Block.planks), new ItemStack(Block.planks))
                        .setDamage(50).setDifficulty(700F).scaleDifficulty(fac);

                // Meat cutting recipes
                registerCuttingRecipe(manager, item, RenewedItems.raw_pork, RenewedItems.cooked_pork, 25, 400F, fac);
                registerCuttingRecipe(manager, item, RenewedItems.raw_beef, RenewedItems.cooked_beef, 30, 500F, fac);
                registerCuttingRecipe(manager, item, RenewedItems.raw_poultry, RenewedItems.cooked_poultry, 20, 350F, fac);
                registerCuttingRecipe(manager, item, RenewedItems.raw_lambchop, RenewedItems.cooked_lambchop, 30, 500F, fac);
            }

            // Quern recipes
            if (item instanceof ItemQuern) {
                registerToolRecipe(new ItemStack(Item.sugar, 1), manager, item, Item.reed).setDamage(20).setDifficulty(225F);
                registerToolRecipe(new ItemStack(RenewedItems.biomass, 1), manager, item,
                        new ItemStack(Block.plantRed, 1, Short.MAX_VALUE),
                        new ItemStack(Block.plantRed, 1, Short.MAX_VALUE),
                        new ItemStack(Block.plantRed, 1, Short.MAX_VALUE),
                        new ItemStack(Block.plantRed, 1, Short.MAX_VALUE),
                        new ItemStack(Block.plantRed, 1, Short.MAX_VALUE),
                        new ItemStack(Block.plantRed, 1, Short.MAX_VALUE),
                        new ItemStack(Block.plantRed, 1, Short.MAX_VALUE),
                        new ItemStack(Block.plantRed, 1, Short.MAX_VALUE))
                        .setDamage(50).setDifficulty(400F);
                registerToolRecipe(new ItemStack(Item.dyePowder, 2, ItemDye.WHITE), manager, item, Item.bone)
                        .setDamage(25).setDifficulty(250F);
            }
        }
    }

    private static void registerCuttingRecipe(CraftingManager manager, Item tool, Item raw, Item cooked, int damage, float difficulty, float scale) {
        registerToolRecipe(new ItemStack(raw.itemID, 2, 1), manager, tool, new ItemStack(raw.itemID, 1, 0))
                .setDamage(damage).setDifficulty(difficulty).scaleDifficulty(scale);
        registerToolRecipe(new ItemStack(cooked.itemID, 2, 1), manager, tool, new ItemStack(cooked.itemID, 1, 0))
                .setDamage(damage * 2).setDifficulty(difficulty * 1.75F).scaleDifficulty(scale);
    }

    public static void registerFurnaceRecipes() {
        FurnaceRecipes recipes = FurnaceRecipes.smelting();

        registerFurnaceMeatRecipe(recipes, RenewedItems.raw_pork, RenewedItems.cooked_pork);
        registerFurnaceMeatRecipe(recipes, RenewedItems.raw_poultry, RenewedItems.cooked_poultry);
        registerFurnaceMeatRecipe(recipes, RenewedItems.raw_beef, RenewedItems.cooked_beef);
        registerFurnaceMeatRecipe(recipes, RenewedItems.raw_lambchop, RenewedItems.cooked_lambchop);

        recipes.mr$addSmeltingComplexEntry(
                new ItemStack(RenewedItems.raw_poultry, 1, 2),
                new ItemStack(RenewedItems.cooked_poultry, 1, 2));
    }

    private static void registerFurnaceMeatRecipe(IFurnaceRecipes recipes, Item input, Item output) {
        recipes.mr$addSmeltingComplexEntry(
                new ItemStack(input, 1, 0),
                new ItemStack(output, 1, 0));
        recipes.mr$addSmeltingComplexEntry(
                new ItemStack(input, 2, 1),
                new ItemStack(output, 2, 1));
    }
}
