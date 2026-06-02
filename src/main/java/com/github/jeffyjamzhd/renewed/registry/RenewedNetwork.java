package com.github.jeffyjamzhd.renewed.registry;

import net.xiaoyu233.fml.FishModLoader;

public class RenewedNetwork {
    public static void init() {
        if (!FishModLoader.isServer()) {
            initClient();
        }
        initServer();
    }

    private static void initClient() {
    }

    private static void initServer() {
    }

    static {
    }
}
