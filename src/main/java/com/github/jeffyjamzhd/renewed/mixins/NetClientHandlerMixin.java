package com.github.jeffyjamzhd.renewed.mixins;

import com.llamalad7.mixinextras.sugar.Local;
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
public class NetClientHandlerMixin {
    @Shadow private Minecraft mc;

    @Inject(method = "handleBlockFX", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/Packet85SimpleSignal;getBlockZ()I",
            shift = At.Shift.AFTER), cancellable = true)
    private void customBlockFXHandler(
            Packet85SimpleSignal packet, CallbackInfo ci,
            @Local EnumBlockFX blockFX,
            @Local WorldClient world) {
        int x = packet.getBlockX();
        int y = packet.getBlockY();
        int z = packet.getBlockZ();

        // Sugar cane transform effect
        if (blockFX.equals(EnumBlockFX.valueOf("transform"))) {
            int blockID = packet.getInteger();
            this.mc.effectRenderer.addBlockDestroyEffects(x, y, z, blockID, 0, 0);
            world.playAuxSFX(2001, x, y, z, blockID);
            ci.cancel();
        }
    }
}
