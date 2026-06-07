package com.github.jeffyjamzhd.renewed.mixins.general.entity.tile;

import com.github.jeffyjamzhd.renewed.block.entity.TileEntityCrate;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.Hopper;
import net.minecraft.IInventory;
import net.minecraft.ItemStack;
import net.minecraft.TileEntityHopper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TileEntityHopper.class)
public class TileEntityHopperMixin {
    @Shadow
    private ItemStack[] hopperItemStacks;

    @Inject(method = "func_102014_c", at = @At(value = "INVOKE", target = "Lnet/minecraft/TileEntityHopper;canInsertItemToInventory(Lnet/minecraft/IInventory;Lnet/minecraft/ItemStack;II)Z", shift = At.Shift.AFTER), cancellable = true)
    private static void insertIntoCrate(IInventory inventory, ItemStack stack, int par2, int par3, CallbackInfoReturnable<ItemStack> cir) {
        if (inventory instanceof TileEntityCrate te) {
            cir.setReturnValue(te.insertStack(stack));
        }
    }

    @ModifyExpressionValue(method = "insertStackFromInventory", at = @At(value = "INVOKE", target = "Lnet/minecraft/TileEntityHopper;canExtractItemFromInventory(Lnet/minecraft/IInventory;Lnet/minecraft/ItemStack;II)Z"))
    private static boolean preventHopperDupe(boolean original, @Local(argsOnly = true) Hopper hopper, @Local(ordinal = 0) ItemStack stack) {
        return original && !isHopperInventoryFull(hopper, stack);
    }

    @Unique
    private static boolean isHopperInventoryFull(Hopper hopper, ItemStack stack) {
        int size = hopper.getSizeInventory();
        for (int i = 0; i < size; i++) {
            ItemStack stackAt = hopper.getStackInSlot(i);
            if (stackAt == null) return false;

            boolean sameItem = stackAt.itemID == stack.itemID;
            boolean sameMeta = stackAt.getItemSubtype() == stack.getItemSubtype();
            boolean underStackLimit = stackAt.stackSize < stack.getMaxStackSize();
            if (sameItem && sameMeta && underStackLimit) {
                return false;
            }
        }
        return true;
    }
}
