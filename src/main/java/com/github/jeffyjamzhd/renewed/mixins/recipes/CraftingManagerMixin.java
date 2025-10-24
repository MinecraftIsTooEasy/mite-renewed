package com.github.jeffyjamzhd.renewed.mixins.recipes;

import com.github.jeffyjamzhd.renewed.api.event.CraftingSoundRegisterEvent;
import com.github.jeffyjamzhd.renewed.api.event.HandpanRegisterEvent;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.CraftingManager;
import net.minecraft.Item;
import net.minecraft.ItemStack;
import net.minecraft.ShapedRecipes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CraftingManager.class)
public class CraftingManagerMixin {
    @Shadow private List recipes;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void event(CallbackInfo ci) {
        CraftingSoundRegisterEvent.init();
        HandpanRegisterEvent.init();
    }

    @ModifyArg(method = "<init>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/CraftingManager;addRecipe(Lnet/minecraft/ItemStack;[Ljava/lang/Object;)Lnet/minecraft/ShapedRecipes;",
            ordinal = 7),
            index = 1)
    private Object[] modifyLeashRecipeSilk(Object[] array) {
        array[array.length-1] = new ItemStack(Item.slimeBall, 1, Short.MAX_VALUE);
        return array;
    }

    @ModifyArg(method = "<init>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/CraftingManager;addRecipe(Lnet/minecraft/ItemStack;[Ljava/lang/Object;)Lnet/minecraft/ShapedRecipes;",
            ordinal = 8),
            index = 1)
    private Object[] modifyLeashRecipeSinew(Object[] array) {
        array[array.length-1] = new ItemStack(Item.slimeBall, 1, Short.MAX_VALUE);
        return array;
    }

    @Unique
    private ShapedRecipes woolRecipe;

    @WrapOperation(method = "<init>", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/CraftingManager;addRecipe(Lnet/minecraft/ItemStack;[Ljava/lang/Object;)Lnet/minecraft/ShapedRecipes;",
            ordinal = 17))
    private ShapedRecipes getWoolRecipe(CraftingManager instance, ItemStack par1ItemStack, Object[] par2ArrayOfObj, Operation<ShapedRecipes> original) {
        woolRecipe = original.call(instance, par1ItemStack, par2ArrayOfObj);
        return woolRecipe;
    }

    @Inject(method = "<init>", at = @At(
            value = "TAIL"))
    private void removeWoolRecipe(CallbackInfo ci) {
        this.recipes.remove(woolRecipe);
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
