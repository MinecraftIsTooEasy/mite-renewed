package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.entity.EntityPolearm;
import moddedmite.rustedironcore.api.event.events.EntityTrackerRegisterEvent;
import moddedmite.rustedironcore.api.event.handler.EntityTrackerHandler;
import net.minecraft.Entity;
import net.minecraft.WorldClient;

import java.util.function.Consumer;

public class RenewedTracker implements Consumer<EntityTrackerRegisterEvent> {
    @Override
    public void accept(EntityTrackerRegisterEvent event) {
        event.registerEntityTracker(entity -> entity instanceof EntityPolearm, 64, 10, true);

        event.registerEntityPacket(entity -> entity instanceof EntityPolearm, transform(EntityPolearm::new));
    }

    private static EntityTrackerHandler.EntitySupplier transform(SimpleConstructor simpleConstructor) {
        return (world, x, y, z, packet) -> simpleConstructor.get(world, x, y, z);
    }

    @FunctionalInterface
    private interface SimpleConstructor {
        Entity get(WorldClient world, double x, double y, double z);
    }
}