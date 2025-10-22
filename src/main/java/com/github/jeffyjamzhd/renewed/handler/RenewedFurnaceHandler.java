package com.github.jeffyjamzhd.renewed.handler;

import com.github.jeffyjamzhd.renewed.item.ItemRenewedBucket;
import moddedmite.rustedironcore.api.event.listener.IFurnaceUpdateListener;
import net.minecraft.ItemStack;
import net.minecraft.TileEntityFurnace;

public class RenewedFurnaceHandler implements IFurnaceUpdateListener {
    @Override
    public void onFurnaceFuelConsumed(TileEntityFurnace furnace) {
        ItemStack fuelStack = furnace.getFuelItemStack();
        if (fuelStack != null && fuelStack.getItem() instanceof ItemRenewedBucket bucket) {
            if (!bucket.getsDamaged()) {
                return;
            }
            fuelStack.tryDamageItem(furnace.getWorldObj(), 1, false);
            if (fuelStack.getItemDamage() >= fuelStack.getMaxDamage()) {
                furnace.getWorldObj()
                        .playSoundAtBlock(furnace.xCoord, furnace.yCoord, furnace.zCoord, "random.break", 0.5F, 1.0F);
                furnace.setInventorySlotContents(1, null);
            }
        }
    }
}
