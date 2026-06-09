package com.github.jeffyjamzhd.renewed.mixins.general.compat.manylib;

import fi.dy.masa.malilib.render.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(value = RenderUtils.class, remap = false)
public class RenderUtilsMixin {
    @ModifyArg(method = "drawHoverText", at = @At(value = "INVOKE", target = "Ljava/lang/String;split(Ljava/lang/String;)[Ljava/lang/String;", ordinal = 0), index = 0)
    private static String fixRegex(String regex) {
        return "\\\\n";
    }
}
