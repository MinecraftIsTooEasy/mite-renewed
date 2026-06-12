package com.github.jeffyjamzhd.renewed.mixins.backpack;

import com.github.jeffyjamzhd.renewed.item.ItemWithInventory;
import com.github.jeffyjamzhd.renewed.registry.RenewedEnchantments;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ContainerRepair.class)
public abstract class ContainerRepairMixin extends Container {
    @Shadow
    private IInventory inputSlots;

    public ContainerRepairMixin(EntityPlayer player) {
        super(player);
    }

    @Inject(method = "isRepairing", at = @At(value = "RETURN", ordinal = 1))
    private void doNotDisenchantBackpacks(boolean by_consumable, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stackInput = this.inputSlots.getStackInSlot(0);
        ItemStack stackCombine = this.inputSlots.getStackInSlot(1);

        if (stackInput == null || stackCombine == null) return;

        if (stackInput.getItem() instanceof ItemWithInventory inv) {
            if (!(stackCombine.getItem() instanceof ItemBottleOfDisenchanting)) return;

            boolean hasEnchant = RenewedEnchantments.ENCHANTMENT_HOLDING.getLevel(stackInput) > 0;
            boolean hasItems = inv.getItemCountInStack(stackInput) > 0;

            if (hasEnchant && hasItems) {
                this.repair_fail_condition = 3;
            }
        }
    }
}
