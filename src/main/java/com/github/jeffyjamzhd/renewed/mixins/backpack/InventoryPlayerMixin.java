package com.github.jeffyjamzhd.renewed.mixins.backpack;

import baubles.api.BaublesApi;
import baubles.common.container.InventoryBaubles;
import com.github.jeffyjamzhd.renewed.api.IInventoryPlayer;
import com.github.jeffyjamzhd.renewed.item.ItemWithInventory;
import com.github.jeffyjamzhd.renewed.registry.RenewedEnchantments;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import net.xiaoyu233.fml.FishModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

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
    private boolean putStackInBackpacks(ItemStack stack) {
        boolean modified = false;

        // Check baubles first (if it is loaded)
        if (FishModLoader.hasMod("baubles")) {
            InventoryBaubles baubles = (InventoryBaubles) BaublesApi.getBaubles(player);
            ItemStack back = baubles.getStackInSlot(2);

            if (back != null) {
                boolean isBackpack = back.getItem() instanceof ItemWithInventory;
                boolean hasVacuum = RenewedEnchantments.ENCHANTMENT_VACUUM.getLevel(back) > 0;

                if (isBackpack && hasVacuum) {
                    ItemWithInventory itemInv = (ItemWithInventory) back.getItem();
                    int oldCount = stack.stackSize;
                    stack.stackSize = itemInv.putStackInInventory(back, stack, player, player.getWorld());

                    if (oldCount != stack.stackSize) {
                        modified = true;
                    }
                    if (stack.stackSize == 0) return modified;
                }
            }
        }

        // Check player's hotbar next
        for (int i = 0; i < 9; i++) {
            ItemStack at = getStackInSlot(i);
            if (at == null) continue;
            if (!(at.getItem() instanceof ItemWithInventory inv)) continue;
            if (RenewedEnchantments.ENCHANTMENT_VACUUM.getLevel(at) == 0) continue;

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
