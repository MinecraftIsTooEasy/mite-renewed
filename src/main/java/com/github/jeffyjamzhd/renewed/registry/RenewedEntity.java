package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.entity.EntityPolearm;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.xiaoyu233.fml.reload.event.EntityRegisterEvent;
import net.xiaoyu233.fml.reload.utils.IdUtil;

public class RenewedEntity {
    public static void register(EntityRegisterEvent registry) {
        registry.register(EntityPolearm.class, MiTERenewed.NAMESPACE, "EntityPolearm", IdUtil.getNextEntityID());
    }
}
