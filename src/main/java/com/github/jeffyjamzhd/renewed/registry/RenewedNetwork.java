package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.network.S2CSyncDifficulty;
import moddedmite.rustedironcore.network.PacketReader;
import net.minecraft.ResourceLocation;
import net.xiaoyu233.fml.FishModLoader;

public class RenewedNetwork {
    public static final ResourceLocation SYNC_DIFFICULTY;

    public static void init() {
        if (!FishModLoader.isServer()) {
            initClient();
        }
        initServer();
    }

    private static void initClient() {
        PacketReader.registerClientPacketReader(SYNC_DIFFICULTY, S2CSyncDifficulty::new);
    }

    private static void initServer() {
    }

    static {
        SYNC_DIFFICULTY = new ResourceLocation("MR", "SyncDifficulty");
    }
}
