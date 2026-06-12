package com.github.jeffyjamzhd.renewed.network;

import com.github.jeffyjamzhd.renewed.registry.RenewedNetwork;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.*;

public class S2CAnimateSlot implements Packet {
    int playerID;
    byte slot;

    public S2CAnimateSlot(EntityPlayer player, byte slot) {
        this.playerID = player.entityId;
        this.slot = slot;
    }

    public S2CAnimateSlot(PacketByteBuf buf) {
        this.playerID = buf.readInt();
        this.slot = buf.readByte();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.playerID);
        buf.writeByte(this.slot);
    }

    @Override
    public void apply(EntityPlayer player) {
        if (player.entityId != this.playerID) return;
        player.inventory.mainInventory[this.slot].animationsToGo = 7;
    }

    @Override
    public ResourceLocation getChannel() {
        return RenewedNetwork.ANIMATE_SLOT;
    }
}
