package com.github.jeffyjamzhd.renewed.api.music.conditions;

import com.github.jeffyjamzhd.renewed.api.music.IMusicCondition;
import com.google.gson.annotations.SerializedName;
import net.minecraft.EntityPlayer;
import net.minecraft.GuiMainMenu;
import net.minecraft.Minecraft;
import net.minecraft.World;

import javax.annotation.Nullable;

public class MCTitle implements IMusicCondition {
    @Override
    public boolean check(@Nullable World world, @Nullable EntityPlayer player) {
        return world == null && !Minecraft.getMinecraft().isIntegratedServerRunning();
    }

    @Override
    public String getIdentifier() {
        return "miterenewed:title";
    }

    @Override
    public int getPriority() {
        return 99;
    }
}
