package com.github.jeffyjamzhd.renewed.mixins.backpack;

import com.github.jeffyjamzhd.renewed.api.IEntityPlayer;
import com.github.jeffyjamzhd.renewed.item.ItemWithInventory;
import com.github.jeffyjamzhd.renewed.registry.RenewedEnchantments;
import com.github.jeffyjamzhd.renewed.registry.RenewedPotion;
import com.github.jeffyjamzhd.renewed.util.ItemUtils;
import com.jeffyjamzhd.jeffylib.api.impl.IItem;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayer.class)
public abstract class EntityPlayerMixin extends EntityLivingBase implements IEntityPlayer {
    public EntityPlayerMixin(World par1World) {
        super(par1World);
    }

    @Shadow public InventoryPlayer inventory;

    @Shadow
    public abstract World getEntityWorld();

    /**
     * Amount of items currently stored within backpacks
     * the player is currently holding
     */
    @Unique private int jbp$itemsInsideBackpacks = 0;

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    public void runBackpackUpdates(CallbackInfo ci) {
        mr$updateBackpackItemCount(this.inventory);
        if (!getEntityWorld().isRemote)
            jbp$updateDamageOverTime();
    }

    @Inject(method = "onLivingUpdate", at = @At("TAIL"))
    public void updateSpeed(CallbackInfo ci) {
        PotionEffect effect = this.getActivePotionEffect(RenewedPotion.ENCUMBERED.id);
        if (effect != null) {
            int amplifier = effect.getAmplifier();
            if (amplifier == 0) return;

            this.jumpMovementFactor /= amplifier * 2;
        }
    }

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    private void preventJumpIfSuperEncumbered(CallbackInfo ci) {
        PotionEffect effect = this.getActivePotionEffect(RenewedPotion.ENCUMBERED.id);
        if (effect != null) {
            int amplifier = effect.getAmplifier();
            if (amplifier > 1) {
                ci.cancel();
            }
        }
    }

    @Override
    public boolean isSprinting() {
        PotionEffect effect = this.getActivePotionEffect(RenewedPotion.ENCUMBERED.id);
        if (effect != null) {
            return false;
        }
        return super.isSprinting();
    }

    @Unique
    public void jbp$updateDamageOverTime() {
        int encumberedLevel = MathHelper.clamp_int((int) Math.floor((this.jbp$itemsInsideBackpacks - 27) / 9F), -1, 3);
        if (encumberedLevel < 0) return;
        if (this.ticksExisted % 20 != 0) return;

        this.addPotionEffect(new PotionEffect(RenewedPotion.ENCUMBERED.id, 100, encumberedLevel));
        if (encumberedLevel > 0) {
            this.addPotionEffect(new PotionEffect(Potion.digSlowdown.id, 100, encumberedLevel - 1));
        }
    }

    @Inject(method = "clonePlayer", at = @At("TAIL"))
    private void cloneNBTOfBackpack(EntityPlayer player, boolean full, CallbackInfo ci) {
        if (full || this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) return;

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack item = player.inventory.getStackInSlot(i);
            if (item == null) continue;
            if (RenewedEnchantments.ENCHANTMENT_SOUL_BOUND.getLevel(item) == 0) continue;

            ItemStack clone = item.copy();
            ItemDamageResult result = clone.tryDamageItem(DamageSource.pepsin, Math.round(24 - player.rand.nextFloat() * 6), player);

            if (result != null && result.itemWasDestroyed()) {
                IItem ext = (IItem) clone.getItem();
                ext.jl$onItemDestroyed(item, player.getWorld(), player.posX, player.posY, player.posZ);
            } else {
                this.inventory.setInventorySlotContents(i, clone);
            }
        }
    }

    @Unique
    @Override
    public void mr$updateBackpackItemCount(InventoryPlayer inventory) {
        int total = 0;

        // Check stack at cursor
        ItemStack cursorStack = inventory.getItemStack();
        if (cursorStack != null && cursorStack.getItem() instanceof ItemWithInventory inv) {
            total += inv.getItemCountInStack(cursorStack, true);
        }

        // Check bauble
        ItemStack backSlot = ItemUtils.getBaubleInBackSlot((EntityPlayer) (Object) this);
        if (backSlot != null && backSlot.getItem() instanceof ItemWithInventory inv) {
            total += inv.getItemCountInStack(backSlot, true);
        }

        // Iterate through main inventory
        for (ItemStack stack : inventory.mainInventory) {
            if (stack == null)
                continue;

            // Add to total if item is valid
            if (stack.getItem() instanceof ItemWithInventory inv) {
                total += inv.getItemCountInStack(stack, true);
            } else {
                total += 1;
            }
        }

        jbp$itemsInsideBackpacks = Math.max(0, total - 40);
    }

    @Override
    public int mr$getBackpackItemCount() {
        return jbp$itemsInsideBackpacks;
    }
}
