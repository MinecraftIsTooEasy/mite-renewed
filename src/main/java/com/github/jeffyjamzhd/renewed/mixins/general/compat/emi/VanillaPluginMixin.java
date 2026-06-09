package com.github.jeffyjamzhd.renewed.mixins.general.compat.emi;

import com.github.jeffyjamzhd.renewed.compat.emi.recipe.EmiShapelessToolRecipe;
import com.github.jeffyjamzhd.renewed.item.recipe.ShapelessToolRecipe;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.emi.emi.VanillaPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipe;
import net.minecraft.IRecipe;
import net.minecraft.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

@Mixin(VanillaPlugin.class)
public class VanillaPluginMixin {
    @Shadow
    private static void addRecipeSafe(EmiRegistry registry, Supplier<EmiRecipe> supplier, IRecipe recipe) {
        throw new UnsupportedOperationException("Implemented via mixin");
    }

    @Redirect(method = "register", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/Item;isDamageable()Z",
            ordinal = 0))
    private boolean isRepairableInstead(Item instance) {
        return instance.isRepairable();
    }

    @WrapOperation(method = "register", at = @At(value = "INVOKE", target = "Ldev/emi/emi/VanillaPlugin;addRecipeSafe(Ldev/emi/emi/api/EmiRegistry;Ljava/util/function/Supplier;Lnet/minecraft/IRecipe;)V", ordinal = 2))
    private void addToolRecipe(EmiRegistry registry, Supplier<EmiRecipe> supplier, IRecipe recipe, Operation<Void> original) {
        if (recipe instanceof ShapelessToolRecipe<?> toolRecipe) {
           addRecipeSafe(registry, () -> new EmiShapelessToolRecipe<>(toolRecipe), recipe);
        } else {
            original.call(registry, supplier, recipe);
        }
    }
}
