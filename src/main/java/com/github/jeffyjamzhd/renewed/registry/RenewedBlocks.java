package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.block.BlockCrate;
import com.github.jeffyjamzhd.renewed.block.BlockRootingSugarCane;
import com.github.jeffyjamzhd.renewed.block.BlockSugarCane;
import com.github.jeffyjamzhd.renewed.block.entity.TileEntityCrate;
import huix.glacier.api.registry.MinecraftRegistry;
import moddedmite.rustedironcore.api.util.IdUtilExtra;
import net.minecraft.Block;
import net.minecraft.Material;
import net.xiaoyu233.fml.reload.event.TileEntityRegisterEvent;
import net.xiaoyu233.fml.reload.utils.IdUtil;

public class RenewedBlocks {
    public static Block sugarCane = new BlockSugarCane(next());
    public static Block rootingSugarCane = new BlockRootingSugarCane(next());

    public static Block crateCopper = new BlockCrate(next(), Material.copper);
    public static Block crateSilver = new BlockCrate(next(), Material.silver);
    public static Block crateGold = new BlockCrate(next(), Material.gold);
    public static Block crateIron = new BlockCrate(next(), Material.iron);
    public static Block crateAncientMetal = new BlockCrate(next(), Material.ancient_metal);
    public static Block crateMithril = new BlockCrate(next(), Material.mithril);
    public static Block crateAdamantium = new BlockCrate(next(), Material.adamantium);

    public static void register(MinecraftRegistry registry) {
        registry.registerBlock(sugarCane, "reeds");
        registry.registerBlock(rootingSugarCane, "reeds");

        registry.registerBlock(crateCopper,         "");
        registry.registerBlock(crateSilver,         "");
        registry.registerBlock(crateGold,           "");
        registry.registerBlock(crateIron,           "");
        registry.registerBlock(crateAncientMetal,   "");
        registry.registerBlock(crateMithril,        "");
        registry.registerBlock(crateAdamantium,     "");
    }

    public static void registerTileEntities(TileEntityRegisterEvent event) {
        event.register(TileEntityCrate.class, MiTERenewed.RESOURCE_ID + "Crate");
    }

    private static int next() {
        return IdUtil.getNextBlockID();
    }
}
