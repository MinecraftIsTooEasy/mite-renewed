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

    public static final ResourceLocation MUSIC_CHRIS = new ResourceLocation(RESOURCE_ID + "renewed1");
    public static final ResourceLocation MUSIC_ELEVEN = new ResourceLocation(RESOURCE_ID + "renewed2");
    public static final ResourceLocation MUSIC_EXCUSE = new ResourceLocation(RESOURCE_ID + "renewed3");
    public static final ResourceLocation MUSIC_FLAKE = new ResourceLocation(RESOURCE_ID + "renewed4");
    public static final ResourceLocation MUSIC_PEANUTS = new ResourceLocation(RESOURCE_ID + "renewed5");
    public static final ResourceLocation MUSIC_MOOG_CITY = new ResourceLocation(RESOURCE_ID + "renewed6");
    public static final ResourceLocation MUSIC_MOOG_CITY_2 = new ResourceLocation(RESOURCE_ID + "renewed7");
    public static final ResourceLocation MUSIC_EQUINOXE = new ResourceLocation(RESOURCE_ID + "renewed8");
    public static final ResourceLocation MUSIC_CALM4 = new ResourceLocation(RESOURCE_ID + "magnetic");

    public static void register(SoundsRegisterEvent event) {
        LOGGER.info("Registering sounds!");
        event.registerSound(HANDPAN_SPLASH);
        event.registerSound(HANDPAN_INSERT, 4);
        event.registerSound(CRAFTING_QUERN);
        event.registerSound(CRAFTING_WOOD);
        event.registerSound(CRAFTING_CHOP);

        event.registerMusic(MUSIC_EXCUSE);
        event.registerMusic(MUSIC_CHRIS);
        event.registerMusic(MUSIC_ELEVEN);
        event.registerMusic(MUSIC_FLAKE);
        event.registerMusic(MUSIC_PEANUTS);
        event.registerMusic(MUSIC_MOOG_CITY);
        event.registerMusic(MUSIC_MOOG_CITY_2);
        event.registerMusic(MUSIC_EQUINOXE);
        event.registerMusic(MUSIC_CALM4);
    }
}
