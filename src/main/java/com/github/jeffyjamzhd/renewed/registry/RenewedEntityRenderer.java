package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.entity.EntityPolearm;
import com.github.jeffyjamzhd.renewed.render.RenderPolearm;
import net.xiaoyu233.fml.reload.event.EntityRendererRegistryEvent;

public class RenewedEntityRenderer {
    public static void register(EntityRendererRegistryEvent registry) {
        registry.register(EntityPolearm.class, new RenderPolearm());
    }
}
