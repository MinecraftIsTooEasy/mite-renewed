package com.github.jeffyjamzhd.renewed.api.inventory;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.api.item.IItemStackInventory;
import com.github.jeffyjamzhd.renewed.util.ItemUtils;
import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;
import net.minecraft.NBTTagCompound;
import net.minecraft.NBTTagList;

import java.util.ArrayList;

public class BackpackInventory implements IItemStackInventory {
    protected final int size;
    public ArrayList<ItemStack> inventory;
    public int currentSlotID;

    public BackpackInventory(ItemStack stack, int inventorySize) {
        // Create tag compound if one doesn't exist
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }

        // Prepare inventory
        NBTTagCompound compound = stack.getTagCompound();
        this.size = inventorySize;
        this.inventory = new ArrayList<>();

        // Read stack NBT
        readFromNBT(compound);
        this.currentSlotID = readCurrentSlotFromNBT(compound);
    }

    public BackpackInventory(NBTTagCompound tag, int inventorySize) {
        // Prepare inventory
        this.size = inventorySize;
        this.inventory = new ArrayList<>();

        // Read stack NBT
        readFromNBT(tag);
        this.currentSlotID = readCurrentSlotFromNBT(tag);
    }

    @Override
    public void onInventoryChanged() {
        for (int index = 0; index < inventory.size(); index++) {
            ItemStack at = getStackInSlot(index);
            if (at != null && at.stackSize == 0) {
                setInventorySlotContents(index, null);
            }
        }
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        // Don't do anything if there's no stack
        if (index >= inventory.size()) {
            return null;
        } else if (inventory.get(index) == null) {
            return null;
        }

        // Prepare for split
        ItemStack at = inventory.get(index);
        ItemStack split;

        // Split if count is below stack size, otherwise
        // null out stack in inventory and return it
        if (inventory.get(index).stackSize > count) {
            split = at.splitStack(count);
        } else {
            split = at;
            inventory.set(index, null);
        }
        return split;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int index) {
        ItemStack stack = getStackInSlot(index);
        this.setInventorySlotContents(index, null);
        return stack;
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (0 <= index && index < inventory.size())
            inventory.set(index, stack);
    }

    @Override
    public int getSizeInventory() {
        return Math.min(inventory.size(), size);
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (inventory.isEmpty() || index < 0 || index >= inventory.size()) {
            return null;
        }
        return inventory.get(index);
    }

    //***       Class specific methods        ***//

    /**
     * Gets first {@link ItemStack} in inventory, while also
     * removing it from this inventory
     */
    public ItemStack popFirstStack() {
        if (!inventory.isEmpty()) {
            ItemStack stack = getFirstStack();
            inventory.remove(inventory.size() - 1);
            currentSlotID = inventory.size() - 1;
            return stack;
        }
        return null;
    }

    public ItemStack getFirstStack() {
        if (!inventory.isEmpty()) {
            return inventory.get(inventory.size() - 1);
        }
        return null;
    }

    public ItemStack putStack(ItemStack stack) {
        inventory.add(stack);
        return null;
    }

    /**
     * Puts an {@link ItemStack} at {@code slot}.
     * Handles stack merging when applicable.
     */
    public ItemStack putStackAt(ItemStack stack, int slot) {
        ItemStack slotStack = getStackInSlot(slot);

        if (slotStack != null && ItemUtils.areItemsEqual(slotStack, stack, false)) {
            // Attempt merge
            int countInSlot = slotStack.stackSize;
            int maxCount = slotStack.getMaxStackSize();
            int diff = Math.min(maxCount - countInSlot, stack.stackSize);

            setInventorySlotContents(slot, slotStack.setStackSize(slotStack.stackSize + diff));
            stack.stackSize -= diff;
        } else if (slotStack == null) {
            // Put stack in slot, splitting incase
            // the stack is over maximum
            setInventorySlotContents(slot, stack);
            return null;
        }

        return stack;
    }

    /**
     * Attempts to put an {@link ItemStack} into this
     * inventory, somewhat similar behavior to shift clicking
     */
    public ItemStack putStackSmart(ItemStack stack) {
        // Iterate inventory and look for an open slot
        // or matching stack
        for (int slot = 0; slot <= this.inventory.size(); slot++) {
            // Try to merge with existing stacks
            if (slot < this.inventory.size()) {
                ItemStack slotStack = getStackInSlot(slot);

                // Attempt to put stack in slot
                if (slotStack == null) {
                    stack = putStackAt(stack, slot);
                } else if (slotStack.itemID == stack.itemID) {
                    stack = putStackAt(stack, slot);
                }

                // No stack, return null
                if (stack == null)
                    return null;

                // Stack size under 0, return null
                if (stack.stackSize <= 0)
                    return null;
            }

            // Check for open slot
            if (slot == this.inventory.size() && this.inventory.size() < this.size) {
                // Attempt to put stack in slot
                stack = putStack(stack);
            }
        }

        // Not enough room, so return remainder
        return stack;
    }

    public int scrollCurrentSlotID(int scroll) {
        currentSlotID += scroll;
        if (currentSlotID >= inventory.size()) {
            currentSlotID = 0;
        }
        if (currentSlotID < 0) {
            currentSlotID = inventory.isEmpty() ? 0 : inventory.size() - 1;
        }
        return currentSlotID;
    }

    public String getRootTagString() {
        return "BackpackInventory";
    }

    public void readFromNBT(NBTTagCompound compound) {
        // Get inventory tag
        NBTTagCompound content = compound.getCompoundTag(getRootTagString());
        if (content == null)
            return;

        // Begin parsing item list
        NBTTagList list = content.getTagList("ItemStacks");
        for (int i = 0; i < list.tagCount() && i < size; i++) {
            // Get ItemStack entry
            NBTTagCompound entry = (NBTTagCompound) list.tagAt(i);
            int slot = entry.getInteger("Slot");

            // Attempt to parse ItemStack data
            try {
                inventory.add(ItemStack.loadItemStackFromNBT(entry));
            } catch (NullPointerException _npe) {
                MiTERenewed.LOGGER.info("Malformed item NBT data in backpack!! T_T");
            }
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        // Iterate through inventory
        NBTTagList list = new NBTTagList();
        for (int slot = 0; slot < this.inventory.size(); slot++) {
            ItemStack stack = this.inventory.get(slot);
            if (stack != null && stack.stackSize > 0) {
                // Write item data to NBT
                NBTTagCompound slotTag = new NBTTagCompound();
                list.appendTag(slotTag);
                slotTag.setInteger("Slot", slot);
                stack.writeToNBT(slotTag);
            }
        }

        NBTTagCompound backpack = new NBTTagCompound();
        backpack.setTag("ItemStacks", list);
        backpack.setInteger("CurrentSlotID", currentSlotID);
        backpack.setInteger("Size", size);
        compound.setTag(getRootTagString(), backpack);
        return compound;
    }

    /**
     * Gets inventory slot count from provided NBT compound.
     * If there is no tag/data, returns {@code orDefault}
     */
    private int readCurrentSlotFromNBT(NBTTagCompound compound) {
        // Get value from tag
        if (compound != null) {
            NBTTagCompound content = compound.getCompoundTag(getRootTagString());
            if (content != null) {
                // It has a set slot id, nice
                int slotID = content.getInteger("CurrentSlotID");
                if (inventory.isEmpty())
                    return 0;
                if (slotID >= inventory.size())
                    return inventory.size() - 1;
                return slotID;
            }
        }
        // No tag or value, return 0
        return 0;
    }


    public NBTTagCompound writeCurrentSlotToNBT(NBTTagCompound compound, int slotID) {
        if (compound != null) {
            NBTTagCompound content = compound.getCompoundTag(getRootTagString());
            if (content != null) {
                content.setInteger("CurrentSlotID", slotID);
            }
        }
        return compound;
    }


    //***       Unused interface methods        ***//

    @Override
    public boolean isUseableByPlayer(EntityPlayer entityPlayer) {
        return true;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemStack) {
        return true;
    }

    @Override
    public void destroyInventory() {

    }

    @Override
    public String getCustomNameOrUnlocalized() {
        return "";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public void openChest() {
    }

    @Override
    public void closeChest() {
    }

}