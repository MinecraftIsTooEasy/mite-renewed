package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.entity.EntityPolearm;
import com.github.jeffyjamzhd.renewed.render.RenderPolearm;
import net.xiaoyu233.fml.reload.event.EntityRendererRegistryEvent;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.LOGGER;

public class RenewedEntityRenderer {
    public static void register(EntityRendererRegistryEvent registry) {
        LOGGER.info("Registering entity renderers!");
        registry.register(EntityPolearm.class, new RenderPolearm());
    }
}
