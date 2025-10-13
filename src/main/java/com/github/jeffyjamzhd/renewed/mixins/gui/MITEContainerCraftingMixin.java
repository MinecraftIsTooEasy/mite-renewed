package com.github.jeffyjamzhd.renewed.mixins.gui;

import com.github.jeffyjamzhd.renewed.item.recipe.ShapelessToolRecipe;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.Optional;

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
            Optional<ItemStack> prevTool = Optional.empty(), currTool = Optional.empty();

            IRecipe prevRecipe = this.previous_crafting_result.recipe;
            IRecipe currRecipe = this.current_crafting_result.recipe;
            if (prevRecipe instanceof ShapelessToolRecipe)
                prevTool = Arrays.stream(prevRecipe.getComponents()).filter(ItemStack::isTool).findFirst();
            if (currRecipe instanceof ShapelessToolRecipe)
                currTool = Arrays.stream(currRecipe.getComponents()).filter(ItemStack::isTool).findFirst();
            boolean prevSpecial = prevRecipe instanceof ShapelessToolRecipe;
            boolean currentSpecial = currRecipe instanceof ShapelessToolRecipe;

            // Reset crafting if tool changes
            if (prevTool.isPresent() && currTool.isPresent())
                if (currTool.get().itemID != prevTool.get().itemID)
                    resetCrafting();


            // Reset crafting if types change
            if ((currentSpecial && !prevSpecial) || (!currentSpecial && prevSpecial))
                resetCrafting();

        }
    }

    @Unique
    private void resetCrafting() {
        MITEContainerCrafting instance = (MITEContainerCrafting) (Object) this;
        this.player.clearCrafting();
        this.getCraftingSlot().mr$setInitialItemStack(this.player, instance);
    }

}
