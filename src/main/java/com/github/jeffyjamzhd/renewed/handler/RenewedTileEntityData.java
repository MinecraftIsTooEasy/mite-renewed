package com.github.jeffyjamzhd.renewed.handler;

import com.github.jeffyjamzhd.renewed.block.entity.TileEntityCrate;
import moddedmite.rustedironcore.api.event.events.TileEntityDataTypeRegisterEvent;
import moddedmite.rustedironcore.api.util.IdUtilExtra;

import java.util.function.Consumer;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.LOGGER;

public class RenewedTileEntityData implements Consumer<TileEntityDataTypeRegisterEvent> {
    public static final int CRATE_PACKET_ID = IdUtilExtra.getNextTileEntityDataType();

    @Override
    public void accept(TileEntityDataTypeRegisterEvent event) {
        LOGGER.info("Registering tile entity data!");

        event.register(CRATE_PACKET_ID, TileEntityCrate.class);
    }
}
