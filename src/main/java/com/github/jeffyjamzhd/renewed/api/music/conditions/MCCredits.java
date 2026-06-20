package com.github.jeffyjamzhd.renewed.api.music.conditions;

import com.github.jeffyjamzhd.renewed.api.music.IMusicCondition;
import net.minecraft.EntityPlayer;
import net.minecraft.GuiWinGame;
import net.minecraft.Minecraft;
import net.minecraft.World;
import org.jetbrains.annotations.Nullable;

public class MCCredits implements IMusicCondition {
    @Override
    public boolean check(@Nullable World world, @Nullable EntityPlayer player) {
        return Minecraft.getMinecraft().currentScreen instanceof GuiWinGame;
    }

    @Override
    public String getIdentifier() {
        return "miterenewed:credits";
    }

    @Override
    public int getPriority() {
        return 99;
    }
}
