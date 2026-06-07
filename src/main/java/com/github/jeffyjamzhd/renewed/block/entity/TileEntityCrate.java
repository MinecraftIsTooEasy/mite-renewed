package com.github.jeffyjamzhd.renewed.block.entity;

import com.github.jeffyjamzhd.renewed.block.BlockCrate;
import com.github.jeffyjamzhd.renewed.handler.RenewedTileEntityData;
import net.minecraft.*;

public class TileEntityCrate extends TileEntity implements IInventory {
    private int storageCapacity;
    public short heldItemID;
    public short heldItemMeta;
    public short heldItemCount;

    public TileEntityCrate() {
        this.storageCapacity = 0;
    }

    public TileEntityCrate(BlockCrate crate) {
        this.storageCapacity = crate.getCapacity();
    }

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        super.writeToNBT(tag);
        tag.setInteger("HeldItemID", this.heldItemID);
        tag.setInteger("HeldItemMeta", this.heldItemMeta);
        tag.setInteger("HeldItemCount", this.heldItemCount);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        super.readFromNBT(tag);
        this.heldItemID = (short) tag.getInteger("HeldItemID");
        this.heldItemMeta = (short) tag.getInteger("HeldItemMeta");
        this.heldItemCount = (short) tag.getInteger("HeldItemCount");
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        this.writeToNBT(nbt);
        return new Packet132TileEntityData(this.xCoord, this.yCoord, this.zCoord, RenewedTileEntityData.CRATE_PACKET_ID, nbt);
    }

    // Unique methods

    private ItemStack createStackFromData() {
        if (!canExtract()) {
            return null;
        }

        Item item = Item.itemsList[heldItemID];
        int stackLimit = item.getItemStackLimit(heldItemMeta, 0);

        return new ItemStack(heldItemID, Math.min(heldItemCount, stackLimit), heldItemMeta);
    }

    public ItemStack extractStack(int amount) {
        ItemStack stack = createStackFromData();
        if (stack != null && !isEmpty()) {
            this.onInventoryChanged();

            this.heldItemCount = (short) Math.max(0, this.heldItemCount - amount);
            if (this.heldItemCount == 0) {
                ensureEmpty();
            }

            // Sync the extraction to the client
            this.onInventoryChanged();
            if (this.worldObj != null) {
                this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
            }

            return stack.splitStack(amount);
        }
        return null;
    }

    public ItemStack insertStack(ItemStack stack) {
        if (!isItemValidForSlot(0, stack)) {
            return stack;
        }

        if (isEmpty()) {
            this.heldItemID = (short) stack.itemID;
            this.heldItemMeta = (short) stack.getItemSubtype();
        }

        short toTake = (short) Math.min(this.getStorageCapacity() - this.heldItemCount, stack.stackSize);
        if (stack.stackSize - toTake == 0) {
            stack = null;
        }
        this.heldItemCount += toTake;

        // Tell the chunk data changed, and tell the world to sync to clients
        this.onInventoryChanged();
        if (this.worldObj != null) {
            this.worldObj.markBlockForUpdate(this.xCoord, this.yCoord, this.zCoord);
        }

        return stack;
    }

    public int getStorageCapacity() {
        if (this.storageCapacity == 0) {
            if (!this.hasWorldObj() || !(this.getBlockType() instanceof BlockCrate)) {
                return this.storageCapacity = 0;
            }

            this.storageCapacity = ((BlockCrate) this.getBlockType()).getCapacity();
        }

        return this.storageCapacity;
    }

    private void ensureEmpty() {
        this.heldItemCount = 0;
        this.heldItemID = 0;
        this.heldItemMeta = 0;
    }

    public boolean isFull() {
        return this.heldItemCount == this.storageCapacity;
    }

    public boolean canExtract() {
        return !isEmpty();
    }

    public boolean isEmpty() {
        return this.heldItemCount == 0 && this.heldItemID == 0;
    }

    // Inventory interface

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public ItemStack getStackInSlot(int _unused) {
        return createStackFromData();
    }

    @Override
    public ItemStack decrStackSize(int _unused, int amount) {
        return extractStack(amount);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int _unused) {
        return null;
    }

    @Override
    public void setInventorySlotContents(int _unused, ItemStack itemStack) {
        insertStack(itemStack);
    }

    @Override
    public int getInventoryStackLimit() {
        return this.getStorageCapacity();
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player) {
        return this.worldObj.getBlockTileEntity(this.xCoord, this.yCoord, this.zCoord) == this && player.getDistanceSq((double) this.xCoord + (double) 0.5F, (double) this.yCoord + (double) 0.5F, (double) this.zCoord + (double) 0.5F) <= (double) 64.0F;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

    @Override
    public boolean isItemValidForSlot(int _unused, ItemStack stack) {
        if (stack == null) {
            return false;
        }

        boolean canBeDamaged = stack.getItem() instanceof IDamageableItem;
        boolean hasNBT = stack.hasTagCompound();
        if (canBeDamaged || hasNBT) {
            return false;
        }

        if (isEmpty()) {
            return true;
        }

        short itemID = (short) stack.getItem().itemID;
        short itemMeta = (short) stack.getItemSubtype();
        return itemID == this.heldItemID && itemMeta == this.heldItemMeta;
    }

    @Override
    public void destroyInventory() {
        ensureEmpty();
    }

}
