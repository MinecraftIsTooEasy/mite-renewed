package com.github.jeffyjamzhd.renewed.network;

import com.github.jeffyjamzhd.renewed.registry.RenewedNetwork;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.EntityPlayer;
import net.minecraft.ResourceLocation;

// Todo: implementation
public class S2CItemInsertSFX implements Packet {
    public S2CItemInsertSFX(PacketByteBuf buf) {}
    public S2CItemInsertSFX() {}

    @Override
    public void write(PacketByteBuf packetByteBuf) {
    }

    @Override
    public void apply(EntityPlayer entityPlayer) {
//        ItemStack chestplate = player.inventory.armorItemInSlot(2);
//        if (chestplate != null && chestplate.getItem() instanceof ItemWithInventory inv) {
//            inv.playInsertSFX(player.worldObj, player);
//        }
    }

    @Override
    public ResourceLocation getChannel() {
        return RenewedNetwork.ITEM_INSERT_SFX;
    }
}
