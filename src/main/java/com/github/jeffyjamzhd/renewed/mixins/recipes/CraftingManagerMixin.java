package com.github.jeffyjamzhd.renewed.mixins.recipes;

import com.github.jeffyjamzhd.renewed.api.event.CraftingSoundRegisterEvent;
import com.github.jeffyjamzhd.renewed.api.event.HandpanRegisterEvent;
import net.minecraft.CraftingManager;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CraftingManager.class)
public class CraftingManagerMixin {
    @Inject(method = "<init>", at = @At("TAIL"))
    private void event(CallbackInfo ci) {
        CraftingSoundRegisterEvent.init();
        HandpanRegisterEvent.init();
    }

    @ModifyArg(method = "<init>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/CraftingManager;addRecipe(Lnet/minecraft/ItemStack;[Ljava/lang/Object;)Lnet/minecraft/ShapedRecipes;",
            ordinal = 40),
            index = 0)
    private ItemStack setCharcoalTorchCount(ItemStack stack) {
        return stack.setStackSize(2);
    }
}
