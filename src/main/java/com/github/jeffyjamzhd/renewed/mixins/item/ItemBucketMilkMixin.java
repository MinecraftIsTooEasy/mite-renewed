package com.github.jeffyjamzhd.renewed.mixins.item;

import com.github.jeffyjamzhd.renewed.item.ItemRenewedBucket;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemBucketMilk.class)
abstract class ItemBucketMilkMixin extends ItemVessel implements IDamageableItem {
    public ItemBucketMilkMixin(int id, Material vessel_material, Material contents_material, int standard_volume, int max_stack_size_empty, int max_stack_size_full, String texture) {
        super(id, vessel_material, contents_material, standard_volume, max_stack_size_empty, max_stack_size_full, texture);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void setDamage(int id, Material material, CallbackInfo ci) {
        ((ItemBucketMilk)(Object)(this)).setMaxDamage(ItemRenewedBucket.getDurabilityForMaterial(material));
    }

    @ModifyArg(method = "onItemRightClick", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/EntityPlayer;convertOneOfHeldItem(Lnet/minecraft/ItemStack;)V"))
    private ItemStack carryDamageOver(ItemStack newStack, @Local(argsOnly = true) EntityPlayer player) {
        ItemStack heldStack = player.getHeldItemStack();
        if (heldStack != null && newStack != null) {
            newStack.setItemDamage(heldStack.getItemDamage());
        }
        return newStack;
    }

    @Override
    public int getNumComponentsForDurability() {
        return 0;
    }

    @Override
    public int getRepairCost() {
        return 0;
    }

    @Override
    public boolean isRepairable() {
        return false;
    }

    @Override
    public Item getRepairItem() {
        return null;
    }

    @Override
    public Material getMaterialForRepairs() {
        return null;
    }

    @Override
    public boolean hasRepairCost() {
        return false;
    }

    @Override
    public boolean mr$usableInCrafting(ItemStack stack) {
        return true;
    }
}
