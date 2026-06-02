package com.github.jeffyjamzhd.renewed.mixins.general.block;

import com.github.jeffyjamzhd.renewed.api.IBlock;
import net.minecraft.Block;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
public abstract class BlockMixin implements IBlock {
    @Override
    public float mr$getBlockHardness(World world, int x, int y, int z, int metadata) {
        return ((Block) (Object) this).getBlockHardness(metadata);
    }
}
