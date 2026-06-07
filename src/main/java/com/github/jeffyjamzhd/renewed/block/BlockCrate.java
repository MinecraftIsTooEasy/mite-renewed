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
        setCreativeTab(CreativeTabs.tabDecorations);
        setHardness(1.6F);
    }

    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileEntityCrate(this);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
                                    EnumFace face, float offset_x, float offset_y, float offset_z) {
        if (!canInteract(world, face, x, y, z)) {
            return false;
        }

        TileEntityCrate te = (TileEntityCrate) world.getBlockTileEntity(x, y, z);

        // Block insertion if full
        if (te.isFull()) {
            return false;
        }

        if (player.getHeldItemStack() != null) {
            // Insert held stack
            if (player.onServer()) {
                ItemStack stack = player.getHeldItemStack();
                ItemStack result = te.insertStack(stack);

                if (!stack.equals(result)) {
                    player.setHeldItemStack(result);
                    playInsertSfx(world, x, y, z);
                    return true;
                }
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
    public boolean mr$onBlockClicked(World world, EnumFace face, int x, int y, int z, EntityPlayer player) {
        if (!canInteract(world, face, x, y, z)) {
            return false;
        }

        TileEntityCrate te = (TileEntityCrate) world.getBlockTileEntity(x, y, z);

        // Block extraction if empty
        if (te.isEmpty()) {
            return false;
        }

        // Extract one item
        if (player.onServer()) {
            // Do not extract if inventory full
            if (getFirstCompatibleInventorySlot(player.inventory, te.createStackFromData()) == -1) {
                player.addChatMessage("Inventory is full, cannot take any more items.");
                return false;
            }

            ItemStack extract = te.extractStack();
            if (extract != null) {
                player.inventory.addItemStackToInventory(extract);
                if (extract.stackSize > 0) {
                    te.insertStack(extract);
                }

                playExtractSfx(world, x, y, z);
                player.inventoryContainer.detectAndSendChanges();
            }

            return true;
        }
        return true;
    }

    @Override
    public boolean mr$useExtendedAPI() {
        // Required for server side block clicking
        return true;
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

    @Override
    public boolean isStandardFormCube(boolean[] is_standard_form_cube, int metadata) {
        return false;
    }

    // Class specific

    public boolean canInteract(World world, EnumFace face, int x, int y, int z) {
        int meta = world.getBlockMetadata(x, y, z);
        EnumDirection direction = getDirectionFacing(meta);

        boolean openFront = world.isAirOrPassableBlock(direction.getNeighborX(x), direction.getNeighborY(y), direction.getNeighborZ(z), false);
        boolean touchingFront = direction.equals(face.getNormal());
        return openFront && touchingFront;
    }

    public int getFirstCompatibleInventorySlot(IInventory inventory, ItemStack stack) {
        int size = inventory.getSizeInventory();
        for (int i = 0; i < 36; i++) {
            ItemStack stackAt = inventory.getStackInSlot(i);

            if (stackAt == null) return i;

            boolean sameID = stack.itemID == stackAt.itemID;
            boolean sameMeta = stack.getItemSubtype() == stackAt.getItemSubtype();
            boolean canStack = stackAt.stackSize < stackAt.getMaxStackSize();
            if (sameID && sameMeta && canStack) return i;
        }
        return -1;
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
