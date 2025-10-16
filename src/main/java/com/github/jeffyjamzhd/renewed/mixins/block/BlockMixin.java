package com.github.jeffyjamzhd.renewed.mixins.block;

import com.github.jeffyjamzhd.renewed.api.IBlock;
import net.minecraft.Block;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Block.class)
public class BlockMixin implements IBlock {
    @Override
    public float mr$getBlockHardness(World world, int x, int y, int z, int metadata) {
        return ((Block) (Object) this).getBlockHardness(metadata);
    }
}
