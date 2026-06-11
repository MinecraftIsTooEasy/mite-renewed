package com.github.jeffyjamzhd.renewed.api.item;

import net.minecraft.IInventory;
import net.minecraft.NBTTagCompound;

public interface IItemStackInventory extends IInventory {
    /**
     * Read from NBT data
     * @param compound NBT data from associated {@link net.minecraft.src.ItemStack}
     */
    void readFromNBT(NBTTagCompound compound);

    /**
     * Read from NBT data
     * @param compound NBT data from associated {@link net.minecraft.src.ItemStack}
     */
    NBTTagCompound writeToNBT(NBTTagCompound compound);
}
