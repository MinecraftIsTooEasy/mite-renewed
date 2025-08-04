package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.item.ItemPolearm;
import huix.glacier.api.entrypoint.IGameRegistry;
import huix.glacier.api.registry.MinecraftRegistry;
import net.minecraft.CreativeTabs;
import net.minecraft.Item;
import net.minecraft.ItemKnife;
import net.minecraft.Material;
import net.xiaoyu233.fml.reload.event.ItemRegistryEvent;
import net.xiaoyu233.fml.reload.utils.IdUtil;

import static net.xiaoyu233.fml.util.ReflectHelper.createInstance;


public class RenewedItems {
    // Item definitions
    public static final ItemKnife sharp_bone =
            createInstance(ItemKnife.class, new Class[]{int.class, Material.class}, IdUtil.getNextItemID(), RenewedMaterial.bone);
    public static final Item tangled_web =
            createInstance(Item.class, new Class[]{int.class, Material.class, String.class}, IdUtil.getNextItemID(), Material.silk, "tangled_web");
    public static final ItemPolearm flint_spear =
            createInstance(ItemPolearm.class, new Class[]{int.class, Material.class}, IdUtil.getNextItemID(), Material.flint);
    public static final ItemPolearm bone_spear =
            createInstance(ItemPolearm.class, new Class[]{int.class, Material.class}, IdUtil.getNextItemID(), RenewedMaterial.bone);

    // Called upon register event
    public static void register(ItemRegistryEvent registry) {
        registerItem(registry, "sharp_bone", "tool/sharp_bone", sharp_bone).setCreativeTab(CreativeTabs.tabTools);
        registerItem(registry, "tangled_web", "tangled_web", tangled_web).setCreativeTab(CreativeTabs.tabMisc);
        registerItem(registry, "flint_spear", "tool/polearm/flint_spear", flint_spear);
        registerItem(registry, "bone_spear", "tool/polearm/bone_spear", bone_spear);
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
        sharp_bone.setLowestCraftingDifficultyToProduce(200.0F);
        tangled_web.setLowestCraftingDifficultyToProduce(50.0F);
    }
}
