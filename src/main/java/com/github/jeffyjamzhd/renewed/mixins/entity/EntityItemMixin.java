package com.github.jeffyjamzhd.renewed.mixins.entity;

import com.github.jeffyjamzhd.renewed.item.ItemRenewedFood;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityItem.class)
public abstract class EntityItemMixin extends Entity {
    public EntityItemMixin(World par1World) {
        super(par1World);
    }

    @Shadow public abstract ItemStack getEntityItem();

    @Shadow private float cooking_progress;

    @Shadow public abstract void setEntityItemStack(ItemStack par1ItemStack);

    @Inject(method = "attackEntityFrom", at = @At(value = "INVOKE", target = "Lnet/minecraft/Damage;isFireDamage()Z", ordinal = 1), cancellable = true)
    private void addSupportForExtendedFood(
            Damage damage,
            CallbackInfoReturnable<EntityDamageResult> cir,
            @Local(name = "result") EntityDamageResult result) {
        ItemStack stack = this.getEntityItem();
        if (damage.isFireDamage() && stack.getItem() instanceof ItemRenewedFood item_food) {
            if (item_food.cookedFood != null || item_food.uncookedFood != null) {
                if (item_food.cookedFood != null) {
                    int x = this.getBlockPosX();
                    int y = this.getBlockPosY();
                    int z = this.getBlockPosZ();

                    for (int dx = -1; dx <= 1; ++dx) {
                        for (int dz = -1; dz <= 1; ++dz) {
                            Block block = this.worldObj.getBlock(x + dx, y, z + dz);
                            if (block == Block.fire) {
                                this.worldObj.getAsWorldServer().addScheduledBlockOperation(EnumBlockOperation.try_extinguish_by_items, x + dx, y, z + dz, (this.worldObj.getTotalWorldTime() / 10L + 1L) * 10L, false);
                            }
                        }
                    }
                }

                this.cooking_progress += damage.getAmount() * 3.0F;
                if (this.cooking_progress >= 100.0F) {
                    ItemStack cooked_item_stack = item_food.getItemProducedWhenDestroyed(stack, damage.getSource());
                    if (cooked_item_stack == null) {
                        this.setDead();
                        cir.setReturnValue(result.setEntityWasDestroyed());
                        return;
                    }

                    if (item_food.isAnimalProduct()) {
                        this.playSound("imported.random.sizzle", 1.0F, 1.0F);
                    }

                    this.setEntityItemStack(cooked_item_stack);
                    int xp_reward = cooked_item_stack.getExperienceReward();

                    while (xp_reward > 0) {
                        int xp_share = EntityXPOrb.getXPSplit(xp_reward);
                        xp_reward -= xp_share;
                        this.worldObj.spawnEntityInWorld(new EntityXPOrb(this.worldObj, this.posX, this.posY + (double) 0.5F, this.posZ + (double) 0.5F, xp_share));
                    }
                }

                cir.setReturnValue(result.setEntityWasAffected());
            }
        }
    }
}
