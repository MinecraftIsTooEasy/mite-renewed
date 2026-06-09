package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.api.IFurnaceRecipes;
import com.github.jeffyjamzhd.renewed.block.BlockCrate;
import com.github.jeffyjamzhd.renewed.item.ItemBoneKnife;
import com.github.jeffyjamzhd.renewed.item.ItemHandpan;
import com.github.jeffyjamzhd.renewed.item.ItemPolearm;
import com.github.jeffyjamzhd.renewed.item.ItemRenewedBucket;
import com.github.jeffyjamzhd.renewed.item.recipe.ShapelessBucketConversionRecipe;
import com.github.jeffyjamzhd.renewed.item.recipe.ShapelessToolRecipe;
import net.minecraft.*;
import net.xiaoyu233.fml.reload.event.RecipeRegistryEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.LOGGER;

public class RenewedRecipes {
    private static final Material[] metals = {Material.copper, Material.silver, Material.gold, Material.iron, Material.ancient_metal, Material.mithril, Material.adamantium};
    private static final HashMap<Tuple, Tuple> SLAB_TO_FULL_MAP = new HashMap<>();

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
                    registry.registerShapedRecipe(new ItemStack(item), true,
                            "M  ",
                            "SI ",
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
        // 2 slab -> full block
        for (Map.Entry<Tuple, Tuple> entry : SLAB_TO_FULL_MAP.entrySet()) {
            Tuple key = entry.getKey();
            Tuple value = entry.getValue();

            ItemStack slab = new ItemStack((Block) key.getFirst(), 1, (int) key.getSecond());
            ItemStack block = new ItemStack((Block) value.getFirst(), 1, (int) value.getSecond());

            registry.registerShapelessRecipe(block, true, slab, slab);
        }

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
        registry.registerShapelessRecipe(
                new ItemStack(Item.stick, 1),
                true,
                new ItemStack(Block.sapling, 1, Short.MAX_VALUE)
        ).difficulty(300F);

        // Biomass recipes
        registry.registerShapelessRecipe(
                new ItemStack(RenewedItems.biomass, 1),
                false,
                new ItemStack(Block.plantRed, 1, Short.MAX_VALUE),
                new ItemStack(Block.plantRed, 1, Short.MAX_VALUE),
                new ItemStack(Block.plantRed, 1, Short.MAX_VALUE),
                new ItemStack(Block.plantRed, 1, Short.MAX_VALUE),
                new ItemStack(Block.plantRed, 1, Short.MAX_VALUE),
                new ItemStack(Block.plantRed, 1, Short.MAX_VALUE),
                new ItemStack(Block.plantRed, 1, Short.MAX_VALUE),
                new ItemStack(Block.plantRed, 1, Short.MAX_VALUE)
        ).difficulty(200F);
        registry.registerShapelessRecipe(
                new ItemStack(RenewedItems.biomass, 2),
                false,
                new ItemStack(Block.leaves, 1, Short.MAX_VALUE),
                new ItemStack(Block.leaves, 1, Short.MAX_VALUE),
                new ItemStack(Block.leaves, 1, Short.MAX_VALUE),
                new ItemStack(Block.leaves, 1, Short.MAX_VALUE)
        ).difficulty(500F);

    }

    /**
     * General shaped recipes
     * @param registry event
     */
    private static void registerShapedRecipes(RecipeRegistryEvent registry) {
        for (Material material : metals) {
            Item ingot = Item.getMatchingItem(ItemIngot.class, material);
            Block crate = BlockCrate.getBlockForMaterial(material);

            registry.registerShapedRecipe(new ItemStack(crate), true,
                    "IWI",
                    "W W",
                    "IWI",
                    'I', ingot,
                    'W', Block.planks
            );
        }
    }

    /**
     * Recipes for meshes
     * @param registry event
     */
    private static void registerMeshRecipes(RecipeRegistryEvent registry) {
        // Create mesh recipes
        registry.registerShapedRecipe(new ItemStack(RenewedItems.silk_mesh), true,
                "SS",
                "SS",
                'S', Item.silk
        ).difficulty(325F);
        registry.registerShapedRecipe(new ItemStack(RenewedItems.sinew_mesh), true,
                "SS",
                "SS",
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
    public static <T extends Item> ShapelessToolRecipe<T> registerToolRecipe(Class<T> tool, ItemStack output, CraftingManager man, Item... input) {
        ShapelessToolRecipe<T> recipe = new ShapelessToolRecipe<>(tool, output, Arrays.stream(input).map(ItemStack::new).toList());
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
    public static <T extends Item> ShapelessToolRecipe<T> registerToolRecipe(Class<T> tool, ItemStack output, CraftingManager man, ItemStack... input) {
        ArrayList<ItemStack> items = new ArrayList<>(Arrays.stream(input).toList());
        ShapelessToolRecipe<T> recipe = new ShapelessToolRecipe<>(tool, output, items);
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

        registerToolRecipe(ItemDagger.class, new ItemStack(RenewedItems.sharp_bone), manager, Item.bone)
                .addExclusion(ItemBoneKnife.class)
                .setDamage(150)
                .setDifficulty(900F);
        registerToolRecipe(ItemDagger.class, new ItemStack(Item.sinew, 2), manager, Item.leather)
                .setDamage(20)
                .setDifficulty(250F);
        registerToolRecipe(ItemDagger.class, new ItemStack(Item.sinew, 2), manager, Item.rottenFlesh)
                .setDamage(30)
                .setDifficulty(400F);
        registerToolRecipe(ItemDagger.class, new ItemStack(Item.silk), manager, new ItemStack(Item.bow, 1, Short.MAX_VALUE))
                .setDamage(10)
                .setDifficulty(100F);
        registerToolRecipe(ItemBow.class, new ItemStack(Item.stick, 2), manager, new Item[]{})
                .setDifficulty(200F);
        registerToolRecipe(ItemDagger.class, new ItemStack(Item.silk), manager, RenewedItems.tangled_web)
                .setDamage(10)
                .setDifficulty(140F);
        registerToolRecipe(ItemDagger.class, new ItemStack(Item.silk), manager, new ItemStack(Block.cloth, 1, Short.MAX_VALUE))
                .setDamage(40)
                .setDifficulty(400F);
        registerToolRecipe(ItemDagger.class, new ItemStack(Item.silk, 2), manager, new ItemStack(Block.sapling, 1, Short.MAX_VALUE))
                .setDamage(60)
                .setDifficulty(600F);

        // Register planks knife -> handpan
        registerToolRecipe(ItemDagger.class, new ItemStack(RenewedItems.handpan), manager, new ItemStack(Block.planks, 1, Short.MAX_VALUE), new ItemStack(Block.planks, 1, Short.MAX_VALUE))
                .setDamage(50)
                .setDifficulty(600F);
        // Register plank knife -> bowl
        registerToolRecipe(ItemDagger.class, new ItemStack(Item.bowlEmpty), manager, new ItemStack(Block.planks, 1, Short.MAX_VALUE))
                .setDamage(25)
                .setDifficulty(300F);

        registerCuttingRecipe(manager, RenewedItems.raw_pork, RenewedItems.cooked_pork, 25, 200F);
        registerCuttingRecipe(manager, RenewedItems.raw_beef, RenewedItems.cooked_beef, 30, 300F);
        registerCuttingRecipe(manager, RenewedItems.raw_poultry, RenewedItems.cooked_poultry, 20, 175F);
        registerCuttingRecipe(manager, RenewedItems.raw_lambchop, RenewedItems.cooked_lambchop, 30, 300F);

        for (Item item : Item.itemsList) {
            if (item == null) continue;

            // Bucket output recipes
            if (item instanceof ItemBucket bucket && bucket.contains(Material.stone) && bucket.getEmptyVessel() instanceof ItemRenewedBucket) {
                manager.getRecipeList().add(new ShapelessBucketConversionRecipe(bucket.getVesselMaterial()));
            }
        }
    }

    private static void registerCuttingRecipe(CraftingManager manager, Item raw, Item cooked, int damage, float difficulty) {
        registerToolRecipe(ItemDagger.class, new ItemStack(raw.itemID, 2, 1), manager, new ItemStack(raw.itemID, 1, 0))
                .setDamage(damage).setDifficulty(difficulty);
        registerToolRecipe(ItemDagger.class, new ItemStack(cooked.itemID, 2, 1), manager, new ItemStack(cooked.itemID, 1, 0))
                .setDamage(damage * 2).setDifficulty(difficulty + difficulty * 1.75F);
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
                new ItemStack(input.itemID, 1, 0),
                new ItemStack(output.itemID, 1, 0));
        recipes.mr$addSmeltingComplexEntry(
                new ItemStack(input.itemID, 2, 1),
                new ItemStack(output.itemID, 2, 1));
    }

    static {
        SLAB_TO_FULL_MAP.put(new Tuple(Block.stoneSingleSlab, 0), new Tuple(Block.stone, 0));
        SLAB_TO_FULL_MAP.put(new Tuple(Block.stoneSingleSlab, 3), new Tuple(Block.cobblestone, 0));
        SLAB_TO_FULL_MAP.put(new Tuple(Block.stoneSingleSlab, 4), new Tuple(Block.brick, 0));
        SLAB_TO_FULL_MAP.put(new Tuple(Block.stoneSingleSlab, 5), new Tuple(Block.stoneBrick, 0));
        SLAB_TO_FULL_MAP.put(new Tuple(Block.stoneSingleSlab, 6), new Tuple(Block.netherBrick, 0));
        SLAB_TO_FULL_MAP.put(new Tuple(Block.woodSingleSlab, 0), new Tuple(Block.planks, 0));
        SLAB_TO_FULL_MAP.put(new Tuple(Block.woodSingleSlab, 1), new Tuple(Block.planks, 1));
        SLAB_TO_FULL_MAP.put(new Tuple(Block.woodSingleSlab, 2), new Tuple(Block.planks, 2));
        SLAB_TO_FULL_MAP.put(new Tuple(Block.woodSingleSlab, 3), new Tuple(Block.planks, 3));
        SLAB_TO_FULL_MAP.put(new Tuple(Block.obsidianSingleSlab, 0), new Tuple(Block.obsidian, 0));
    }
}
