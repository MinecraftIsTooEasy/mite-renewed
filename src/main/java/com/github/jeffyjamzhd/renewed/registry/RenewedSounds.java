package com.github.jeffyjamzhd.renewed.registry;

import net.minecraft.ResourceLocation;
import net.xiaoyu233.fml.reload.event.SoundsRegisterEvent;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.LOGGER;
import static com.github.jeffyjamzhd.renewed.MiTERenewed.RESOURCE_ID;

public class RenewedSounds {
    public static final ResourceLocation HANDPAN_SPLASH = new ResourceLocation(RESOURCE_ID + "item.handpan.splash");
    public static final ResourceLocation HANDPAN_INSERT = new ResourceLocation(RESOURCE_ID + "item.handpan.insert");
    public static final ResourceLocation CRAFTING_QUERN = new ResourceLocation(RESOURCE_ID + "crafting.quern");
    public static final ResourceLocation CRAFTING_WOOD = new ResourceLocation(RESOURCE_ID + "crafting.wood");
    public static final ResourceLocation CRAFTING_CHOP = new ResourceLocation(RESOURCE_ID + "crafting.chop");

    public static void register(SoundsRegisterEvent event) {
        LOGGER.info("Registering sounds!");
        event.registerSound(HANDPAN_SPLASH);
        event.registerSound(HANDPAN_INSERT, 4);
        event.registerSound(CRAFTING_QUERN);
        event.registerSound(CRAFTING_WOOD);
        event.registerSound(CRAFTING_CHOP);
    }
}
