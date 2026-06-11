package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import net.minecraft.ResourceLocation;
import net.xiaoyu233.fml.reload.event.SoundsRegisterEvent;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.LOGGER;
import static com.github.jeffyjamzhd.renewed.MiTERenewed.RESOURCE_ID;

public class RenewedSounds {
    public static final ResourceLocation HANDPAN_SPLASH = MiTERenewed.of("item.handpan.splash");
    public static final ResourceLocation HANDPAN_INSERT = MiTERenewed.of("item.handpan.insert");
    public static final ResourceLocation CRAFTING_QUERN = MiTERenewed.of("crafting.quern");
    public static final ResourceLocation CRAFTING_WOOD = MiTERenewed.of("crafting.wood");
    public static final ResourceLocation CRAFTING_CHOP = MiTERenewed.of("crafting.chop");
    public static final ResourceLocation DEATH_POOF = MiTERenewed.of("random.poof");
    public static final ResourceLocation BACKPACK_EXTRACT = MiTERenewed.of("item.backpack.extract");
    public static final ResourceLocation BACKPACK_INSERT = MiTERenewed.of("item.backpack.insert");
    public static final ResourceLocation BACKPACK_FULL = MiTERenewed.of("item.backpack.full");


    public static void register(SoundsRegisterEvent event) {
        LOGGER.info("Registering sounds!");
        event.registerSound(HANDPAN_SPLASH);
        event.registerSound(HANDPAN_INSERT, 4);
        event.registerSound(CRAFTING_QUERN);
        event.registerSound(CRAFTING_WOOD);
        event.registerSound(CRAFTING_CHOP);
        event.registerSound(DEATH_POOF);
        event.registerSound(BACKPACK_EXTRACT, 3);
        event.registerSound(BACKPACK_INSERT, 3);
        event.registerSound(BACKPACK_FULL);
    }
}
