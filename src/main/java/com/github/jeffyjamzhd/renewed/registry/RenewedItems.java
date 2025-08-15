package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.api.item.FoodData;
import com.github.jeffyjamzhd.renewed.item.*;
import net.minecraft.*;
import net.xiaoyu233.fml.reload.event.ItemRegistryEvent;
import net.xiaoyu233.fml.reload.utils.IdUtil;
import net.xiaoyu233.fml.util.ReflectHelper;

import static com.github.jeffyjamzhd.renewed.api.item.FoodData.*;
import static net.xiaoyu233.fml.util.ReflectHelper.createInstance;
import static com.github.jeffyjamzhd.renewed.MiTERenewed.LOGGER;

public class RenewedItems {
    /**
     * Renewed specific items
     */
    public static final ItemKnife sharp_bone =
            createInstance(ItemKnife.class, new Class[]{int.class, Material.class}, IdUtil.getNextItemID(), RenewedMaterial.bone);
    public static final Item tangled_web =
            createInstance(Item.class, new Class[]{int.class, Material.class, String.class}, IdUtil.getNextItemID(), Material.silk, "tangled_web");
    public static final ItemPolearm flint_spear =
            createInstance(ItemPolearm.class, new Class[]{int.class, Material.class, ResourceLocation.class},
            IdUtil.getNextItemID(), Material.flint, new ResourceLocation(MiTERenewed.RESOURCE_ID + "tool/polearm/hand/flint_spear"));
    public static final ItemPolearm bone_spear =
            createInstance(ItemPolearm.class, new Class[]{int.class, Material.class, ResourceLocation.class},
            IdUtil.getNextItemID(), RenewedMaterial.bone, new ResourceLocation(MiTERenewed.RESOURCE_ID + "tool/polearm/hand/bone_spear"));
    public static final Item sinew_mesh =
            createInstance(Item.class, new Class[]{int.class, Material.class, String.class}, IdUtil.getNextItemID(), Material.leather, "sinew_mesh");
    public static final Item silk_mesh =
            createInstance(Item.class, new Class[]{int.class, Material.class, String.class}, IdUtil.getNextItemID(), Material.silk, "silk_mesh");
    public static final Item handpan =
            createInstance(ItemHandpan.class, new Class[]{int.class}, IdUtil.getNextItemID());
    public static final ItemPolearm obsidian_spear =
            createInstance(ItemPolearm.class, new Class[]{int.class, Material.class, ResourceLocation.class},
                    IdUtil.getNextItemID(), Material.obsidian, new ResourceLocation(MiTERenewed.RESOURCE_ID + "tool/polearm/hand/obsidian_spear"));
    public static final ItemPolearm copper_spear =
            createInstance(ItemPolearm.class, new Class[]{int.class, Material.class, ResourceLocation.class},
                    IdUtil.getNextItemID(), Material.copper, new ResourceLocation(MiTERenewed.RESOURCE_ID + "tool/polearm/hand/copper_spear"));
    public static final ItemPolearm silver_spear =
            createInstance(ItemPolearm.class, new Class[]{int.class, Material.class, ResourceLocation.class},
                    IdUtil.getNextItemID(), Material.silver, new ResourceLocation(MiTERenewed.RESOURCE_ID + "tool/polearm/hand/silver_spear"));
    public static final ItemPolearm gold_spear =
            createInstance(ItemPolearm.class, new Class[]{int.class, Material.class, ResourceLocation.class},
                    IdUtil.getNextItemID(), Material.gold, new ResourceLocation(MiTERenewed.RESOURCE_ID + "tool/polearm/hand/gold_spear"));
    public static final ItemPolearm iron_spear =
            createInstance(ItemPolearm.class, new Class[]{int.class, Material.class, ResourceLocation.class},
                    IdUtil.getNextItemID(), Material.iron, new ResourceLocation(MiTERenewed.RESOURCE_ID + "tool/polearm/hand/iron_spear"));
    public static final ItemPolearm ancient_metal_spear =
            createInstance(ItemPolearm.class, new Class[]{int.class, Material.class, ResourceLocation.class},
                    IdUtil.getNextItemID(), Material.ancient_metal, new ResourceLocation(MiTERenewed.RESOURCE_ID + "tool/polearm/hand/ancient_metal_spear"));
    public static final ItemPolearm mithril_spear =
            createInstance(ItemPolearm.class, new Class[]{int.class, Material.class, ResourceLocation.class},
                    IdUtil.getNextItemID(), Material.mithril, new ResourceLocation(MiTERenewed.RESOURCE_ID + "tool/polearm/hand/mithril_spear"));
    public static final ItemPolearm adamantium_spear =
            createInstance(ItemPolearm.class, new Class[]{int.class, Material.class, ResourceLocation.class},
                    IdUtil.getNextItemID(), Material.adamantium, new ResourceLocation(MiTERenewed.RESOURCE_ID + "tool/polearm/hand/adamantium_spear"));
    public static final ItemQuern quern =
            createInstance(ItemQuern.class, new Class[]{int.class, String.class}, IdUtil.getNextItemID(), "quern");
    public static final ItemBiomass biomass =
            createInstance(ItemBiomass.class, new Class[]{int.class}, IdUtil.getNextItemID());

    /**
     * Vanilla overwrites
     */
    public static final ItemRenewedFood raw_pork =
            new ItemRenewedFood(63, 2, Material.meat);
    public static final ItemRenewedFood cooked_pork =
            new ItemRenewedFood(64, 2, Material.meat);
    public static final ItemRenewedFood raw_beef =
            new ItemRenewedFood(107, 2, Material.meat);
    public static final ItemRenewedFood cooked_beef =
            new ItemRenewedFood(108, 2, Material.meat);
    public static final ItemRenewedFood raw_poultry =
            new ItemRenewedFood(109, 3, Material.meat);
    public static final ItemRenewedFood cooked_poultry =
            new ItemRenewedFood(110, 3, Material.meat);
    public static final ItemRenewedFood raw_lambchop =
            new ItemRenewedFood(916, 2, Material.meat);
    public static final ItemRenewedFood cooked_lambchop =
            new ItemRenewedFood(917, 2, Material.meat);

    // Called upon register event
    public static void register(ItemRegistryEvent registry) {
        LOGGER.info("Registering items!");
        registerItem(registry, "sharp_bone", "tool/sharp_bone", sharp_bone).setCreativeTab(CreativeTabs.tabTools);
        registerItem(registry, "tangled_web", "tangled_web", tangled_web).setCreativeTab(CreativeTabs.tabMaterials);
        registerItem(registry, "flint_spear", "tool/polearm/flint_spear", flint_spear);
        registerItem(registry, "bone_spear", "tool/polearm/bone_spear", bone_spear);
        registerItem(registry, "obsidian_spear", "tool/polearm/obsidian_spear", obsidian_spear);
        registerItem(registry, "copper_spear", "tool/polearm/copper_spear", copper_spear);
        registerItem(registry, "silver_spear", "tool/polearm/silver_spear", silver_spear);
        registerItem(registry, "gold_spear", "tool/polearm/gold_spear", gold_spear);
        registerItem(registry, "iron_spear", "tool/polearm/iron_spear", iron_spear);
        registerItem(registry, "ancient_metal_spear", "tool/polearm/ancient_metal_spear", ancient_metal_spear);
        registerItem(registry, "mithril_spear", "tool/polearm/mithril_spear", mithril_spear);
        registerItem(registry, "adamantium_spear", "tool/polearm/adamantium_spear", adamantium_spear);
        registerItem(registry, "sinew_mesh", "sinew_mesh", sinew_mesh).setCreativeTab(CreativeTabs.tabMaterials);
        registerItem(registry, "silk_mesh", "silk_mesh", silk_mesh).setCreativeTab(CreativeTabs.tabMaterials);
        registerItem(registry, "handpan", "tool/handpan", handpan);
        registerItem(registry, "quern", "tool/quern", quern);
        registerItem(registry, "biomass", "biomass", biomass);
    }

    /**
     * Registers an item.
     * @param registry The register
     * @param name Name of the item
     * @param resource Texture path
     * @param item Item object
     * @return The registered item
     */
    private static Item registerItem(ItemRegistryEvent registry, String name, String resource, Item item) {
        return registry.register(MiTERenewed.NAMESPACE, MiTERenewed.RESOURCE_ID + resource, name, item);
    }

    static {
        Item.silk.setLowestCraftingDifficultyToProduce(100F);
        sharp_bone.setLowestCraftingDifficultyToProduce(200F);
        tangled_web.setLowestCraftingDifficultyToProduce(250F);
        sinew_mesh.setLowestCraftingDifficultyToProduce(400F);
        silk_mesh.setLowestCraftingDifficultyToProduce(300F);
        biomass.setLowestCraftingDifficultyToProduce(0F);

        raw_pork
                .setTranslationKeys("item.porkchopRaw", "item.raw_bacon")
                .setTextures(
                        new ResourceLocation("minecraft:porkchop_raw"),
                        new ResourceLocation(MiTERenewed.RESOURCE_ID + "food/raw_bacon"))
                .setData(
                        foodData(4, 4, 0, HAS_PROTEIN),
                        foodData(2, 1, 0, HAS_PROTEIN))
                .setAnimalProduct()
                .setCraftingDifficultyAsComponent(100F)
                .setLowestCraftingDifficultyToProduce(0F);
        cooked_pork
                .setTranslationKeys("item.porkchopCooked", "item.cooked_bacon")
                .setTextures(
                        new ResourceLocation("minecraft:porkchop_cooked"),
                        new ResourceLocation(MiTERenewed.RESOURCE_ID + "food/cooked_bacon"))
                .setData(
                        foodData(8, 8, 0, HAS_PROTEIN),
                        foodData(4, 4, 0, HAS_PROTEIN))
                .setXP(4, 2)
                .setAnimalProduct()
                .setCraftingDifficultyAsComponent(100F)
                .setLowestCraftingDifficultyToProduce(0F);
        raw_beef
                .setTranslationKeys("item.beefRaw", "item.raw_sirloin")
                .setTextures(
                        new ResourceLocation("minecraft:beef_raw"),
                        new ResourceLocation(MiTERenewed.RESOURCE_ID + "food/raw_sirloin"))
                .setData(
                        foodData(5, 5, 0, HAS_PROTEIN),
                        foodData(3, 2, 0, HAS_PROTEIN))
                .setAnimalProduct()
                .setCraftingDifficultyAsComponent(100F)
                .setLowestCraftingDifficultyToProduce(0F);
        cooked_beef
                .setTranslationKeys("item.beefCooked", "item.cooked_sirloin")
                .setTextures(
                        new ResourceLocation("minecraft:beef_cooked"),
                        new ResourceLocation(MiTERenewed.RESOURCE_ID + "food/cooked_sirloin"))
                .setData(
                        foodData(10, 10, 0, HAS_PROTEIN),
                        foodData(5, 5, 0, HAS_PROTEIN))
                .setXP(4, 2)
                .setAnimalProduct()
                .setCraftingDifficultyAsComponent(100F)
                .setLowestCraftingDifficultyToProduce(0F);
        raw_poultry
                .setTranslationKeys("item.chickenRaw", "item.raw_fillet", "item.raw_gizzard")
                .setTextures(
                        new ResourceLocation("minecraft:chicken_raw"),
                        new ResourceLocation(MiTERenewed.RESOURCE_ID + "food/raw_fillet"),
                        new ResourceLocation(MiTERenewed.RESOURCE_ID + "food/raw_gizzard"))
                .setData(
                        foodData(3, 3, 0, HAS_PROTEIN),
                        foodData(2, 1, 0, HAS_PROTEIN),
                        foodData(2, 4, 0, HAS_PROTEIN))
                .setEffect(
                        foodEffect(Potion.poison.id, .3F, 400),
                        foodEffect(Potion.poison.id, .3F, 400),
                        foodEffect(Potion.poison.id, .6F, 200))
                .setAnimalProduct()
                .setCraftingDifficultyAsComponent(100F)
                .setLowestCraftingDifficultyToProduce(0F);
        cooked_poultry
                .setTranslationKeys("item.chickenCooked", "item.cooked_fillet", "item.cooked_gizzard")
                .setTextures(
                        new ResourceLocation("minecraft:chicken_cooked"),
                        new ResourceLocation(MiTERenewed.RESOURCE_ID + "food/cooked_fillet"),
                        new ResourceLocation(MiTERenewed.RESOURCE_ID + "food/cooked_gizzard"))
                .setData(
                        foodData(6, 6, 0, HAS_PROTEIN),
                        foodData(3, 3, 0, HAS_PROTEIN),
                        foodData(4, 10, 0, HAS_PROTEIN))
                .setXP(4, 2, 4)
                .setAnimalProduct()
                .setCraftingDifficultyAsComponent(100F)
                .setLowestCraftingDifficultyToProduce(0F);
        raw_lambchop
                .setTranslationKeys("item.lambchopRaw", "item.raw_mutton_chops")
                .setTextures(
                        new ResourceLocation("minecraft:food/lambchop_raw"),
                        new ResourceLocation(MiTERenewed.RESOURCE_ID + "food/raw_mutton_chops"))
                .setData(
                        foodData(3, 3, 0, HAS_PROTEIN),
                        foodData(2, 1, 0, HAS_PROTEIN))
                .setAnimalProduct()
                .setCraftingDifficultyAsComponent(100F)
                .setLowestCraftingDifficultyToProduce(0F);
        cooked_lambchop
                .setTranslationKeys("item.lambchopCooked", "item.cooked_mutton_chops")
                .setTextures(
                        new ResourceLocation("minecraft:food/lambchop_cooked"),
                        new ResourceLocation(MiTERenewed.RESOURCE_ID + "food/cooked_mutton_chops"))
                .setData(
                        foodData(6, 6, 0, HAS_PROTEIN),
                        foodData(3, 3, 0, HAS_PROTEIN))
                .setXP(4, 2)
                .setAnimalProduct()
                .setCraftingDifficultyAsComponent(100F)
                .setLowestCraftingDifficultyToProduce(0F);

        ItemRenewedFood.setRelations(raw_poultry, cooked_poultry);
        ItemRenewedFood.setRelations(raw_beef, cooked_beef);
        ItemRenewedFood.setRelations(raw_pork, cooked_pork);
        ItemRenewedFood.setRelations(raw_lambchop, cooked_lambchop);
    }
}
