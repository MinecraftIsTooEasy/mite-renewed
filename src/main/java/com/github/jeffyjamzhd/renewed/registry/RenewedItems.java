package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
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

    // Called upon register event
    public static void register(ItemRegistryEvent registry) {
        registerItem(registry, "sharp_bone", "tool/sharp_bone", sharp_bone).setCreativeTab(CreativeTabs.tabTools);
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
}
