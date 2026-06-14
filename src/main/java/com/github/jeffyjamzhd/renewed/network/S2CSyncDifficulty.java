package com.github.jeffyjamzhd.renewed.network;

import com.github.jeffyjamzhd.renewed.api.IWorldInfo;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedNetwork;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.EntityPlayer;
import net.minecraft.NBTTagCompound;
import net.minecraft.ResourceLocation;
import net.minecraft.World;

import java.io.IOException;

public class S2CSyncDifficulty implements Packet {
    NBTTagCompound difficulty;

    public S2CSyncDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty.asTagCompound();
    }

    public S2CSyncDifficulty(PacketByteBuf buf) {
        try {
            this.difficulty = (NBTTagCompound) NBTTagCompound.readNamedTag(buf.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(PacketByteBuf buf) {
        try {
            NBTTagCompound.writeNamedTag(this.difficulty, buf.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(EntityPlayer player) {
        World world = player.getWorld();
        IWorldInfo info = (IWorldInfo) world.getWorldInfo();
        info.mr$setDifficulty(Difficulty.createFromTagCompound(this.difficulty));
    }

    @Override
    public ResourceLocation getChannel() {
        return RenewedNetwork.SYNC_DIFFICULTY;
    }
}
