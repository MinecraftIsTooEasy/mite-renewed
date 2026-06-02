package com.github.jeffyjamzhd.renewed.mixins.difficulty;

import com.github.jeffyjamzhd.renewed.api.IPacket1Login;
import com.github.jeffyjamzhd.renewed.api.IPacket9Respawn;
import com.github.jeffyjamzhd.renewed.api.IWorldInfo;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetClientHandler.class)
@Environment(EnvType.CLIENT)
public abstract class NetClientHandlerMixin extends NetHandler {
    @Shadow
    private WorldClient worldClient;

    @Inject(method = "handleLogin", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;loadWorld(Lnet/minecraft/WorldClient;)V"))
    private void assignDifficulty(Packet1Login packet, CallbackInfo ci) {
        Difficulty difficulty = ((IPacket1Login) packet).mr$getDifficulty();
        ((IWorldInfo) this.worldClient.getWorldInfo()).mr$setDifficulty(difficulty);
    }

    @Inject(method = "handleRespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;loadWorld(Lnet/minecraft/WorldClient;)V"))
    private void assignDifficultyOnRespawn(Packet9Respawn respawn, CallbackInfo ci) {
        Difficulty difficulty = ((IPacket9Respawn) respawn).mr$getDifficulty();
        ((IWorldInfo) this.worldClient.getWorldInfo()).mr$setDifficulty(difficulty);
    }
}
