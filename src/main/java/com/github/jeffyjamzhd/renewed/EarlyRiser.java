package com.github.jeffyjamzhd.renewed;

import com.chocohead.mm.api.ClassTinkerers;
import com.chocohead.mm.api.EnumAdder;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class EarlyRiser implements PreLaunchEntrypoint {
    public static final EnumAdder BLOCKFX_ENUM;

    @Override
    public void onPreLaunch() {
        BLOCKFX_ENUM.addEnum("transform", 4);
        BLOCKFX_ENUM.build();
    }

    static {
        BLOCKFX_ENUM = ClassTinkerers.enumBuilder("net.minecraft.EnumBlockFX", new Object[]{Integer.TYPE});
    }
}
