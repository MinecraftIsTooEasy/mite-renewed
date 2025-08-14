package com.github.jeffyjamzhd.renewed.render;

import com.github.jeffyjamzhd.renewed.item.ItemPolearm;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;

public abstract class ItemRenderPolearm {
    public static void renderPolearm(ItemRenderer instance, EntityPlayer player, ItemStack heldItem, float par1, float par2) {
        float var21 = 0.8F;
        float var20 = player.getSwingProgress(par1);
        float var22 = MathHelper.sin(MathHelper.sqrt_float(var20) * (float)Math.PI);
        float var14 = 0.9F;
        GL11.glTranslatef(.2F + -var22 * 0.2F, -.15F + var22 * .25F, -0.2F + -var22 * var22 * 0.8F);

        GL11.glTranslatef(0.6F * var21, -1.05F * var21 - (1.0F - par2) * 0.6F, -0.5F * var21);
        GL11.glScalef(var14 - 0.35F, var14, var14);
        GL11.glRotatef(50F * var22, 0F, 0F, 1F);
        GL11.glRotatef(-10F * var22 * var22, 1F, 0F, 0F);
        GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(32826);

        //GL11.glRotatef(-var22 * 20.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-var22 * 30.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(-var22 * -20.0F, 1.0F, 0.0F, 0.0F);

        // Readying spear
        if (player.getItemInUseCount() > 0) {
            GL11.glRotatef(-18.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-12.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-8.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(-0.6F, 0.6F, 0.0F);
            float var16 = (float)heldItem.getMaxItemUseDuration() - ((float)player.getItemInUseCount() - par1 + 1.0F);
            float var17 = var16 / (float) ItemPolearm.getTicksForMaxPull(heldItem);
            var17 = (var17 * var17 + var17 * 2.0F) / 3.0F;
            if (var17 > 1.0F) {
                var17 = 1.0F;
            }

            if (var17 > 0.1F) {
                GL11.glTranslatef(0.0F, MathHelper.sin((var16 - 0.1F) * 1.3F) * 0.01F * (var17 - 0.1F), 0.0F);
            }

            GL11.glTranslatef(0.0F, 0.0F, var17 * 0.25F);
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

        instance.renderItem(player, heldItem, 2);
    }

    public static void renderPolearm3D(AbstractClientPlayer player) {
        GL11.glRotatef(player.getItemInUseDuration() > 0 ? 180F : 0, 0F, 1F, 0F);
        GL11.glRotatef(player.getItemInUseDuration() > 0 ? 180F : 0, 0F, 0F, 1F);
        GL11.glTranslatef(-.05F, player.getItemInUseDuration() > 0 ? .5F : .7F, player.getItemInUseDuration() > 0 ? -.275F : -0.3125F);
        GL11.glRotatef(180F, 0F, 0F, 1F);
        GL11.glRotatef(-20F, 1F, 0F, 0F);
        GL11.glRotatef(40F, 0F, 1.0F, 0F);
    }


    public static void handlePlayerArms(Entity entity, ModelRenderer leftArm, ModelRenderer rightArm, float idle, float delta) {
        EntityPlayer player = entity.getAsPlayer();
        float frac = ItemPolearm.getFractionPulled(player.getHeldItemStack(), player.getItemInUseCount());
        float factor = 1F - (float) Math.pow(1F - frac, 3);

        leftArm.rotateAngleX += -.2F + MathHelper.cos(idle * 0.5F) * 0.02F;
        rightArm.rotateAngleX += (-3F + MathHelper.cos(idle * 0.2F) * 0.02F) * factor;
    }
}
