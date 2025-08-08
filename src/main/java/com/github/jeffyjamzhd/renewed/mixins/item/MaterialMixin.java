package com.github.jeffyjamzhd.renewed.mixins.item;

import net.minecraft.Material;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Material.class)
public class MaterialMixin {
    @Shadow @Final public static Material feather;

    @Shadow @Final public static Material plants;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void modifyMaterial(CallbackInfo ci) {
        feather.setFlammability(true, true, true);
        plants.setFlammability(true, true, true);
    }
}
