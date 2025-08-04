package com.github.jeffyjamzhd.renewed.mixins.gui;

import net.minecraft.GuiMainMenu;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(GuiMainMenu.class)
public class GuiMainMenuMixin {
    @ModifyArg(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiMainMenu;drawString(Lnet/minecraft/FontRenderer;Ljava/lang/String;III)V", ordinal = 0), index = 1)
    String modifyString(String par2) {
        return Minecraft.getVersionDescriptor(true);
    }
}
