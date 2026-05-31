package com.github.jeffyjamzhd.renewed.mixins.difficulty.server;

import com.github.jeffyjamzhd.renewed.api.IPacket1Login;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ServerConfigurationManager.class)
public class ServerConfigurationManagerMixin {
    @WrapOperation(method = "initializeConnectionToPlayer", at = @At(value = "INVOKE", target = "Lnet/minecraft/NetServerHandler;sendPacketToPlayer(Lnet/minecraft/Packet;)V", ordinal = 0))
    private void assignDifficulty(NetServerHandler instance, Packet packet, Operation<Void> original, @Local WorldServer server) {
        ((IPacket1Login) packet).mr$setDifficulty(server.getWorld().mr$getDifficulty());
        original.call(instance, packet);
    }
}
