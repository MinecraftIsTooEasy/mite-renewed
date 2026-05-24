package com.github.jeffyjamzhd.renewed.network;

import com.github.jeffyjamzhd.renewed.api.IWorldInfo;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedNetwork;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.*;

import java.io.IOException;

public class S2CSyncDifficulty implements Packet {
    private final Difficulty difficulty;

    public S2CSyncDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    public S2CSyncDifficulty(PacketByteBuf packetByteBuf) {
        try {
            NBTTagCompound difficultyNBT = (NBTTagCompound) NBTTagCompound.readNamedTag(packetByteBuf.getInputStream());
            this.difficulty = Difficulty.createFromTagCompound(difficultyNBT);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(PacketByteBuf packetByteBuf) {
        try {
            NBTTagCompound.writeNamedTag(difficulty.asTagCompound(), packetByteBuf.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(EntityPlayer entityPlayer) {
        WorldClient world = Minecraft.getMinecraft().theWorld;
        ((IWorldInfo)world.getWorldInfo()).mr$setDifficulty(difficulty);
    }

    @Override
    public ResourceLocation getChannel() {
        return RenewedNetwork.SYNC_DIFFICULTY;
    }
}
