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

    public static void init() {
        MiTERenewed.LOGGER.info("Registering packets!");

        if (!FishModLoader.isServer()) {
            initClient();
        }
        initServer();
    }

    private static void initClient() {
        PacketReader.registerClientPacketReader(ANIMATE_SLOT, S2CAnimateSlot::new);
    }

    private static void initServer() {
        PacketReader.registerServerPacketReader(BLOCK_HIT, C2SBlockHit::new);
        PacketReader.registerServerPacketReader(ITEM_UPDATE_SCROLL, C2SItemUpdateScroll::new);
    }
}
