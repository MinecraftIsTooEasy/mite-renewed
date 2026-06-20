package com.github.jeffyjamzhd.renewed.mixins.general.recipes;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.CraftingManager;
import net.minecraft.RecipesWeapons;
import net.minecraft.ShapedRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RecipesWeapons.class)
public class RecipesWeaponsMixin {
    @Unique
    private ShapedRecipes sinewBow;

    @Inject(method = "addRecipes", at = @At("TAIL"))
    private void removeRecipes(CraftingManager manager, CallbackInfo ci) {
        manager.getRecipeList().remove(sinewBow);
    }

    @ModifyExpressionValue(method = "addRecipes", at = @At(value = "INVOKE", target = "Lnet/minecraft/CraftingManager;addRecipe(Lnet/minecraft/ItemStack;[Ljava/lang/Object;)Lnet/minecraft/ShapedRecipes;", ordinal = 6))
    private ShapedRecipes noSinewBowRecipe(ShapedRecipes original) {
        this.sinewBow = original;
        return original;
    }
}
