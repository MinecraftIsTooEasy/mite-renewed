package com.github.jeffyjamzhd.renewed.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.EntityPlayer;
import net.minecraft.EnumFace;
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
     * {@code true} if this block uses the extended interaction API
     */
    default boolean mr$useExtendedAPI() { return false; }

    /**
     * Extended block hardness check.
     */
    default float mr$getBlockHardness(World world, int x, int y, int z, int metadata) {
        return 0F;
    }

    /**
     * Extended block clicked hook
     * @return true if interaction should be sent to server
     */
    default boolean mr$onBlockClicked(World world, EnumFace face, int x, int y, int z, EntityPlayer player) { return false; }
}
