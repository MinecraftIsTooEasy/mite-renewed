package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.network.C2SBlockHit;
import moddedmite.rustedironcore.network.PacketReader;
import net.minecraft.ResourceLocation;
import net.xiaoyu233.fml.FishModLoader;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.ofPacket;

public class RenewedNetwork {
    public static final ResourceLocation BLOCK_HIT = ofPacket("BlockHit");

    public static void init() {
        if (!FishModLoader.isServer()) {
            initClient();
        }
        initServer();
    }

    private static void initClient() {
    }

    private static void initServer() {
        PacketReader.registerServerPacketReader(BLOCK_HIT, C2SBlockHit::new);
    }
}
