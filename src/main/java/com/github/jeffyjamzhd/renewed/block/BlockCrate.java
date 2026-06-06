package com.github.jeffyjamzhd.renewed.block;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.block.entity.TileEntityCrate;
import com.github.jeffyjamzhd.renewed.registry.RenewedSounds;
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
        if (!canAccess(world, x, y, z)) {
            world.playSoundAtBlock(x, y, z, "imported.random.chest_locked", 0.2F);
            return true;
        }

        TileEntityCrate te = (TileEntityCrate) world.getBlockTileEntity(x, y, z);

        if (te.isFull()) {
            return false;
        }

        if (player.getHeldItemStack() != null) {
            // Insert held stack
            if (player.onServer()) {
                ItemStack stack = player.getHeldItemStack();
                player.setHeldItemStack(te.insertStack(stack));
                playInsertSfx(world, x, y, z);
            }

            return true;
        } else if (!te.isEmpty()) {
            // Attempt to insert matching stacks from player inventory
            InventoryPlayer inventory = player.inventory;
            int size = inventory.getSizeInventory();
            boolean hasInserted = false;

            if (!player.onServer()) {
                return true;
            }

            for (int i = 0; i < size; i++) {
                ItemStack stack = inventory.getStackInSlot(i);
                if (stack == null) continue;

                if (te.isItemValidForSlot(0, stack)) {
                    ItemStack result = te.insertStack(stack);
                    inventory.setInventorySlotContents(i, result);
                    hasInserted = true;

                    // If we have remaining items, the container has filled to max capacity
                    if (result != null) {
                        break;
                    }
                }
            }

            player.inventoryContainer.detectAndSendChanges();
            if (hasInserted) playInsertSfx(world, x, y, z);
            return true;
        }
        return false;
    }

    @Override
    public void onBlockClicked(World par1World, int par2, int par3, int par4, EntityPlayer par5EntityPlayer) {
        super.onBlockClicked(par1World, par2, par3, par4, par5EntityPlayer);
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

    public boolean canAccess(World world, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        EnumDirection direction = getDirectionFacing(meta);
        return world.isAirOrPassableBlock(direction.getNeighborX(x), direction.getNeighborY(y), direction.getNeighborZ(z), false);
    }

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

    private void playInsertSfx(World world, int x, int y, int z) {
        world.playSoundAtBlock(x, y, z, RenewedSounds.HANDPAN_INSERT.toString(), 0.3F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
    }

    private void playExtractSfx(World world, int x, int y, int z) {
        world.playSoundAtBlock(x, y, z, "random.pop", 0.3F, ((world.rand.nextFloat() - world.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
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
