package com.github.jeffyjamzhd.renewed.mixins;

import com.github.jeffyjamzhd.renewed.item.recipe.ShapelessToolRecipe;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.Container;
import net.minecraft.GuiContainer;
import net.minecraft.MITEContainerCrafting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(GuiContainer.class)
public class GuiContainerMixin {
    @Shadow public Container inventorySlots;

    @ModifyExpressionValue(
            method = "drawItemStackTooltip(Lnet/minecraft/ItemStack;IILnet/minecraft/Slot;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/InventoryCrafting;hasDamagedItem()Z")
    )
    private boolean modifyExpressionToolCraft(boolean original) {
        return original && !(((MITEContainerCrafting) (this.inventorySlots)).getRecipe() instanceof ShapelessToolRecipe);
    }
}
