package com.github.jeffyjamzhd.renewed.mixins.general.core;

import com.github.jeffyjamzhd.renewed.network.C2SBlockHit;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import moddedmite.rustedironcore.network.Network;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerControllerMP.class)
public class PlayerControllerMPMixin {
    @Shadow @Final private Minecraft mc;

    @ModifyReturnValue(method = "isItemStackEligibleForAUM", at = @At("RETURN"))
    public boolean aumExtraCondition(
            boolean original, @Local(argsOnly = true) ItemStack stack) {
        return original || (stack != null && stack.getItem().mr$isAutoUse(stack));
    }

    @Redirect(method = "clickBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/Block;onBlockClicked(Lnet/minecraft/World;IIILnet/minecraft/EntityPlayer;)V"))
    private void extendedBlockMethod(Block instance, World world, int x, int y, int z, EntityPlayer player, @Local(argsOnly = true) EnumFace face) {
        if (!instance.mr$useExtendedAPI()) {
            instance.onBlockClicked(world, x, y, z, player);
            return;
        }

        if (instance.mr$onBlockClicked(world, face, x, y, z, player)) {
            Network.sendToServer(new C2SBlockHit(x, y, z, face));
        }
    }

    @Redirect(method = "updateController", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/SoundManager;playRandomMusicIfReady()V"))
    private void removeUpdateForVanillaMusicEngine(SoundManager instance) {
    }

    @Inject(method = "updateController", at = @At("HEAD"))
    private void updateMusicEngine(CallbackInfo ci) {
        this.mc.sndManager
                .mr$getMusicEngine()
                .tickEngine(this.mc.theWorld, this.mc.thePlayer);
    }
}
