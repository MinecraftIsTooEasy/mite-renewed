package com.github.jeffyjamzhd.renewed.item;

import com.github.jeffyjamzhd.renewed.entity.EntityPolearm;
import com.github.jeffyjamzhd.renewed.registry.RenewedMaterial;
import net.minecraft.*;

public class ItemPolearm extends ItemSword {

    protected ItemPolearm(int par1, Material material) {
        super(par1, material);
        this.setCreativeTab(CreativeTabs.tabCombat);
        this.setReachBonus(1.0F);
    }

    @Override
    public boolean hasQuality() {
        return true;
    }

    @Override
    public float getBaseHarvestEfficiency(Block block) {
        return 0.25F;
    }

    @Override
    public float getBaseDamageVsEntity() {
        return this.isPrimitive() ? 0.25F : 1.0F;
    }

    @Override
    public String getToolType() {
        return "polearm";
    }

    @Override
    public float getBaseDecayRateForAttackingEntity(ItemStack item_stack) {
        return 0.25F;
    }

    @Override
    public boolean canBlock() {
        return false;
    }

    @Override
    public boolean onItemRightClick(EntityPlayer player, float delta, boolean ctrl_pressed) {
        player.setHeldItemInUse();
        return true;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack item_stack, World world, EntityPlayer player, int item_in_use_count) {
        if (!world.isRemote) {
            float fraction_pulled = getFractionPulled(item_stack, item_in_use_count);
            fraction_pulled = (fraction_pulled * fraction_pulled + fraction_pulled * 2.0F) / 3.0F;
            if (!(fraction_pulled < 0.1F)) {
                if (fraction_pulled > 1.0F) {
                    fraction_pulled = 1.0F;
                }

                EntityPolearm entityPolearm = new EntityPolearm(world, player, fraction_pulled * .8F, this, item_stack.getItemDamage() + 30);
                if (fraction_pulled == 1.0F) {
                    entityPolearm.setIsCritical(true);
                }

                int power = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, item_stack);
                entityPolearm.setDamage(this.getBaseDamageVsEntity() + this.getMeleeDamageBonus() + (power * 0.5F) + 2.0F);

                int punch = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, item_stack);
                if (punch > 0) {
                    entityPolearm.setKnockback(punch);
                }

                world.playSoundAtEntity(player, "random.bow", 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + fraction_pulled * 0.5F);
                world.spawnEntityInWorld(entityPolearm);
                player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
            }
        }
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
        return 24000;
    }

    @Override
    public EnumItemInUseAction getItemInUseAction(ItemStack par1ItemStack, EntityPlayer player) {
        return EnumItemInUseAction.BOW;
    }

    public boolean isPrimitive() {
        return this.hasMaterial(Material.flint, RenewedMaterial.bone);
    }

    public static int getTicksForMaxPull(ItemStack item_stack) {
        return 10 - EnchantmentHelper.getEnchantmentLevelFractionOfInteger(Enchantment.quickness, item_stack, 10);
    }

    public static int getTicksPulled(ItemStack item_stack, int item_in_use_count) {
        return item_stack.getMaxItemUseDuration() - item_in_use_count;
    }

    public static float getFractionPulled(ItemStack item_stack, int item_in_use_count) {
        return Math.min((float)getTicksPulled(item_stack, item_in_use_count) / (float)getTicksForMaxPull(item_stack), 1.0F);
    }


}
