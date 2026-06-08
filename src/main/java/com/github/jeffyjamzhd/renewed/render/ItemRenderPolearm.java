package com.github.jeffyjamzhd.renewed.render;

import com.github.jeffyjamzhd.renewed.item.ItemPolearm;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

public abstract class ItemRenderPolearm {
    public static void renderPolearm(ItemRenderer instance, EntityPlayer player, ItemStack heldItem, float partialTicks, float equipProgress) {
        // Base constants for scaling and offset
        float positionOffset = 1F;
        float itemScale = 0.9F;
        float swingProgress = player.getSwingProgress(partialTicks);
        float swingAnim = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);
        float weightDrop = -MathHelper.sin(swingProgress * (float)Math.PI);

        GL11.glTranslatef(0.2F - (swingAnim * 0.25F), -0.1F - (weightDrop * 0.2F), -0.2F - (swingAnim * 1.2F));
        GL11.glTranslatef(0.4F * positionOffset, -.95F * positionOffset - (1.0F - equipProgress) * 0.6F, -0.5F * positionOffset);
        GL11.glScalef(itemScale - 0.35F, itemScale, itemScale);

        GL11.glRotatef(65.0F * swingAnim, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(15.0F * swingAnim + 10.0F * weightDrop, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(30.0F * swingAnim, 0.0F, 1.0F, 0.0F);

        GL11.glRotatef(35.0F, 0.0F, 1.0F, 0.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        GL11.glRotatef(-swingAnim * 60.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(swingAnim * 30.0F, 1.0F, 0.0F, 0.0F);

        // Readying spear
        if (player.getItemInUseCount() > 0) {
            GL11.glRotatef(-18.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-12.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(-8.0F, 1.0F, 0.0F, 0.0F);
            GL11.glTranslatef(-0.6F, 0.6F, 0.0F);

            // Calculate how long the item has been in use
            float ticksInUse = (float)heldItem.getMaxItemUseDuration() - ((float)player.getItemInUseCount() - partialTicks + 1.0F);
            float chargeProgress = ticksInUse / (float) ItemPolearm.getTicksForMaxPull(heldItem);

            // Smooth out the charge progress curve
            chargeProgress = (chargeProgress * chargeProgress + chargeProgress * 2.0F) / 3.0F;
            if (chargeProgress > 1.0F) {
                chargeProgress = 1.0F;
            }

            // Apply shaking effect when fully drawn/charged
            if (chargeProgress > 0.1F) {
                float shakeForce = MathHelper.sin((ticksInUse - 0.1F) * 1.3F) * 0.01F * (chargeProgress - 0.1F);
                GL11.glTranslatef(0.0F, shakeForce, 0.0F);
            }

            // Drawback
            GL11.glTranslatef(0.0F, 0.0F, chargeProgress * 0.25F);
            GL11.glRotatef(-335.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-50.0F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(0.0F, 0.5F, 0.0F);

            float stretchScale = 1.0F + chargeProgress * 0.2F;
            GL11.glScalef(1.0F, 1.0F, stretchScale);

            // Revert
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
        // 1. Grab the raw integer use count
        int useCount = player.getItemInUseCount();

        // 2. Interpolate the value using delta!
        // Because useCount counts DOWN, we subtract delta to smoothly bridge the gap between ticks.
        float smoothUseCount = (float) useCount;
        if (useCount > 0) {
            smoothUseCount -= delta;
        }

        // 3. Calculate the fraction using the smoothed float
        // Note: You will need to change your getFractionPulled method to accept a 'float' instead of an 'int'!
        float frac = ItemPolearm.getFractionPulledAnimation(player.getHeldItemStack(), smoothUseCount);

        // Your cubic easing function (this is a great way to make it feel heavy/snappy by the way!)
        float factor = 1F - (float) Math.pow(1F - frac, 3);

        // Apply rotations
        leftArm.rotateAngleX += -.2F + MathHelper.cos(idle * 0.5F) * 0.02F;
        rightArm.rotateAngleX += (-3F + MathHelper.cos(idle * 0.2F) * 0.02F) * factor;
    }
}
