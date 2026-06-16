package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.item.material.MaterialBone;
import net.minecraft.EnumQuality;
import net.minecraft.Material;

public class RenewedMaterial {
    public static final Material bone = new MaterialBone()
            .setMinHarvestLevel(2)
            .setEnchantability(0)
            .setMaxQuality(EnumQuality.average);
}
