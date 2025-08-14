package com.github.jeffyjamzhd.renewed.mixins.gui;

import com.github.jeffyjamzhd.renewed.api.ISlotCrafting;
import com.github.jeffyjamzhd.renewed.item.recipe.ShapelessToolRecipe;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MITEContainerCrafting.class)
public abstract class MITEContainerCraftingMixin extends Container {
    @Shadow public CraftingResult current_crafting_result;
    @Shadow private CraftingResult previous_crafting_result;
    @Shadow protected abstract SlotCrafting getCraftingSlot();

    public MITEContainerCraftingMixin(EntityPlayer player) {
        super(player);
    }

    @Inject(method = "onCraftMatrixChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/CraftingResult;haveEquivalentItemStacks(Lnet/minecraft/CraftingResult;Lnet/minecraft/CraftingResult;)Z"))
    private void craftMatrixRecipeTypeChange(IInventory inventory, CallbackInfo ci) {
        if (this.current_crafting_result == null || this.previous_crafting_result == null)
            return;
        if (CraftingResult.haveEquivalentItemStacks(this.current_crafting_result, this.previous_crafting_result)) {
            boolean currentSpecial = this.current_crafting_result.recipe instanceof ShapelessToolRecipe;
            boolean prevSpecial = this.previous_crafting_result.recipe instanceof ShapelessToolRecipe;

            if ((currentSpecial && !prevSpecial) || (!currentSpecial && prevSpecial)) {
                MITEContainerCrafting instance = (MITEContainerCrafting) (Object) this;
                this.player.clearCrafting();
                this.getCraftingSlot().mr$setInitialItemStack(this.player, instance);

            }
        }
    }

}
