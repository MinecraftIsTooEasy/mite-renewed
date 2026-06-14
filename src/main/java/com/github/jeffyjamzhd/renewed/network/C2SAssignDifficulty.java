package com.github.jeffyjamzhd.renewed.network;

import com.github.jeffyjamzhd.renewed.api.IWorldInfo;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedNetwork;
import moddedmite.rustedironcore.network.Network;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import net.minecraft.*;
import net.minecraft.server.MinecraftServer;

import java.io.IOException;

public class C2SAssignDifficulty implements Packet {
    int playerID;
    NBTTagCompound difficulty;

    public C2SAssignDifficulty(PacketByteBuf buf) {
        this.playerID = buf.readInt();
        try {
            this.difficulty = (NBTTagCompound) NBTTagCompound.readNamedTag(buf.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public C2SAssignDifficulty(EntityPlayer player, Difficulty difficulty) {
        this.playerID = player.entityId;
        this.difficulty = difficulty.asTagCompound();
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeInt(this.playerID);
        try {
            NBTTagCompound.writeNamedTag(this.difficulty, buf.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void apply(EntityPlayer player) {
        boolean isOP = player.canCommandSenderUseCommand(2, "");
        boolean isOwner = MinecraftServer.isPlayerHostingGame(player);

        IWorldInfo info = (IWorldInfo) player.worldObj.getWorldInfo();
        boolean isLocked = info.mr$isDifficultyLocked();

        if ((isOP || isOwner) && !isLocked) {
            Difficulty difficultyObj = Difficulty.createFromTagCompound(this.difficulty);
            info.mr$setDifficulty(difficultyObj);
            Network.sendToClient((ServerPlayer) player, new S2CSyncDifficulty(difficultyObj));
        }
    }

    @Override
    public ResourceLocation getChannel() {
        return RenewedNetwork.ASSIGN_DIFFICULTY;
    }
}
