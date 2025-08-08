package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.entity.EntityPolearm;
import moddedmite.rustedironcore.api.event.events.EntityTrackerRegisterEvent;
import moddedmite.rustedironcore.api.event.handler.EntityTrackerHandler;
import moddedmite.rustedironcore.api.util.IdUtilExtra;
import net.minecraft.Entity;
import net.minecraft.Packet23VehicleSpawn;
import net.minecraft.WorldClient;
import net.xiaoyu233.fml.reload.utils.IdUtil;

import java.util.function.Consumer;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.LOGGER;

public class RenewedTracker implements Consumer<EntityTrackerRegisterEvent> {
    public static int POLEARM_TYPE;

    @Override
    public void accept(EntityTrackerRegisterEvent event) {
        LOGGER.info("Registering entity trackers!");
        POLEARM_TYPE = IdUtilExtra.getNextPacket23Type();

        event.registerEntityTracker(entity -> entity instanceof EntityPolearm, 64, 10, true);
        event.registerEntityPacket(entity -> entity instanceof EntityPolearm, POLEARM_TYPE, entity -> new Packet23VehicleSpawn(entity, POLEARM_TYPE, ((EntityPolearm) entity).getOwnerRID()), transform(EntityPolearm::new));
    }

    private static EntityTrackerHandler.EntitySupplier transform(SimpleConstructor simpleConstructor) {
        return (world, x, y, z, packet) -> simpleConstructor.get(world, x, y, z, packet.arrow_item_id, packet.throwerEntityId);
    }

    @FunctionalInterface
    private interface SimpleConstructor {
        Entity get(WorldClient world, double x, double y, double z, int item, int thrower);
    }
}