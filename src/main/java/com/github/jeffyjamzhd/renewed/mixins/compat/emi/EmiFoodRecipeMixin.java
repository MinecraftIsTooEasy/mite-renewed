package com.github.jeffyjamzhd.renewed.mixins.compat.emi;

import com.github.jeffyjamzhd.renewed.item.ItemRenewedFood;
import dev.emi.emi.api.stack.EmiStack;
import moddedmite.emi.recipe.EmiFoodRecipe;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EmiFoodRecipe.class)
public class EmiFoodRecipeMixin {
    @Mutable @Shadow @Final private int nutrition;
    @Mutable @Shadow @Final private int saturation;
    @Mutable @Shadow @Final private int phytonutrients;
    @Mutable @Shadow @Final private int protein;
    @Mutable @Shadow @Final private int sugar;
    @Mutable @Shadow @Final private EmiStack foodItem;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void fixSubitemFood(ItemStack foodStack, CallbackInfo ci) {
        if (foodStack.getItem() instanceof ItemRenewedFood item) {
            int sub = foodStack.getItemSubtype();
            this.nutrition = item.getNutritionSubtype(sub);
            this.saturation = item.getSatiationSubtype(null, sub);
            this.phytonutrients = item.getPhytonutrientsSubtype(sub) / 8000;
            this.protein = item.getProteinSubtype(sub) / 8000;
            this.sugar = item.getSugarSubtype(sub) / 8000;
            this.foodItem = EmiStack.of(new ItemStack(item.itemID, 1, sub));
        }
    }
}
