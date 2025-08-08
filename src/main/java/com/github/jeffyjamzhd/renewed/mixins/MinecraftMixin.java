package com.github.jeffyjamzhd.renewed.mixins;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value = Minecraft.class, priority = 1)
public class MinecraftMixin {
    @ModifyReturnValue(method = "getVersionDescriptor", at = @At("RETURN"))
    private static String modifyVersion(String original) {
        return MiTERenewed.getVersionString();
    }

    @ModifyArg(method = "startGame", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V"), index = 0)
    private String setTitle(String newTitle) {
        return Minecraft.getVersionDescriptor(true);
    }
}
