package com.github.jeffyjamzhd.renewed.mixins.backpack;

import com.github.jeffyjamzhd.renewed.api.IInventoryPlayer;
import com.github.jeffyjamzhd.renewed.item.ItemWithInventory;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.EntityPlayer;
import net.minecraft.InventoryPlayer;
import net.minecraft.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(InventoryPlayer.class)
public abstract class InventoryPlayerMixin implements IInventoryPlayer {
    @Shadow public abstract boolean addItemStackToInventory(ItemStack par1ItemStack);
    @Shadow public EntityPlayer player;
    @Shadow public abstract ItemStack getStackInSlot(int par1);
    @Unique boolean mr$stackFromWorld = false;
    @Unique boolean[] mr$shouldAnimate = new boolean[9];

    @ModifyReturnValue(method = "addItemStackToInventory", at = @At(
            value = "RETURN",
            ordinal = 4))
    private boolean addSingleStackToBackpack(boolean original, @Local(argsOnly = true) ItemStack stack) {
        if (!original && mr$stackFromWorld)
            return putStackInBackpacks(stack);
        return original;
    }

    @ModifyReturnValue(method = "addItemStackToInventory", at = @At(
            value = "RETURN",
            ordinal = 6))
    private boolean addStackToBackpack(boolean original, @Local(argsOnly = true) ItemStack stack) {
        if (!original && mr$stackFromWorld)
            return putStackInBackpacks(stack);
        return original;
    }

    @Inject(method = "onInventoryChanged", at = @At("TAIL"))
    private void updatePlayerBackpackItemCount(CallbackInfo ci) {
        player.mr$updateBackpackItemCount((InventoryPlayer) (Object) this);
    }

    @Unique
    private boolean validForBackpackInsertion() {
        for (int i = 0; i < 9; i++) {
            ItemStack at = getStackInSlot(i);
            if (at == null) continue;
            if (!(at.getItem() instanceof ItemWithInventory)) continue;
            return true;
        }
        return false;
    }

    @Unique
    private boolean putStackInBackpacks(ItemStack stack) {
        boolean modified = false;

        for (int i = 0; i < 9; i++) {
            ItemStack at = getStackInSlot(i);
            if (at == null) continue;
            if (!(at.getItem() instanceof ItemWithInventory inv)) continue;

            int oldCount = stack.stackSize;
            stack.stackSize = inv.putStackInInventory(at, stack, player, player.getWorld());

            if (oldCount != stack.stackSize) {
                modified = true;
                this.mr$shouldAnimate[i] = true;
            }
            if (stack.stackSize == 0) break;
        }
        return modified;
    }

    @Override
    public boolean mr$addStackFromWorld(ItemStack stack) {
        mr$stackFromWorld = true;
        boolean result = this.addItemStackToInventory(stack);
        mr$stackFromWorld = false;
        return result;
    }

    @Override
    public boolean mr$slotNeedsToAnimate(byte at) {
        boolean value = this.mr$shouldAnimate[at];
        this.mr$shouldAnimate[at] = false;
        return value;
    }
}
