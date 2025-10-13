package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.entity.EntityPolearm;
import net.xiaoyu233.fml.reload.event.EntityRegisterEvent;
import net.xiaoyu233.fml.reload.utils.IdUtil;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.LOGGER;

public class RenewedEntity {
    public static void register(EntityRegisterEvent registry) {
        LOGGER.info("Registering entities!");
        registry.register(EntityPolearm.class, MiTERenewed.NAMESPACE, "EntityPolearm", IdUtil.getNextEntityID());
    }
}
