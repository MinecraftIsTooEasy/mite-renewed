package com.github.jeffyjamzhd.renewed.item;

import com.github.jeffyjamzhd.renewed.entity.EntityPolearm;
import com.github.jeffyjamzhd.renewed.registry.RenewedMaterial;
import net.minecraft.*;

public class ItemPolearm extends ItemSword {
    private final ResourceLocation handTexture;
    public Icon handIcon;

    protected ItemPolearm(int id, Material material, ResourceLocation handTexture) {
        super(id, material);
        this.setCreativeTab(CreativeTabs.tabCombat);
        this.setReachBonus(1.0F);
        this.handTexture = handTexture;
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
    public int getNumComponentsForDurability() {
        return 1;
    }

    @Override
    public float getBaseDamageVsEntity() {
        return this.isPrimitive() ? 0.5F : 1.0F;
    }

    @Override
    public String getToolType() {
        return "polearm";
    }

    @Override
    public float getBaseDecayRateForAttackingEntity(ItemStack item_stack) {
        return this.isPrimitive() ? 0.25F : 0.5F;
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

                EntityPolearm entityPolearm = new EntityPolearm(
                        world, player, fraction_pulled * .8F,
                        this, item_stack.getItemDamage() + item_stack.getScaledDamage(.5F), item_stack.getEnchantmentTagList());

                if (fraction_pulled == 1.0F) {
                    entityPolearm.setIsCritical(true);
                }

                int sharp = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, item_stack);
                entityPolearm.setDamage(this.getCombinedDamageVsEntity() + 2.0F);

                int flame = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, item_stack);
                if (flame > 0) {
                    entityPolearm.setFire(100);
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
    public Icon getIconFromSubtypeForRenderPass(int par1, int par2) {
        return par2 == 2 ? this.handIcon : this.itemIcon;
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public void registerIcons(IconRegister register) {
        super.registerIcons(register);
        handIcon = register.registerIcon(handTexture.toString());
    }

    @Override
    public EnumItemInUseAction getItemInUseAction(ItemStack par1ItemStack, EntityPlayer player) {
        return EnumItemInUseAction.BOW;
    }

    public boolean isPrimitive() {
        return this.hasMaterial(Material.flint, RenewedMaterial.bone, Material.obsidian);
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
