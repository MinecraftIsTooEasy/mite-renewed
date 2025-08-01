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
            (ItemKnife) createInstance(ItemKnife.class, new Class[]{int.class, Material.class}, IdUtil.getNextItemID(), RenewedMaterial.bone);

    private static void registerItem(ItemRegistryEvent registry, String name, String resource, Item item, CreativeTabs tab) {
        registry.register(MiTERenewed.NAMESPACE, MiTERenewed.RESOURCE_ID + resource, name, item, tab);
    }

    // Called upon register event
    public static void register(ItemRegistryEvent registry) {
        registerItem(registry, "sharp_bone", "tool/sharp_bone", sharp_bone, CreativeTabs.tabTools);
    }
}
