package com.github.jeffyjamzhd.renewed.block;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;

public class BlockCrate extends BlockDirectionalWithTileEntity {
    private static final int BITMASK_DIRECTION = 0b1100;

    public BlockCrate(int id, Material material) {
        super(id, material, new BlockConstants());
    }

    @Override
    public String getMetadataNotes() {
        return "Bits 1, 2 and 4 are used for horizontal orientation. Bit 8 is unused.";
    }

    @Override
    public boolean isValidMetadata(int metadata) {
        return metadata >= 2 && metadata < 6;
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return null;
    }

    @Override
    public EnumDirection getDirectionFacing(int meta) {
        return this.getDirectionFacingStandard4(meta);
    }

    @Override
    public boolean canBeReplacedBy(int metadata, Block other_block, int other_block_metadata) {
        return false;
    }

    @Override
    public int getMetadataForDirectionFacing(int metadata, EnumDirection direction) {
        return direction.isNorth() ? 2 : (direction.isSouth() ? 3 : (direction.isWest() ? 4 : (direction.isEast() ? 5 : -1)));
    }

    // Class specific

    private String getFormattedMetalName() {
        return this.blockMaterial.getCapitalizedName().replace(" ", "");
    }


    // Client methods

    private Icon crateFront;
    public Icon crateFrame;

    @Override
    @Environment(EnvType.CLIENT)
    public void registerIcons(IconRegister register) {
        String framePath = "crate/frame" + getFormattedMetalName();
        this.blockIcon = register.registerIcon(MiTERenewed.RESOURCE_ID + "crate/panelSide");
        this.crateFront = register.registerIcon(MiTERenewed.RESOURCE_ID + "crate/panelFront");
        this.crateFrame = register.registerIcon(MiTERenewed.RESOURCE_ID + framePath);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public Icon getIcon(int side, int meta) {
        if (EnumFace.get(side).getNormal() == this.getDirectionFacing(meta)) {
            return this.crateFront;
        }
        return this.blockIcon;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public String getUnlocalizedName() {
        return "tile.crate%s".formatted(this.getFormattedMetalName());
    }

    @Override
    @Environment(EnvType.CLIENT)
    protected String getTextureName() {
        return this.blockIcon.getIconName();
    }
}
