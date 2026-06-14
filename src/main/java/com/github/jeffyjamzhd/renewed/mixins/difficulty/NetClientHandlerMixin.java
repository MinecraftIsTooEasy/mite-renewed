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
        IPacket1Login login = (IPacket1Login) packet;
        IWorldInfo info = (IWorldInfo) this.worldClient.getWorldInfo();

        info.mr$setDifficulty(login.mr$getDifficulty());
        info.mr$setDifficultyLocked(login.mr$getDifficultyLock());
    }

    @Inject(method = "handleRespawn", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;loadWorld(Lnet/minecraft/WorldClient;)V"))
    private void assignDifficultyOnRespawn(Packet9Respawn packet, CallbackInfo ci) {
        IPacket9Respawn respawn = (IPacket9Respawn) packet;
        IWorldInfo info = (IWorldInfo) this.worldClient.getWorldInfo();

        info.mr$setDifficulty(respawn.mr$getDifficulty());
        info.mr$setDifficultyLocked(respawn.mr$getDifficultyLock());
    }
}
