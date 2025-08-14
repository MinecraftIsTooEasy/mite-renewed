package com.github.jeffyjamzhd.renewed.mixins.entity;

import com.github.jeffyjamzhd.renewed.api.IFoodStats;
import com.github.jeffyjamzhd.renewed.item.ItemRenewedFood;
import net.minecraft.EntityPlayer;
import net.minecraft.FoodStats;
import net.minecraft.Item;
import net.minecraft.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(FoodStats.class)
public abstract class FoodStatsMixin implements IFoodStats {
    @Shadow public abstract int addSatiation(int satiation);
    @Shadow public abstract int addNutrition(int nutrition);
    @Shadow private EntityPlayer player;

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
}
