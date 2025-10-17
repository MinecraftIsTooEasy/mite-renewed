package com.github.jeffyjamzhd.renewed.block;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.registry.RenewedBlocks;
import net.minecraft.*;

import java.util.Random;

public class BlockRootingSugarCane extends BlockAbstractReed {
    private static final byte MASK_DAILY_GROWTH = 0b1000;
    private static final byte MASK_GROWTH = 0b0111;

    public BlockRootingSugarCane(int id) {
        super(id);
    }

    @Override
    public String getMetadataNotes() {
        return "First bit dictates if daily growth occurred, last 3 bits dictate growth count";
    }

    @Override
    public boolean isValidMetadata(int metadata) {
        return metadata >= 0 && metadata < 16;
    }

    @Override
    public boolean isLegalAt(World world, int x, int y, int z, int metadata) {
        if (!super.isLegalAt(world, x, y, z, metadata)) {
            return false;
        }
        return world.getNeighborBlockMaterial(x, --y, z, EnumFace.NORTH) == Material.water
                || world.getNeighborBlockMaterial(x, y, z, EnumFace.EAST) == Material.water
                || world.getNeighborBlockMaterial(x, y, z, EnumFace.SOUTH) == Material.water
                || world.getNeighborBlockMaterial(x, y, z, EnumFace.WEST) == Material.water;
    }

    @Override
    public boolean updateTick(World world, int x, int y, int z, Random random) {
        if (super.updateTick(world, x, y, z, random)) {
            return true;
        }
        int meta = world.getBlockMetadata(x, y, z);
        int dailyGrowth = (meta & MASK_DAILY_GROWTH) >> 3;
        boolean hasDaily = dailyGrowth == 1;

        // Only run during day
        if (world.isDaytime()) {
            // Do not continue if already grown today
            if (hasDaily) {
                return false;
            }

            int growth = (meta & MASK_GROWTH) + 1;
            if (random.nextFloat() < 0.8) {
                dailyGrowth = 1;
            }

            // Play FX
            world.blockFX(EnumBlockFX.manure, x, y, z);

            // Root if growth is good enough
            if (growth > 7) {
                world.setBlock(x, y, z, RenewedBlocks.sugarCane.blockID);
                world.blockFX(EnumBlockFX.valueOf("transform"), x, y, z, new SignalData().setInteger(this.blockID));
                return true;
            }

            // Set metadata
            meta = 0;
            meta |= growth;
            meta |= dailyGrowth << 3;
            world.setBlockMetadataWithNotify(x, y, z, meta, 4);
            return true;
        } else if (hasDaily) {
            // During night reset bit
            meta = meta & ~MASK_DAILY_GROWTH;
            world.setBlockMetadataWithNotify(x, y, z, meta, 4);
        }
        return true;
    }
}
