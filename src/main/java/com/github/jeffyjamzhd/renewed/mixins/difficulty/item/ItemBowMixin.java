package com.github.jeffyjamzhd.renewed.mixins.difficulty.item;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemBow.class)
public class ItemBowMixin {
    @Inject(method = "onPlayerStoppedUsing", at = @At(value = "INVOKE", target = "Lnet/minecraft/World;spawnEntityInWorld(Lnet/minecraft/Entity;)Z"))
    private void scaleDamage(ItemStack item_stack, World world, EntityPlayer player, int item_in_use_count, CallbackInfo ci, @Local EntityArrow arrow) {
        Difficulty difficulty = Difficulty.getFromWorld(world).orElseThrow();
        double damageFactor = (double) difficulty.getParamValue(RenewedDifficulties.PLAYER_DAMAGE_FACTOR);
        arrow.setDamage(arrow.getDamage() * damageFactor);
    }
}
