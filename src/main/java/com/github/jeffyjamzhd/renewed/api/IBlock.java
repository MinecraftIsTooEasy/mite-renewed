package com.github.jeffyjamzhd.renewed.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.World;

public interface IBlock {
    /**
     * {@code true} if this block should use more granular rendering
     * (additional icon checks)
     */
    @Environment(EnvType.CLIENT)
    default boolean mr$useSpecialCrossedRenderer() {
        return false;
    }

    /**
     * Extended block hardness check.
     */
    default float mr$getBlockHardness(World world, int x, int y, int z, int metadata) {
        return 0F;
    }
}
