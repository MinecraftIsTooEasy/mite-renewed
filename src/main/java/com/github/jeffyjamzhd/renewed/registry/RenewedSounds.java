package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import net.minecraft.ResourceLocation;
import net.xiaoyu233.fml.reload.event.SoundsRegisterEvent;

public class RenewedSounds {
    public static final ResourceLocation HANDPAN_SPLASH = new ResourceLocation(MiTERenewed.RESOURCE_ID + "item.handpan.splash");

    public static void register(SoundsRegisterEvent event) {
        event.registerSound(HANDPAN_SPLASH);
    }
}
