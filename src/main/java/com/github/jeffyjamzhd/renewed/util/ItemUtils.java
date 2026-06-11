package com.github.jeffyjamzhd.renewed.util;

import net.minecraft.EntityItem;
import net.minecraft.EntityPlayer;
import net.minecraft.ItemStack;
import net.minecraft.World;

public class ItemUtils {
    static public void ejectStackWithRandomVelocity(World world, double x, double y, double z, ItemStack stack)
    {
        EntityItem entityitem = new EntityItem(world, x, y, z, stack);

        float velocityFactor = 0.05F;

        entityitem.motionX = (float)world.rand.nextGaussian() * velocityFactor;
        entityitem.motionY = (float)world.rand.nextGaussian() * velocityFactor + 0.2F;
        entityitem.motionZ = (float)world.rand.nextGaussian() * velocityFactor;

        entityitem.delayBeforeCanPickup = 10;

        world.spawnEntityInWorld(entityitem);
    }

    static public void givePlayerStackOrEject(EntityPlayer player, ItemStack stack)
    {
        if ( player.inventory.addItemStackToInventory(stack))
        {
            player.worldObj.playSoundAtEntity( player, "random.pop", 0.2F,
                    ((player.rand.nextFloat() - player.rand.nextFloat()) * 0.7F + 1.0F) * 2.0F);
        }
        else if ( !player.worldObj.isRemote )
        {
            ejectStackWithRandomVelocity(player.worldObj, player.posX, player.posY, player.posZ, stack);
        }
    }

    static public boolean areItemsEqual(ItemStack item, ItemStack otherItem, boolean checkIfWildcard) {
        if (item == null || otherItem == null) return false;

        boolean sameID = item.itemID == otherItem.itemID;
        boolean sameMeta = item.getItemDamage() == otherItem.getItemDamage();
        boolean wildcard = checkIfWildcard && otherItem.getItemDamage() == Short.MAX_VALUE;
        return sameID && (wildcard || sameMeta);
    }
}
