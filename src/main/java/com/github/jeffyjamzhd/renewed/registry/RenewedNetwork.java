package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.network.*;
import moddedmite.rustedironcore.network.PacketReader;
import net.minecraft.ResourceLocation;
import net.xiaoyu233.fml.FishModLoader;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.ofPacket;

public class RenewedNetwork {
    public static final ResourceLocation BLOCK_HIT = ofPacket("BlockHit");
    public static final ResourceLocation ITEM_UPDATE_SCROLL = ofPacket("ItemUpdateScroll");
    public static final ResourceLocation ANIMATE_SLOT = ofPacket("AnimateSlots");
    public static final ResourceLocation ASSIGN_DIFFICULTY = ofPacket("AssignDifficulty");
    public static final ResourceLocation SYNC_DIFFICULTY = ofPacket("SyncDifficulty");
    public static final ResourceLocation CHECK_AUTH_SERVER = ofPacket("CheckAuthS");
    public static final ResourceLocation CHECK_AUTH_CLIENT = ofPacket("CheckAuthC");

    public static void init() {
        MiTERenewed.LOGGER.info("Registering packets!");

        if (!FishModLoader.isServer()) {
            initClient();
        }
        initServer();
    }

    private static void initClient() {
        PacketReader.registerClientPacketReader(ANIMATE_SLOT, S2CAnimateSlot::new);
        PacketReader.registerClientPacketReader(SYNC_DIFFICULTY, S2CSyncDifficulty::new);
        PacketReader.registerClientPacketReader(CHECK_AUTH_CLIENT, ValidatePlayerAuth.S2C::new);
    }

    private static void initServer() {
        PacketReader.registerServerPacketReader(BLOCK_HIT, C2SBlockHit::new);
        PacketReader.registerServerPacketReader(ITEM_UPDATE_SCROLL, C2SItemUpdateScroll::new);
        PacketReader.registerServerPacketReader(ASSIGN_DIFFICULTY, C2SAssignDifficulty::new);
        PacketReader.registerServerPacketReader(CHECK_AUTH_SERVER, ValidatePlayerAuth.C2S::new);
    }
}
