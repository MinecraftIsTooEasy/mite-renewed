package com.github.jeffyjamzhd.renewed.network;

import com.github.jeffyjamzhd.renewed.registry.RenewedNetwork;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.*;

public class C2SBlockHit implements Packet {
    int x, y, z;
    EnumFace face;

    public C2SBlockHit(int x, int y, int z, EnumFace face) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.face = face;
    }

    public C2SBlockHit(PacketByteBuf buf) {
        this.x = buf.readInt();
        this.y = buf.readInt();
        this.z = buf.readInt();
        this.face = EnumFace.get(buf.readByte());
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.x);
        buf.writeInt(this.y);
        buf.writeInt(this.z);
        buf.writeByte(this.face.ordinal());
    }

    @Override
    public void apply(EntityPlayer player) {
        World world = player.getWorld();
        Block at = world.getBlock(x, y, z);
        at.mr$onBlockClicked(world, face, x, y, z, player);
    }

    @Override
    public ResourceLocation getChannel() {
        return RenewedNetwork.BLOCK_HIT;
    }
}
