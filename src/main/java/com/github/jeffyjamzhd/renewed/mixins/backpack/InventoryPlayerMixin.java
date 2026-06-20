package com.github.jeffyjamzhd.renewed.mixins.backpack;

import com.github.jeffyjamzhd.renewed.api.IInventoryPlayer;
import com.github.jeffyjamzhd.renewed.item.ItemWithInventory;
import com.github.jeffyjamzhd.renewed.registry.RenewedEnchantments;
import com.github.jeffyjamzhd.renewed.util.ItemUtils;
import com.jeffyjamzhd.jeffylib.api.impl.IItem;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.*;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryPlayer.class)
public abstract class InventoryPlayerMixin implements IInventoryPlayer {
    @Shadow public abstract boolean addItemStackToInventory(ItemStack par1ItemStack);
    @Shadow public EntityPlayer player;
    @Shadow public abstract ItemStack getStackInSlot(int par1);

    @Shadow
    public ItemStack[] mainInventory;
    @Unique boolean mr$stackFromWorld = false;
    @Unique boolean[] mr$shouldAnimate = new boolean[9];
    @Unique ItemStack mr$soulboundStack;

    @Inject(method = "addItemStackToInventory", at = @At(
            value = "INVOKE", target = "Lnet/minecraft/ItemStack;isItemDamaged()Z"),
            cancellable = true)
    private void addStackToVacuumBackpack(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (mr$stackFromWorld) {
            if (putStackInBackpacks(stack, true)) cir.setReturnValue(true);
        }
    }

    @ModifyReturnValue(method = "addItemStackToInventory", at = @At(
            value = "RETURN",
            ordinal = 4))
    private boolean addSingleStackToBackpack(boolean original, @Local(argsOnly = true) ItemStack stack) {
        if (!original && mr$stackFromWorld)
            return putStackInBackpacks(stack, false);
        return original;
    }

    @ModifyReturnValue(method = "addItemStackToInventory", at = @At(
            value = "RETURN",
            ordinal = 6))
    private boolean addStackToBackpack(boolean original, @Local(argsOnly = true) ItemStack stack) {
        if (!original && mr$stackFromWorld)
            return putStackInBackpacks(stack, false);
        return original;
    }

    @Inject(method = "onInventoryChanged", at = @At("TAIL"))
    private void updatePlayerBackpackItemCount(CallbackInfo ci) {
        player.mr$updateBackpackItemCount((InventoryPlayer) (Object) this);
    }

    @Inject(method = "clearInventory", at = @At(value = "FIELD", target = "Lnet/minecraft/InventoryPlayer;mainInventory:[Lnet/minecraft/ItemStack;", shift = At.Shift.AFTER, opcode = Opcodes.GETFIELD, ordinal = 2))
    private void keepBackpackFromClear(int par1, int par2, CallbackInfoReturnable<Integer> cir,
                              @Local ItemStack stack) {
        if (hasSoulbound(stack)) {
            this.mr$soulboundStack = stack;
        }
    }

    @Inject(method = "takeDamage(Lnet/minecraft/ItemStack;Lnet/minecraft/DamageSource;F)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/ItemDamageResult;itemWasDestroyed()Z"))
    private void onDestroyedHook(ItemStack stack, DamageSource source, float amount,
                                                  CallbackInfoReturnable<Boolean> cir,
                                                  @Local ItemDamageResult idr) {
        if (stack.getItem() instanceof IItem item && idr.itemWasDestroyed()) {
            item.jl$onItemDestroyed(stack, player.getWorld(), player.posX, player.posY, player.posZ);
        }
    }

    @Inject(method = "clearInventory", at = @At(value = "FIELD", target = "Lnet/minecraft/InventoryPlayer;mainInventory:[Lnet/minecraft/ItemStack;", ordinal = 1, shift = At.Shift.AFTER))
    private void revertBackpackClear(int par1, int par2, CallbackInfoReturnable<Integer> cir,
                                     @Local(ordinal = 2) LocalIntRef removedCount,
                                     @Local(ordinal = 3) int at) {
        if (hasSoulbound(this.mr$soulboundStack)) {
            // Revert removal
            removedCount.set(removedCount.get() - this.mr$soulboundStack.getMaxStackSize());
            this.mainInventory[at - 1] = this.mr$soulboundStack;
            this.mr$soulboundStack = null;
        }
    }

    @WrapOperation(method = "dropAllItems", at = @At(value = "INVOKE", target = "Lnet/minecraft/EntityPlayer;dropPlayerItemWithRandomChoice(Lnet/minecraft/ItemStack;Z)Lnet/minecraft/EntityItem;", ordinal = 0))
    private EntityItem keepBackpackFromDrop(EntityPlayer instance, ItemStack stack, boolean _unused, Operation<EntityItem> original) {
        if (hasSoulbound(stack)) {
            this.mr$soulboundStack = stack;
            return null;
        }
        return original.call(instance, stack, _unused);
    }

    @Inject(method = "dropAllItems", at = @At(value = "FIELD", target = "Lnet/minecraft/InventoryPlayer;mainInventory:[Lnet/minecraft/ItemStack;", ordinal = 1))
    private void keepBackpackFromDropArray(CallbackInfo ci, @Local int indice) {
        if (hasSoulbound(this.mr$soulboundStack)) {
            this.mainInventory[indice - 1] = this.mr$soulboundStack;
            this.mr$soulboundStack = null;
        }
    }

    @Unique
    private boolean hasSoulbound(ItemStack stack) {
        if (stack == null) return false;

        boolean isBag = stack.getItem() instanceof ItemWithInventory;
        boolean hasSoulbound = RenewedEnchantments.ENCHANTMENT_SOUL_BOUND.getLevel(stack) > 0;
        return isBag && hasSoulbound;
    }

    @Unique
    private boolean hasVacuum(ItemStack stack) {
        if (stack == null) return false;

        boolean isBag = stack.getItem() instanceof ItemWithInventory;
        boolean hasVacuum = RenewedEnchantments.ENCHANTMENT_VACUUM.getLevel(stack) > 0;
        return isBag && hasVacuum;
    }


    @Unique
    private boolean putStackInBackpacks(ItemStack stack, boolean checkVacuum) {
        boolean modified = false;

        // Check bauble first (if exists)
        ItemStack back = ItemUtils.getBaubleInBackSlot(player);
        if (back != null && back.getItem() instanceof ItemWithInventory inv && (!checkVacuum || hasVacuum(back))) {
            int oldCount = stack.stackSize;
            stack.stackSize = checkVacuum ?
                    inv.putStackInInventoryMatching(back, stack, player, player.getWorld()) :
                    inv.putStackInInventory(back, stack, player, player.getWorld());

            if (oldCount != stack.stackSize) {
                modified = true;
            }
            if (stack.stackSize == 0) return modified;
        }

        // Check player's hotbar next
        ItemStack[] hotbarBackpacks = getBackpacksInHotbar();
        for (int i = 0; i < 9; i++) {
            ItemStack backpack = hotbarBackpacks[i];
            if (backpack == null) continue;
            if (checkVacuum && !hasVacuum(backpack)) continue;
            ItemWithInventory inv = (ItemWithInventory) backpack.getItem();

            int oldCount = stack.stackSize;
            stack.stackSize = checkVacuum ?
                    inv.putStackInInventoryMatching(backpack, stack, player, player.getWorld()) :
                    inv.putStackInInventory(backpack, stack, player, player.getWorld());

            if (oldCount != stack.stackSize) {
                modified = true;
                this.mr$shouldAnimate[i] = true;
            }
            if (stack.stackSize == 0) break;
        }

        return modified;
    }

    @Unique
    private ItemStack[] getBackpacksInHotbar() {
        ItemStack[] backpacks = new ItemStack[9];

        for (int i = 0; i < 9; i++) {
            ItemStack at = getStackInSlot(i);
            if (at == null) continue;
            if (!(at.getItem() instanceof ItemWithInventory inv)) continue;

            backpacks[i] = at;
        }

        return backpacks;
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
