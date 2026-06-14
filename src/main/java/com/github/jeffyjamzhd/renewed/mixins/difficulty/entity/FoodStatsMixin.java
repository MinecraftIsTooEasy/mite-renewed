package com.github.jeffyjamzhd.renewed.mixins.difficulty.entity;

import com.github.jeffyjamzhd.renewed.api.IFoodStats;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.item.ItemRenewedFood;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.EntityPlayer;
import net.minecraft.FoodStats;
import net.minecraft.Item;
import net.minecraft.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FoodStats.class)
public abstract class FoodStatsMixin implements IFoodStats {
    @Shadow public abstract int addSatiation(int satiation);
    @Shadow public abstract int addNutrition(int nutrition);
    @Shadow private EntityPlayer player;

    @Shadow
    private float hunger;

    @Override
    public void mr$addFoodValueSubtype(Item item, int subtype) {
        if (item instanceof ItemRenewedFood food) {
            this.addSatiation(food.getSatiationSubtype(this.player, subtype));
            this.addNutrition(food.getNutritionSubtype(subtype));

            if (this.player instanceof ServerPlayer sPlayer) {
                sPlayer.addInsulinResistance(0);
                sPlayer.addEssentialFats(food.getEssentialFatsSubtype(subtype));
                sPlayer.addProtein(food.getProteinSubtype(subtype));
                sPlayer.addPhytonutrients(food.getPhytonutrientsSubtype(subtype));
            }
        }
    }

    @ModifyReturnValue(method = "getNutritionLimit", at = @At("RETURN"))
    private int getNutritionLimit(int original) {
        Difficulty difficulty = this.player.worldObj.mr$getDifficulty();
        if (difficulty == null) {
            return original;
        }

        int level = this.player.getExperienceLevel();
        int levelsPer = difficulty.getParamValue(RenewedDifficulties.LEVELS_NEEDED_FOR_STAT_UP);
        int minimum = difficulty.getParamValue(RenewedDifficulties.MINIMUM_HUNGER) * 2;
        int maximum = difficulty.getParamValue(RenewedDifficulties.MAXIMUM_HUNGER) * 2;

        return Math.max(Math.min(minimum + level / levelsPer * 2, maximum), minimum);
    }

    @ModifyExpressionValue(method = "onUpdate", at = @At(value = "INVOKE", target = "Lnet/minecraft/EnchantmentHelper;getRegenerationModifier(Lnet/minecraft/EntityLivingBase;)F"))
    private float regenMultiply(float original, @Local(argsOnly = true) ServerPlayer player) {
        Difficulty difficulty = Difficulty.getFromWorld(player.worldObj).orElseThrow();
        float factor = difficulty.getParamValue(RenewedDifficulties.REGEN_SPEED);

        return original * factor;
    }

    @Inject(method = "addHunger", at = @At(value = "HEAD"))
    private void exhaustionMultiply(float _unused, CallbackInfo ci, @Local(argsOnly = true) LocalFloatRef hunger) {
        Difficulty difficulty = Difficulty.getFromWorld(player.worldObj).orElseThrow();
        float factor = difficulty.getParamValue(RenewedDifficulties.EXHAUSTION_FACTOR);

        hunger.set(hunger.get() * factor);
    }
}
