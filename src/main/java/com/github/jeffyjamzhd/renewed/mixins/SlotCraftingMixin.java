package com.github.jeffyjamzhd.renewed.mixins;

import com.github.jeffyjamzhd.renewed.api.ISlotCrafting;
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

@Mixin(value = SlotCrafting.class, priority = 1200)
public abstract class SlotCraftingMixin implements ISlotCrafting {
    @Mutable @Final @Shadow private final IInventory craftMatrix;

    @Shadow public CraftingResult crafting_result;

    @Shadow protected abstract void setInitialItemStack(EntityPlayer player, MITEContainerCrafting container);

    @Unique private IRecipe lastRecipe;

    protected SlotCraftingMixin(IInventory craftMatrix) {
        this.craftMatrix = craftMatrix;
    }

    @Inject(method = "onPickupFromSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/IInventory;decrStackSize(II)Lnet/minecraft/ItemStack;", ordinal = 0))
    private void damageDagger(EntityPlayer player, ItemStack par2ItemStack, CallbackInfo ci, @Local(ordinal = 2) int i) {
        ItemStack stack = this.craftMatrix.getStackInSlot(i);
        if (this.crafting_result != null)
            lastRecipe = this.crafting_result.recipe;
        if (stack != null && stack.isTool() && lastRecipe instanceof ShapelessToolRecipe) {
            // Damage tool
            int currentDamage = stack.getItemDamage();
            stack.setItemDamage(currentDamage + ((ShapelessToolRecipe) lastRecipe).getDamage());
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
        return original && !(this.crafting_result.recipe instanceof ShapelessToolRecipe);
    }

    @WrapOperation(method = "onPickupFromSlot", at = @At(value = "INVOKE", target = "Lnet/minecraft/IInventory;decrStackSize(II)Lnet/minecraft/ItemStack;"))
    private ItemStack decrTool(IInventory instance, int i, int j, Operation<ItemStack> original, @Local(name = "item") Item item) {
        if (lastRecipe instanceof ShapelessToolRecipe && item.isTool()) return this.craftMatrix.decrStackSize(i, 0);
        else return original.call(instance, i, j);
    }

    @Override
    public void mr$setInitialItemStack(EntityPlayer player, MITEContainerCrafting container) {
        setInitialItemStack(player, container);
    }
}
