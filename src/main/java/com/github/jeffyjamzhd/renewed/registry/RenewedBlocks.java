package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.block.BlockRootingSugarCane;
import com.github.jeffyjamzhd.renewed.block.BlockSugarCane;
import huix.glacier.api.registry.MinecraftRegistry;
import net.minecraft.Block;
import net.xiaoyu233.fml.reload.utils.IdUtil;

public class RenewedBlocks {
    public static Block sugarCane = new BlockSugarCane(IdUtil.getNextBlockID());
    public static Block rootingSugarCane = new BlockRootingSugarCane(IdUtil.getNextBlockID());

    public static void register(MinecraftRegistry registry) {
        registry.registerBlock(sugarCane, "reeds");
        registry.registerBlock(rootingSugarCane, "reeds");
    }
}
