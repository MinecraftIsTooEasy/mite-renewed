package com.github.jeffyjamzhd.renewed.mixins.difficulty.server;

import com.github.jeffyjamzhd.renewed.api.IPacket1Login;
import com.github.jeffyjamzhd.renewed.api.IPacket9Respawn;
import com.github.jeffyjamzhd.renewed.api.IWorld;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerConfigurationManager.class)
public class ServerConfigurationManagerMixin {
    @Shadow
    @Final
    private MinecraftServer mcServer;

    @WrapOperation(method = "initializeConnectionToPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/NetServerHandler;sendPacketToPlayer(Lnet/minecraft/Packet;)V", ordinal = 0))
    private void assignDifficulty(NetServerHandler instance, Packet packet, Operation<Void> original, @Local WorldServer server) {
        IPacket1Login login = (IPacket1Login) packet;
        IWorld worldCast = (IWorld) server.getWorld();

        login.mr$setDifficulty(worldCast.mr$getDifficulty());
        login.mr$setDifficultyLock(worldCast.mr$isDifficultyLocked());

        original.call(instance, packet);
    }

    @WrapOperation(method = "respawnPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/NetServerHandler;sendPacketToPlayer(Lnet/minecraft/Packet;)V", ordinal = 1))
    private void assignDifficultyForRespawn(NetServerHandler instance, Packet packet, Operation<Void> original, @Local WorldServer server) {
        IPacket9Respawn respawn = (IPacket9Respawn) packet;
        IWorld worldCast = (IWorld) server.getWorld();

        respawn.mr$setDifficulty(worldCast.mr$getDifficulty());
        respawn.mr$setDifficultyLock(worldCast.mr$isDifficultyLocked());

        original.call(instance, packet);
    }

    @WrapOperation(method = "transferPlayerToDimension", at = @At(value = "INVOKE", target = "Lnet/minecraft/NetServerHandler;sendPacketToPlayer(Lnet/minecraft/Packet;)V", ordinal = 0))
    private void assignDifficultyForDimension(NetServerHandler instance, Packet packet, Operation<Void> original) {
        IPacket9Respawn respawn = (IPacket9Respawn) packet;
        IWorld worldCast = (IWorld) this.mcServer.getOverworld();

        respawn.mr$setDifficulty(worldCast.mr$getDifficulty());
        respawn.mr$setDifficultyLock(worldCast.mr$isDifficultyLocked());

        original.call(instance, packet);
    }
}
