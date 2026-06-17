package com.github.jeffyjamzhd.renewed.mixins.difficulty.gui;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.EntityPlayer;
import net.minecraft.IInventory;
import net.minecraft.SlotCraftingBase;
import net.minecraft.SlotFurnace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(SlotFurnace.class)
public abstract class SlotFurnaceMixin extends SlotCraftingBase {
    public SlotFurnaceMixin(EntityPlayer player, IInventory inventory, int slot_index, int display_x, int display_y) {
        super(player, inventory, slot_index, display_x, display_y);
    }

    @ModifyExpressionValue(method = "onCrafting", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemStack;getExperienceReward(I)I"))
    private int smeltXPRewardScalar(int original) {
        Difficulty difficulty = Difficulty.getFromWorld(this.player.getWorld()).orElseThrow();
        float scalar = difficulty.getParamValue(RenewedDifficulties.SMELTING_EXPERIENCE_FACTOR);

        return Math.round(original * scalar);
    }
}
