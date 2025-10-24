package com.github.jeffyjamzhd.renewed.mixins.gui;

import com.github.jeffyjamzhd.renewed.api.ISlotCrafting;
import com.github.jeffyjamzhd.renewed.api.sound.CraftingSoundHandler;
import com.github.jeffyjamzhd.renewed.item.ItemRenewedBucket;
import com.github.jeffyjamzhd.renewed.item.recipe.ShapelessBucketConversionRecipe;
import com.github.jeffyjamzhd.renewed.item.recipe.ShapelessToolRecipe;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(value = SlotCrafting.class, priority = 1200)
public abstract class SlotCraftingMixin implements ISlotCrafting {
    @Mutable @Final @Shadow private final IInventory craftMatrix;
    @Shadow public CraftingResult crafting_result;
    @Shadow protected abstract void setInitialItemStack(EntityPlayer player, MITEContainerCrafting container);
    @Unique private IRecipe mr$lastRecipe;

    protected SlotCraftingMixin(IInventory craftMatrix) {
        this.craftMatrix = craftMatrix;
    }

    @Inject(method = "onPickupFromSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/IInventory;decrStackSize(II)Lnet/minecraft/ItemStack;", ordinal = 0))
    private void damageDagger(EntityPlayer player, ItemStack par2ItemStack, CallbackInfo ci, @Local(ordinal = 2) int i) {
        ItemStack stack = this.craftMatrix.getStackInSlot(i);
        if (this.crafting_result != null)
            mr$lastRecipe = this.crafting_result.recipe;
        if (stack != null && stack.isTool() && mr$lastRecipe instanceof ShapelessToolRecipe) {
            // Damage tool
            int currentDamage = stack.getItemDamage();
            stack.setItemDamage(currentDamage + ((ShapelessToolRecipe) mr$lastRecipe).getDamage());
            if (stack.getItemDamage() >= stack.getMaxDamage()) {
                player.entityFX(EnumEntityFX.item_breaking, new SignalData().setByte(0).setShort(stack.itemID));
                this.craftMatrix.setInventorySlotContents(i, null);
            }
        }
    }

    @ModifyExpressionValue(
            method = "canPlayerCraftItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/InventoryCrafting;hasDamagedItem()Z")
    )
    private boolean modifyExpressionToolCraft(boolean original) {
        if (this.crafting_result.recipe == null)
            return original;
        return original &&
                !(this.crafting_result.recipe instanceof ShapelessToolRecipe) &&
                !(this.crafting_result.recipe instanceof ShapelessBucketConversionRecipe) &&
                Arrays.stream(this.crafting_result.recipe.getComponents())
                        .noneMatch(stack -> stack.getItem() instanceof ItemRenewedBucket);
    }

    @WrapOperation(method = "onPickupFromSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/IInventory;decrStackSize(II)Lnet/minecraft/ItemStack;"))
    private ItemStack decrTool(IInventory instance, int i, int j, Operation<ItemStack> original, @Local(name = "item") Item item) {
        if (mr$lastRecipe instanceof ShapelessToolRecipe && item.isTool()) {
            return this.craftMatrix.decrStackSize(i, 0);
        } else {
            return original.call(instance, i, j);
        }
    }

    @Inject(method = "onPickupFromSlot", at = @At("HEAD"))
    private void soundHook(EntityPlayer player, ItemStack stack, CallbackInfo ci) {
        if (player.onServer())
            CraftingSoundHandler.onCraft(stack, this.crafting_result.recipe, player.getWorld(), player);
    }

    @Override
    public void mr$setInitialItemStack(EntityPlayer player, MITEContainerCrafting container) {
        setInitialItemStack(player, container);
    }
}
