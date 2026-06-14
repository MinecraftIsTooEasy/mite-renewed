package com.github.jeffyjamzhd.renewed.network;

import com.github.jeffyjamzhd.renewed.api.compat.IGuiWorldOption;
import com.github.jeffyjamzhd.renewed.registry.RenewedNetwork;
import moddedmite.rustedironcore.network.Network;
import moddedmite.rustedironcore.network.Packet;
import moddedmite.rustedironcore.network.PacketByteBuf;
import moddedmite.xylose.bettergamesetting.client.gui.GuiWorldOption;
import net.minecraft.*;
import net.minecraft.server.MinecraftServer;

public class ValidatePlayerAuth {

    public static class C2S implements Packet {
        int playerID;

        public C2S(EntityPlayer player) {
            this.playerID = player.entityId;
        }

        public C2S(PacketByteBuf buf) {
            this.playerID = buf.readInt();
        }

        @Override
        public void write(PacketByteBuf buf) {
            buf.writeInt(this.playerID);
        }

        @Override
        public void apply(EntityPlayer player) {
            if (player.entityId != this.playerID) return;

            // Are they OP? Or is player owner of lan server?
            boolean isOP = player.canCommandSenderUseCommand(2, "");
            boolean isOwner = MinecraftServer.isPlayerHostingGame(player);

            Network.sendToClient((ServerPlayer) player, new S2C(isOP || isOwner));
        }

        @Override
        public ResourceLocation getChannel() {
            return RenewedNetwork.CHECK_AUTH_SERVER;
        }
    }

    public static class S2C implements Packet {
        boolean hasAuth;

        public S2C(boolean hasAuth) {
            this.hasAuth = hasAuth;
        }

        public S2C(PacketByteBuf buf) {
            this.hasAuth = buf.readBoolean();
        }

        @Override
        public void write(PacketByteBuf buf) {
            buf.writeBoolean(this.hasAuth);
        }

        @Override
        public void apply(EntityPlayer player) {
            Minecraft mc = Minecraft.getMinecraft();
            GuiScreen screen = mc.currentScreen;

            if (screen instanceof GuiWorldOption worldOption) {
                IGuiWorldOption cast = (IGuiWorldOption) worldOption;
                cast.mr$updateButtonUsability(this.hasAuth);
            }
        }

        @Override
        public ResourceLocation getChannel() {
            return RenewedNetwork.CHECK_AUTH_CLIENT;
        }
    }
}
