package com.github.jeffyjamzhd.renewed.mixins.difficulty.item;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.EntityLivingBase;
import net.minecraft.ItemArmor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemArmor.class)
public class ItemArmorMixin {
    @ModifyExpressionValue(method = "getProtectionAfterDamageFactor", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemArmor;getMultipliedProtection(Lnet/minecraft/ItemStack;)F"))
    private float scaleProtectionValue(float original, @Local(argsOnly = true) EntityLivingBase entity) {
        Difficulty difficulty = Difficulty.getFromWorld(entity.worldObj).orElseThrow();
        float factor = difficulty.getParamValue(RenewedDifficulties.ARMOR_PROTECTION_FACTOR);

        return original * factor;
    }
}
