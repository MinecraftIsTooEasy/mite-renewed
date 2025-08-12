package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.api.music.TracklistRegistry;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.*;
import static com.github.jeffyjamzhd.renewed.api.music.TracklistRegistry.track;

public class RenewedTracklist {
    public static void register() {
        LOGGER.info("Registering track list!");

        TracklistRegistry.registerTrackList(
                track("calm1.ogg", "C418", "Minecraft"),
                track("calm2.ogg", "C418", "Clark"),
                track("calm3.ogg", "C418", "Sweden"),
                track("piano1.ogg", "C418", "Dry Hands"),
                track("piano2.ogg", "C418", "Wet Hands"),
                track("piano3.ogg", "C418", "Mice on Venus"),
                track("hal1.ogg", "C418", "Subwoofer Lullaby"),
                track("hal2.ogg", "C418", "Living Mice"),
                track("hal3.ogg", "C418", "Haggstrom"),
                track("hal4.ogg", "C418", "Danny"),
                track("nuance1.ogg", "C418", "Key"),
                track("nuance2.ogg", "C418", "Oxygene"),
                track("magnetic.ogg", "Markus Alexei", "Magnetic Circuit"),
                track("renewed1.ogg", "C418", "Chris"),
                track("renewed2.ogg", "C418", "Eleven"),
                track("renewed3.ogg", "C418", "Excuse"),
                track("renewed4.ogg", "C418", "Flake"),
                track("renewed5.ogg", "C418", "Peanuts")
        );
    }
}
