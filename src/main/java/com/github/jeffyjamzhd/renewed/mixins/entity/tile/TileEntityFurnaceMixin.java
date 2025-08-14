package com.github.jeffyjamzhd.renewed.mixins.entity.tile;

import com.github.jeffyjamzhd.renewed.api.IFurnaceRecipes;
import com.github.jeffyjamzhd.renewed.api.recipe.FurnaceEntry;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.FurnaceRecipes;
import net.minecraft.ItemStack;
import net.minecraft.TileEntityFurnace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = TileEntityFurnace.class, priority = 200)
public abstract class TileEntityFurnaceMixin {
    @Shadow public abstract ItemStack getInputItemStack();

    @Inject(method = "smeltItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/TileEntityFurnace;getInputItemStack()Lnet/minecraft/ItemStack;", ordinal = 3))
    private void setConsumption(
            int heat, CallbackInfo ci,
            @Local(name = "consumption") LocalIntRef consumption) {
        ItemStack input = getInputItemStack();
        FurnaceEntry entry = ((IFurnaceRecipes)FurnaceRecipes.smelting()).mr$getComplexEntry(input, false);
        if (entry != null)
            consumption.set(entry.output().stackSize);
    }
}
