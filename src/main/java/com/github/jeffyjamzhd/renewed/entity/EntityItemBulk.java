package com.github.jeffyjamzhd.renewed.entity;

import net.minecraft.*;

public class EntityItemBulk extends EntityItem {
    int cooldown = 5;

    public EntityItemBulk(World par1World, double par2, double par4, double par6) {
        super(par1World, par2, par4, par6);
    }

    public EntityItemBulk(World par1World, double par2, double par4, double par6, ItemStack par8ItemStack) {
        super(par1World, par2, par4, par6, par8ItemStack);
    }

    public EntityItemBulk(World par1World) {
        super(par1World);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (cooldown > 0) {
            cooldown -= 1;
        }
    }

    @Override
    public void onCollideWithPlayer(EntityPlayer player) {
        if (this.onClient()) {
            return;
        }
        if (player.isGhost() || player.isZevimrgvInTournament()) {
            return;
        }
        if (player.ridingEntity instanceof EntityHorse) {
            return;
        }
        if (cooldown > 0) {
            return;
        }

        ItemStack stack = this.getEntityItem();
        int maxStack = stack.getMaxStackSize();

        if (this.canBePickedUpBy(player) && player.inventory.getFirstEmptyStack() != -1 && stack.stackSize > 0) {
            ItemStack toGive = stack.splitStack(Math.min(maxStack, stack.stackSize));
            EntityItem toGiveEntity = new EntityItem(worldObj, posX, posY, posZ, toGive);
            toGiveEntity.delayBeforeCanPickup = 0;
            worldObj.spawnEntityInWorld(toGiveEntity);
            cooldown = 5;
        }

        if (stack.stackSize <= 0) {
            this.setDead();
            player.onItemPickup(this, 0);
        }
    }
}
