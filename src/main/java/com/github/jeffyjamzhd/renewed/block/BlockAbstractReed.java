package com.github.jeffyjamzhd.renewed.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;

public class BlockAbstractReed extends Block {
    protected BlockAbstractReed(int id) {
        super(id, Material.plants, new BlockConstants().setNeverHidesAdjacentFaces().setNotAlwaysLegal());
        this.setBlockBoundsForAllThreads(0.125F, 0.0F, 0.125F, 0.875F, 1F, 0.875F);
        this.setTickRandomly(true);
        this.setHardness(0.1F);
        this.setStepSound(soundGrassFootstep);
        this.disableStats();
    }

    @Override
    public boolean canOccurAt(World world, int x, int y, int z, int metadata) {
        return world.canBlockSeeTheSky(x, y + 1, z) && super.canOccurAt(world, x, y, z, metadata);
    }

    @Override
    public boolean isLegalAt(World world, int x, int y, int z, int metadata) {
        if (!super.isLegalAt(world, x, y, z, metadata)) {
            return false;
        }
        if (world.getBlock(x, y - 1, z) instanceof BlockAbstractReed) {
            --y;
            int height = 1;
            while (world.getBlock(x, --y, z) instanceof BlockAbstractReed) {
                ++height;
            }
            return height < 3;
        }
        return world.getNeighborBlockMaterial(x, --y, z, EnumFace.NORTH) == Material.water
                || world.getNeighborBlockMaterial(x, y, z, EnumFace.EAST) == Material.water
                || world.getNeighborBlockMaterial(x, y, z, EnumFace.SOUTH) == Material.water
                || world.getNeighborBlockMaterial(x, y, z, EnumFace.WEST) == Material.water;
    }

    @Override
    public boolean isLegalOn(int metadata, Block block_below, int block_below_metadata) {
        if (block_below instanceof BlockAbstractReed) {
            return true;
        }
        return isOnValidBlock(block_below);
    }

    @Override
    public int dropBlockAsEntityItem(BlockBreakInfo info) {
        if (info.wasNotLegal() || info.wasExploded()) {
            info.world.destroyBlock(info, false, true);
        }
        if (info.wasExploded() || info.wasCrushed()) {
            return 0;
        }
        return this.dropBlockAsEntityItem(info, this.itemToDrop(info));
    }

    @Override
    public boolean canBePlacedOnBlock(int metadata, Block block_below, int block_below_metadata, double block_below_bounds_max_y) {
        return block_below instanceof BlockAbstractReed
                || super.canBePlacedOnBlock(metadata, block_below, block_below_metadata, block_below_bounds_max_y);
    }

    @Override
    public boolean canBeCarried() {
        return false;
    }

    @Override
    public int getRenderType() {
        return 1;
    }

    @Override
    public int idPicked(World world, int x, int y, int z) {
        return Item.reed.itemID;
    }

    @Override
    public boolean isSolid(boolean[] is_solid, int metadata) {
        return false;
    }

    @Override
    public boolean isStandardFormCube(boolean[] is_standard_form_cube, int metadata) {
        return false;
    }

    @Override
    public boolean blocksFluids(boolean[] blocks_fluids, int metadata) {
        return false;
    }

    /*---- Class Specific Methods ----*/

    public boolean isRootCane(IBlockAccess access, int x, int y, int z) {
        return isOnValidBlock(access, x, y, z);
    }

    /**
     * {@code true} if arg is a valid block
     */
    public boolean isOnValidBlock(Block blockBelow) {
        return blockBelow == grass || blockBelow == dirt || blockBelow == sand;
    }

    public boolean isOnValidBlock(IBlockAccess access, int x, int y, int z) {
        return isOnValidBlock(access.getBlock(x, y - 1, z));
    }

    public Item itemToDrop(BlockBreakInfo info) {
        return Item.reed;
    }

    // ---- CLIENT METHODS -----


    @Override
    public boolean mr$useBlockRenderAPI() {
        return true;
    }

    @Override
    public boolean mr$renderBlock(RenderBlocks renderer, IBlockAccess accessor, int x, int y, int z) {
        renderReed(renderer, accessor, x, y, z, true);
        return true;
    }

    @Override
    public void mr$renderBlockBreaking(RenderBlocks renderer, IBlockAccess accessor, int x, int y, int z) {
        renderReed(renderer, accessor, x, y, z, false);
    }

    @Environment(EnvType.CLIENT)
    private void renderReed(RenderBlocks renderer, IBlockAccess accessor, int x, int y, int z, boolean renderInner) {
        Tessellator tessellator = Tessellator.instance;

        int brightness = this.getMixedBrightnessForBlock(accessor, x, y, z);
        int color = this.colorMultiplier(accessor, x, y, z);
        float r = (float)(color >> 16 & 255) / 255F;
        float g = (float)(color >> 8 & 255) / 255F;
        float b = (float)(color & 255) / 255F;
        tessellator.setBrightness(brightness);

        tessellator.setColorOpaque_F(r * .6F, g * .6F, b * 0.6F);
        renderer.setRenderBounds(2F / 16F, 0F, 0F, 14F / 16F, 1F, 1F);
        renderer.renderFaceXNeg(this, x, y, z, this.getBlockTexture(accessor, x, y, z, 4)); // West facing out
        renderer.renderFaceXPos(this, x, y, z, this.getBlockTexture(accessor, x, y, z, 5)); // East facing out

        if (renderInner) {
            renderer.setRenderBounds(0F, 0F, 0F, 2F / 16F, 1F, 1F);
            renderer.renderFaceXPos(this, x, y, z, this.getBlockTexture(accessor, x, y, z, 4));
            renderer.setRenderBounds(14F / 16F, 0F, 0F, 1F, 1F, 1F);
            renderer.renderFaceXNeg(this, x, y, z, this.getBlockTexture(accessor, x, y, z, 5));
        }

        tessellator.setColorOpaque_F(r * .8F, g * .8F, b * .8F);
        renderer.setRenderBounds(0F, 0F, 2F / 16F, 1F, 1F, 14F / 16F);
        renderer.renderFaceZNeg(this, x, y, z, this.getBlockTexture(accessor, x, y, z, 2)); // North facing out
        renderer.renderFaceZPos(this, x, y, z, this.getBlockTexture(accessor, x, y, z, 3)); // South facing out

        if (renderInner) {
            renderer.setRenderBounds(0F, 0F, 0F, 1F, 1F, 2F / 16F);
            renderer.renderFaceZPos(this, x, y, z, this.getBlockTexture(accessor, x, y, z, 2));
            renderer.setRenderBounds(0F, 0F, 14F / 16F, 1F, 1F, 1F);
            renderer.renderFaceZNeg(this, x, y, z, this.getBlockTexture(accessor, x, y, z, 3));
        }
    }
}
