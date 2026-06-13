package com.github.jeffyjamzhd.renewed.mixins.backpack;

import baubles.api.BaublesApi;
import baubles.common.container.InventoryBaubles;
import com.github.jeffyjamzhd.renewed.api.IEntityPlayer;
import com.github.jeffyjamzhd.renewed.item.ItemWithInventory;
import com.github.jeffyjamzhd.renewed.registry.RenewedEnchantments;
import net.minecraft.*;
import net.xiaoyu233.fml.FishModLoader;
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

    /**
     * Amount of items currently stored within backpacks
     * the player is currently holding
     */
    @Unique private int jbp$itemsInsideBackpacks = 0;

    @Inject(method = "onLivingUpdate", at = @At("HEAD"))
    public void runAddonUpdates(CallbackInfo ci) {
        mr$updateBackpackItemCount(this.inventory);
//        if (!getEntityWorld().isRemote)
//            jbp$updateDamageOverTime();
    }

//    @Unique
//    public void jbp$updateDamageOverTime() {
//        ArrayList<StatusEffect> statusEffectList = getAllActiveStatusEffects();
//
//        // Clear out old effects
//        for (StatusEffect status : jbp$ticksUntilNextDOT.keySet()) {
//            if (!statusEffectList.contains(status)) {
//                jbp$ticksUntilNextDOT.remove(status);
//            }
//        }
//
//        // Iterate through effects
//        for (StatusEffect status : statusEffectList) {
//            // Check if already exists
//            if (jbp$ticksUntilNextDOT.containsKey(status)) {
//                // Get information
//                int value = jbp$ticksUntilNextDOT.get(status);
//                int damageValue = status.jbp$getDamageOverTimeDamage();
//                DamageSource source = status.jbp$getDamageOverTimeSource();
//
//                // Decrement timer
//                value--;
//                jbp$ticksUntilNextDOT.put(status, value);
//
//                // Reset ticks if needed
//                if (value <= 0) {
//                    // Get ticks
//                    int reset = status.jbp$getDamageOverTimeTicks();
//
//                    // Damage player and reset timer
//                    this.attackEntityFrom(source, damageValue);
//                    this.getEntityWorld()
//                            .playSoundAtEntity(this, JBSounds.CRUSHED.sound(), 0.8F, 0.6F + this.rand.nextFloat() * 0.1F);
//                    jbp$ticksUntilNextDOT.put(status, reset);
//                }
//
//            } else if (status.jbp$getDamageOverTimeTicks() > 0) {
//                // If it doesn't exist, but should...
//                // Damage player and set timer
//                DamageSource source = status.jbp$getDamageOverTimeSource();
//                int damage = status.jbp$getDamageOverTimeDamage();
//                int ticks = status.jbp$getDamageOverTimeTicks();
//
//                this.attackEntityFrom(source, damage);
//                this.getEntityWorld()
//                        .playSoundAtEntity(this, JBSounds.CRUSHED.sound(), 0.8F, 0.6F + this.rand.nextFloat() * 0.1F);
//                jbp$ticksUntilNextDOT.put(status, ticks);
//            }
//        }
//    }

    @Inject(method = "clonePlayer", at = @At("TAIL"))
    private void cloneNBTOfBackpack(EntityPlayer player, boolean full, CallbackInfo ci) {
        if (full || this.worldObj.getGameRules().getGameRuleBooleanValue("keepInventory")) return;

        for (int i = 0; i < player.inventory.getSizeInventory(); i++) {
            ItemStack item = player.inventory.getStackInSlot(i);
            if (item == null) continue;
            if (RenewedEnchantments.ENCHANTMENT_SOUL_BOUND.getLevel(item) == 0) continue;

            this.inventory.setInventorySlotContents(i, item.copy());
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
        if (FishModLoader.hasMod("baubles")) {
            InventoryBaubles baubles = (InventoryBaubles) BaublesApi.getBaubles((EntityPlayer) (Object) this);
            ItemStack back = baubles.getStackInSlot(2);

            if (back != null && back.getItem() instanceof ItemWithInventory inv) {
                total += inv.getItemCountInStack(back, true);
            }
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
