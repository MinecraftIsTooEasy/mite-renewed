package com.github.jeffyjamzhd.renewed.block;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;

import java.util.Random;

public class BlockSugarCane extends BlockAbstractReed {
    public BlockSugarCane(int id) {
        super(id);
    }

    @Override
    public String getMetadataNotes() {
        return "All bits used to track growth.";
    }

    @Override
    public boolean isValidMetadata(int metadata) {
        return metadata >= 0 && metadata < 16;
    }

    @Override
    public boolean updateTick(World world, int x, int y, int z, Random random) {
        if (super.updateTick(world, x, y, z, random)) {
            return true;
        }
        if (random.nextFloat() < 0.2F) {
            return false;
        }
        if (world.getBlockLightValue(x, y, z) < 15) {
            return false;
        }
        if (this.canOccurAt(world, x, y + 1, z, 0)) {
            int meta = world.getBlockMetadata(x, y, z);
            if (++meta == 16) {
                world.setBlock(x, y + 1, z, this.blockID);
                meta = 0;
            }
            world.setBlockMetadataWithNotify(x, y, z, meta, 4);
            return true;
        }
        return false;
    }

    @Override
    public boolean canBeReplacedBy(int metadata, Block other_block, int other_block_metadata) {
        return false;
    }

    @Override
    public float mr$getBlockHardness(World world, int x, int y, int z, int metadata) {
        return isRootCane(world, x, y, z) ? 0.2F : 0.07F;
    }

    /*---- Client Methods ----*/

    private Icon rootIcon;

    @Override
    @Environment(EnvType.CLIENT)
    public void registerIcons(IconRegister register) {
        super.registerIcons(register);
        rootIcon = register.registerIcon(MiTERenewed.RESOURCE_ID + "reeds_root");
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Icon getBlockTexture(IBlockAccess access, int x, int y, int z, int side) {
        return isRootCane(access, x, y, z) ? rootIcon : blockIcon;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean mr$useSpecialCrossedRenderer() {
        return true;
    }
}
