package com.github.jeffyjamzhd.renewed;

import com.github.jeffyjamzhd.renewed.registry.RenewedBlocks;
import huix.glacier.api.entrypoint.IGameRegistry;
import huix.glacier.api.registry.MinecraftRegistry;

public class RenewedGameRegistry implements IGameRegistry {
    public static final MinecraftRegistry registry =
            new MinecraftRegistry(MiTERenewed.RESOURCE_ID).initAutoItemRegister();

    @Override
    public void onGameRegistry() {
        RenewedBlocks.register(registry);
    }
}
