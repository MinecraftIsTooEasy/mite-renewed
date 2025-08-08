package com.github.jeffyjamzhd.renewed.render;

import com.github.jeffyjamzhd.renewed.item.ItemPolearm;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;

public abstract class ItemRenderPolearm {
    public static void renderPolearm(ItemRenderer instance, EntityPlayer player, ItemStack heldItem, float par1, float par2) {
        float var21 = 0.8F;
        float var20 = player.getSwingProgress(par1);
        float var22 = MathHelper.sin(var20 * (float)Math.PI);
        float var13 = MathHelper.sin(MathHelper.sqrt_float(var20) * (float)Math.PI);
        GL11.glTranslatef(-var13 * 0.2F, 0F, -var22 * 0.6F);

        GL11.glTranslatef(0.6F * var21, -0.65F * var21 - (1.0F - par2) * 0.6F, -0.9F * var21);
        GL11.glRotatef(30F * var22, 0F, 0F, 1F);
        GL11.glRotatef(-10F * var22, 1F, 0F, 0F);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(32826);

        var22 = MathHelper.sin(var20 * var20 * (float)Math.PI);
        //GL11.glRotatef(-var22 * 20.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var13 * 20.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(-var13 * -20.0F, 1.0F, 0.0F, 0.0F);
        float var14 = 0.4F;
        GL11.glScalef(var14, var14, var14);

        // Readying spear
        if (player.getItemInUseCount() > 0) {
            GL11.glRotatef(-18.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-12.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-8.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(-0.9F, 0.2F, 0.0F);
            float var16 = (float)heldItem.getMaxItemUseDuration() - ((float)player.getItemInUseCount() - par1 + 1.0F);
            float var17 = var16 / (float) ItemPolearm.getTicksForMaxPull(heldItem);
            var17 = (var17 * var17 + var17 * 2.0F) / 3.0F;
            if (var17 > 1.0F) {
                var17 = 1.0F;
            }

            if (var17 > 0.1F) {
                GL11.glTranslatef(0.0F, MathHelper.sin((var16 - 0.1F) * 1.3F) * 0.01F * (var17 - 0.1F), 0.0F);
            }

            GL11.glTranslatef(0.0F, 0.0F, var17 * 0.1F);
            GL11.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.5F, 0.0F);
            float var18 = 1.0F + var17 * 0.2F;
            GL11.glScalef(1.0F, 1.0F, var18);
            GL11.glTranslatef(0.0F, -0.5F, 0.0F);
            GL11.glRotatef(50.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(335.0F, 0.0F, 0.0F, 1.0F);
        }

        if (heldItem.getItem().shouldRotateAroundWhenRendering()) {
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
        }

        instance.renderItem(player, heldItem, 0);
    }
}
