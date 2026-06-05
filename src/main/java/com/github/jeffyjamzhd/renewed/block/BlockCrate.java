package com.github.jeffyjamzhd.renewed.block;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.block.entity.TileEntityCrate;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;

public class BlockCrate extends BlockDirectionalWithTileEntity {
    public BlockCrate(int id, Material material) {
        super(id, material, new BlockConstants());
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityCrate(this);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
                                    EnumFace face, float offset_x, float offset_y, float offset_z) {
        if (!world.isAirOrPassableBlock(x, y + 1, z, false)) {
            return false;
        }

        if (player.getHeldItemStack() != null) {
            if (player.onServer()) {
                ItemStack stack = player.getHeldItemStack();
                TileEntityCrate te = (TileEntityCrate) world.getBlockTileEntity(x, y, z);
                player.setHeldItemStack(te.insertStack(stack));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean playerSwingsOnBlockActivated(boolean empty_handed) {
        return true;
    }

    @Override
    public EnumDirection getDirectionFacing(int meta) {
        return this.getDirectionFacingStandard4(meta);
    }

    @Override
    public int getMetadataForDirectionFacing(int metadata, EnumDirection direction) {
        return direction.isNorth() ? 2 : (direction.isSouth() ? 3 : (direction.isWest() ? 4 : (direction.isEast() ? 5 : -1)));
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
    public boolean canBeReplacedBy(int metadata, Block other_block, int other_block_metadata) {
        return false;
    }

    // Class specific

    public int getCapacity() {
        if (this.blockMaterial == Material.copper) return 256;
        if (this.blockMaterial == Material.silver) return 256;
        if (this.blockMaterial == Material.gold) return 256;
        if (this.blockMaterial == Material.iron) return 512;
        if (this.blockMaterial == Material.ancient_metal) return 1024;
        if (this.blockMaterial == Material.mithril) return 2048;
        if (this.blockMaterial == Material.adamantium) return 4096;
        return 256;
    }

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
