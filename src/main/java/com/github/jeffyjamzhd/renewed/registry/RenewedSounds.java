package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import net.minecraft.ResourceLocation;
import net.xiaoyu233.fml.reload.event.SoundsRegisterEvent;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.LOGGER;
import static com.github.jeffyjamzhd.renewed.MiTERenewed.RESOURCE_ID;

public class RenewedSounds {
    public static final ResourceLocation HANDPAN_SPLASH = new ResourceLocation(RESOURCE_ID + "item.handpan.splash");
    public static final ResourceLocation HANDPAN_INSERT = new ResourceLocation(RESOURCE_ID + "item.handpan.insert");

    public static void register(SoundsRegisterEvent event) {
        LOGGER.info("Registering sounds!");
        event.registerSound(HANDPAN_SPLASH);
        event.registerSound(HANDPAN_INSERT, 4);
    }
}
