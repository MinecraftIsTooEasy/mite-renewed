package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.item.ItemHandpan;
import com.github.jeffyjamzhd.renewed.item.ItemPolearm;
import net.minecraft.*;
import net.xiaoyu233.fml.reload.event.ItemRegistryEvent;
import net.xiaoyu233.fml.reload.utils.IdUtil;

import static net.xiaoyu233.fml.util.ReflectHelper.createInstance;
import static com.github.jeffyjamzhd.renewed.MiTERenewed.LOGGER;

public class RenewedItems {
    // Item definitions
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
    }
}
