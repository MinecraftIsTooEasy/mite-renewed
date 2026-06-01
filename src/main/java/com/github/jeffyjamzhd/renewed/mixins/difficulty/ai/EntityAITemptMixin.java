package com.github.jeffyjamzhd.renewed.mixins.difficulty.ai;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalDoubleRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityAITempt.class)
public class EntityAITemptMixin {
    @Shadow private EntityPlayer temptingPlayer;
    @Shadow private EntityCreature temptedEntity;
    @Shadow private int breedingFood;

    @Inject(method = "shouldExecute", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityPlayer;getHeldItemStack()Lnet/minecraft/ItemStack;"))
    private void checkActualAnimalFoodItem(CallbackInfoReturnable<Boolean> cir,
                                     @Local(ordinal = 0) LocalRef<EntityPlayer> closest_seen_player_holding_tempt_item,
                                     @Local(ordinal = 1) EntityPlayer player,
                                     @Local(ordinal = 0) LocalDoubleRef distance_to_closest_seen_player_holding_tempt_item,
                                     @Local(ordinal = 1) LocalDoubleRef distance) {
        ItemStack stack = player.getHeldItemStack();
        if (stack != null && temptedEntity instanceof EntityAnimal animal) {
            // We want to set breeding food to an unreachable ID so that the
            // default check is never ran
            if (this.breedingFood != -99999) {
                this.breedingFood = -99999;
            }

            // If invalid, skip the check
            if (!animal.isFoodItem(stack)) {
                return;
            }

            // Player has valid food item of animal entity
            if (player == this.temptingPlayer) {
                distance.set(distance.get() - 4D);
            }

            if (closest_seen_player_holding_tempt_item.get() == null || distance.get() < distance_to_closest_seen_player_holding_tempt_item.get()) {
                closest_seen_player_holding_tempt_item.set(player);
                distance_to_closest_seen_player_holding_tempt_item.set(distance.get());
            }
        }
    }
}
