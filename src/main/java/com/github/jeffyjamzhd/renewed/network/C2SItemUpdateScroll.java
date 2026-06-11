package com.github.jeffyjamzhd.renewed.network;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.item.ItemWithInventory;
import com.github.jeffyjamzhd.renewed.registry.RenewedNetwork;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;
import net.minecraft.ResourceLocation;
import net.minecraft.Slot;

public class C2SItemUpdateScroll implements Packet {
    short slotID;
    short scrollValue;

    public C2SItemUpdateScroll(PacketByteBuf buf) {
        this.slotID = buf.readShort();
        this.scrollValue = buf.readShort();
    }

    public C2SItemUpdateScroll(short slotID, short scrollValue) {
        this.slotID = slotID;
        this.scrollValue = scrollValue;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeShort(slotID);
        buf.writeShort(scrollValue);
    }

    @Override
    public void apply(EntityPlayer player) {
        try {
            // Check for cursor stack first!!
            if (slotID == -999 && player.inventory.getItemStack() != null) {
                ItemStack cursorStack = player.inventory.getItemStack();
                if (cursorStack.getItem() instanceof ItemWithInventory invItem) {
                    invItem.setSelectedSlot(cursorStack, scrollValue);
                    // JeffyBackpacks.logInfo("Set cursor selected slot to {} on server", scrollValue);
                }
                return;
            }

            // Okay, now check for slot
            Slot slot = player.openContainer.getSlot(slotID);
            if (slot != null && slot.getHasStack()) {
                // Check item in slot
                ItemStack stack = slot.getStack();
                if (stack.getItem() instanceof ItemWithInventory invItem) {
                    // Set selected slot
                    invItem.setSelectedSlot(stack, scrollValue);
                    // JeffyBackpacks.logInfo("Set item selected slot to {} on server", scrollValue);
                }
            }
        } catch (Exception e) {
            MiTERenewed.LOGGER.info(e);
        }
    }

    @Override
    public ResourceLocation getChannel() {
        return RenewedNetwork.ITEM_UPDATE_SCROLL;
    }
}
