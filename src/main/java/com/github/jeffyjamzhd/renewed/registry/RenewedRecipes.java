package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.item.ItemHandpan;
import com.github.jeffyjamzhd.renewed.item.ItemPolearm;
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
        registerShapelessRecipes(registry);
        registerMeshRecipes(registry);
        registerHandpanRecipes();
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
                );
        registry.registerShapelessRecipe(
                new ItemStack(RenewedItems.handpan, 1, ItemHandpan.SILK),
                true,
                RenewedItems.handpan, RenewedItems.silk_mesh
        );
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
     * Handpan specific recipes
     */
    private static void registerHandpanRecipes() {
        // Sinew mesh
        HandpanRecipeProcessor.registerHandpanRecipe(
                Block.gravel, 10, 120, 1,
                new HandpanOutput(Block.gravel, .5F),
                new HandpanOutput(Item.chipFlint, .33F),
                new HandpanOutput(Item.shardObsidian, .25F),
                new HandpanOutput(Item.copperNugget, .08F),
                new HandpanOutput(Item.flint, .05F)
        );
        HandpanRecipeProcessor.registerHandpanRecipe(
                Block.dirt, 5, 160, 1,
                new HandpanOutput(Block.dirt, .5F),
                new HandpanOutput(Item.wormRaw, .3F),
                new HandpanOutput(Item.bone, .05F),
                new HandpanOutput(Item.copperNugget, .05F)
        );
        HandpanRecipeProcessor.registerHandpanRecipe(
                Block.sand, 5, 80, 1,
                HandpanOutput.of(Block.sand, .5F),
                HandpanOutput.of(Item.clay, .33F),
                HandpanOutput.of(Item.reed, .1F)
        );

        // Silk/string mesh
        HandpanRecipeProcessor.registerHandpanRecipe(
                Block.gravel, 15, 160, 2,
                HandpanOutput.of(Block.gravel, .33F),
                HandpanOutput.of(Item.copperNugget, .5F),
                HandpanOutput.of(Item.silverNugget, .25F),
                HandpanOutput.of(Item.goldNugget, .075F),
                HandpanOutput.of(Item.shardDiamond, .02F)
        );
        HandpanRecipeProcessor.registerHandpanRecipe(
                Block.dirt, 10, 220, 2,
                HandpanOutput.of(Block.dirt, .33F),
                HandpanOutput.of(Item.wormRaw, .5F),
                HandpanOutput.of(Item.seeds, .75F),
                HandpanOutput.of(Item.pumpkinSeeds, .1F),
                HandpanOutput.of(Item.bone, .075F),
                HandpanOutput.of(Item.copperNugget, .1F)
        );
        HandpanRecipeProcessor.registerHandpanRecipe(
                Block.sand, 10, 120, 2,
                HandpanOutput.of(Block.sand, .33F),
                HandpanOutput.of(Item.reed, .2F),
                HandpanOutput.of(Item.shardGlass, .33F),
                HandpanOutput.of(Item.shardEmerald, .04F),
                HandpanOutput.of(Item.copperNugget, .1F)
        );
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
        LOGGER.info("Registering special recipes!");

        // Iterate and add knife -> sharp bone recipes
        for (Item item : Item.itemsList)
            if (item instanceof ItemDagger) {
                // Get item material and factor
                Material mat = ((ItemDagger) item).getToolMaterial();
                float fac = 20F * (float)(1D / Math.sqrt(item.getCraftingDifficultyAsComponent(new ItemStack(item))));

                // Register knife -> sharp bone
                if (item.itemID != RenewedItems.sharp_bone.itemID)
                    registerToolRecipe(new ItemStack(RenewedItems.sharp_bone), manager, item, Item.bone)
                            .setDamage(60).setDifficulty(1200F).scaleDifficulty(fac);

                // Register generic extraction recipes
                registerToolRecipe(new ItemStack(Item.sinew, 2), manager, item, Item.leather)
                        .setDamage(20).setDifficulty(450F).scaleDifficulty(fac);
                registerToolRecipe(new ItemStack(Item.sinew, 3), manager, item, Item.rottenFlesh)
                        .setDamage(30).setDifficulty(600F).scaleDifficulty(fac);
                registerToolRecipe(new ItemStack(Item.silk), manager, item, RenewedItems.tangled_web)
                        .setDamage(10).setDifficulty(180F).scaleDifficulty(fac);
                registerToolRecipe(new ItemStack(Item.silk, 2), manager, item, new ItemStack(Block.cloth))
                        .setDamage(40).setDifficulty(1200F).scaleDifficulty(fac);

                // Register planks knife -> handpan
                registerToolRecipe(new ItemStack(RenewedItems.handpan), manager, item, new ItemStack(Block.planks), new ItemStack(Block.planks))
                        .setDamage(50).setDifficulty(700F).scaleDifficulty(fac);
            }
    }
}
