package com.github.jeffyjamzhd.renewed.mixins;

import com.github.jeffyjamzhd.renewed.item.recipe.ShapelessToolRecipe;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = SlotCrafting.class, priority = 1300)
public abstract class CraftingManagerMixin {
    @Mutable @Final @Shadow private final IInventory craftMatrix;

    @Shadow public CraftingResult crafting_result;
    @Unique private boolean itemWasTool = false;

    protected CraftingManagerMixin(IInventory craftMatrix) {
        this.craftMatrix = craftMatrix;
    }

    @Inject(method = "onPickupFromSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemStack;getItem()Lnet/minecraft/Item;", ordinal = 0))
    private void damageDagger(EntityPlayer par1EntityPlayer, ItemStack par2ItemStack, CallbackInfo ci) {
        // Iterate through crafting grid
        for (int slot = this.craftMatrix.getSizeInventory() - 1; slot >= 0; --slot) {
            ItemStack stack = this.craftMatrix.getStackInSlot(slot);
            IRecipe recipe = this.crafting_result.recipe;
            if (stack != null && stack.isTool() && recipe instanceof ShapelessToolRecipe) {
                itemWasTool = true;
                int currentDamage = stack.getItemDamage();
                stack.setItemDamage(currentDamage + 66);
                if (stack.getItemDamage() >= stack.getMaxDamage()) {
                    this.craftMatrix.setInventorySlotContents(slot, null);
                }
            }
        }
    }

    @ModifyExpressionValue(
            method = "canPlayerCraftItem",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/InventoryCrafting;hasDamagedItem()Z")
    )
    private boolean modifyExpressionToolCraft(boolean original) {
        return original && !(this.crafting_result.recipe instanceof ShapelessToolRecipe);
    }

    @WrapOperation(method = "onPickupFromSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/IInventory;decrStackSize(II)Lnet/minecraft/ItemStack;"))
    private ItemStack decrDagger(IInventory instance, int i, int j, Operation<ItemStack> original, @Local(name = "item") Item item) {
        if (itemWasTool && item.isTool()) return this.craftMatrix.decrStackSize(i, 0);
        else return original.call(instance, i, j);
    }
}
