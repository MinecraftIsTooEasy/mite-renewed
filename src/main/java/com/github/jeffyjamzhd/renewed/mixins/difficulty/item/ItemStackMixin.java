package com.github.jeffyjamzhd.renewed.mixins.difficulty.item;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.AttributeModifier;
import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.UUID;

@Mixin(ItemStack.class)
@Environment(EnvType.CLIENT)
public class ItemStackMixin {
    @ModifyExpressionValue(method = "getTooltip", at = @At(value = "INVOKE", target = "Lnet/minecraft/AttributeModifier;getAmount()D"))
    private double scaleDamageValueForTooltip(double original,
                                              @Local AttributeModifier modifier,
                                              @Local(argsOnly = true)EntityPlayer player) {
        // Only process damage
        if (!modifier.getID().equals(UUID.fromString("CB3F55D3-645C-4F38-A497-9C13A33DB5CF"))) return original;

        Difficulty difficulty = Difficulty.getFromWorld(player.getWorld()).orElseThrow();
        float scalar = difficulty.getParamValue(RenewedDifficulties.PLAYER_DAMAGE_FACTOR);

        return Math.round((original * scalar) * 10F) / 10F;
    }
}
