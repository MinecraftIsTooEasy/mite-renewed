package com.github.jeffyjamzhd.renewed.api.music;

import net.minecraft.EntityPlayer;
import net.minecraft.World;

public interface IMusicCondition {
    boolean check(World world, EntityPlayer player);
    String getIdentifier();
}
