package com.github.jeffyjamzhd.renewed.mixins.difficulty.block;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import net.minecraft.BlockUnderminable;
import net.minecraft.Entity;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockUnderminable.class)
public class BlockUnderminableMixin {
    @Inject(method = "onEntityWalking", at = @At("HEAD"), cancellable = true)
    private void rejectUndermine(World world, int x, int y, int z, Entity entity, CallbackInfo ci) {
        if (cantDisturb(world)) {
            ci.cancel();
        }
    }

    @Inject(method = "onEntityCollidedWithBlock", at = @At("HEAD"), cancellable = true)
    private void rejectUndermineOnTouch(World world, int x, int y, int z, Entity entity, CallbackInfo ci) {
        if (cantDisturb(world)) {
            ci.cancel();
        }
    }

    @Unique
    private boolean cantDisturb(World world) {
        Difficulty difficulty = Difficulty.getFromWorld(world).orElseThrow();
        return !difficulty.getParamValue(RenewedDifficulties.CAN_DISTURB_GROUND);
    }
}
