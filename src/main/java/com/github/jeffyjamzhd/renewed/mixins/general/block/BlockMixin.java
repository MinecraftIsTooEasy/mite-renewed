package com.github.jeffyjamzhd.renewed.mixins.general.block;

import com.github.jeffyjamzhd.renewed.api.IBlock;
import net.minecraft.Block;
import net.minecraft.EntityPlayer;
import net.minecraft.EnumFace;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Block.class)
public abstract class BlockMixin implements IBlock {
    @Shadow
    public abstract void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer);

    @Override
    public float mr$getBlockHardness(World world, int x, int y, int z, int metadata) {
        return ((Block) (Object) this).getBlockHardness(metadata);
    }

    @Override
    public boolean mr$onBlockClicked(World world, EnumFace face, int x, int y, int z, EntityPlayer player) {
        onBlockClicked(world, x, y, z, player);
        return false;
    }
}
