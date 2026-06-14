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
    NBTTagCompound difficulty;

    public C2SAssignDifficulty(PacketByteBuf buf) {
        try {
            this.difficulty = (NBTTagCompound) NBTTagCompound.readNamedTag(buf.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public C2SAssignDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty.asTagCompound();
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
        boolean isOP = player.canCommandSenderUseCommand(2, "");
        boolean isOwner = MinecraftServer.isPlayerHostingGame(player);

        IWorldInfo info = (IWorldInfo) player.worldObj.getWorldInfo();
        boolean isLocked = info.mr$isDifficultyLocked();

        if ((isOP || isOwner) && !isLocked) {
            Difficulty difficultyObj = Difficulty.createFromTagCompound(this.difficulty);
            info.mr$setDifficulty(difficultyObj);

            S2CSyncDifficulty sync = new S2CSyncDifficulty(difficultyObj);
            MinecraftServer server = MinecraftServer.getServer();

            for (Object o : server.getConfigurationManager().playerEntityList) {
                if (o instanceof ServerPlayer playerAt) {
                    // Send sync packet to player
                    Network.sendToClient(playerAt, sync);

                    // Set player variables
                    FoodStats stats = playerAt.getFoodStats();
                    playerAt.setHealth(playerAt.getHealth(), true, null);
                    stats.setNutrition(stats.getNutrition(), true);
                    stats.setSatiation(stats.getSatiation(), true);

                    // Sync stats to client in next packet
                    float health = playerAt.getHealth();
                    int nutrition = stats.getNutrition();
                    int satiation = stats.getSatiation();
                    float visionDimming = playerAt.vision_dimming;
                    playerAt.getNetManager().addToSendQueue(new Packet8UpdateHealth(health, satiation, nutrition, visionDimming));
                }
            }
        }
    }

    @Override
    public ResourceLocation getChannel() {
        return RenewedNetwork.ASSIGN_DIFFICULTY;
    }
}
