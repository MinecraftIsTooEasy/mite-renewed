package com.github.jeffyjamzhd.renewed.mixins.general.core;

import com.github.jeffyjamzhd.renewed.api.IWorldSettings;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.WorldSettings;
import net.minecraft.WorldType;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "loadAllWorlds", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldSettings;func_82750_a(Ljava/lang/String;)Lnet/minecraft/WorldSettings;"))
    private void provideDifficultyIfNone(String par1Str, String par2Str, long par3, WorldType par5WorldType, String par6Str, CallbackInfo ci,
                                         @Local WorldSettings settings) {
        ((IWorldSettings)settings).mr$setDifficulty(RenewedDifficulties.EXTREME);
    }
}
