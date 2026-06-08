package com.github.jeffyjamzhd.renewed.render;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.item.ItemHandpan;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;

public class ItemRenderHandpan {
    private static ResourceLocation TEX = new ResourceLocation(MiTERenewed.RESOURCE_ID + "textures/items/handpan/render.png");
    private static RenderBlocks blockRender = new RenderBlocks();
    private static float prevAnimX = 0F, prevAnimZ = 0F;

    static public void renderHandpan(float partialTicks, ItemStack item, EntityClientPlayerMP player, float equipTime, float playerPitch) {
        // Get information
        int subtype = item.getItemSubtype();
        int duration = player.getItemInUseDuration();
        int maxUse = ItemHandpan.getIntendedSpeed(item);
        float progress = ItemHandpan.getFractionUsing(item, duration);

        // Vanilla rendering constants
        float depthScale = 0.8F;
        float swingProgress = player.getSwingProgress(partialTicks);
        float swingSin = MathHelper.sin(swingProgress * (float)Math.PI);
        float swingSqrtSin = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);

        // Handle swing translation
        GL11.glTranslatef(0F, MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI * 2.0F) * 0.2F, -swingSin * 0.6F);

        // Calculate tilt based on player's looking pitch
        float tiltAngle = .4F - playerPitch / 120.0F + 0.1F;
        float armTiltAngle = .4F - playerPitch / 55.0F;

        if (tiltAngle < 0.0F) {
            tiltAngle = 0.0F;
        }
        if (tiltAngle > 1.0F) {
            tiltAngle = 1.0F;
        }

        // Calculate custom animation
        float value = duration > 0 ? progress * maxUse : 0;
        float animX = (float) ((Math.cos(value / 10F)) * .12F);
        float animZ = (float) ((Math.sin(value / 6F)) * .3F);
        animZ = prevAnimZ + (animZ - prevAnimZ) * partialTicks * 0.1f;
        animX = prevAnimX + (animX - prevAnimX) * partialTicks * 0.1f;

        // Prepare for render (apply easing to tilt)
        tiltAngle = -MathHelper.cos(tiltAngle * (float)Math.PI) * 0.2F + 0.5F;
        GL11.glTranslatef(0.0F, 0.0F * depthScale - (1.0F - equipTime) * 1.2F - tiltAngle * 0.8F + 0.04F, -0.9F * depthScale);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(tiltAngle * -85.0F, 0.0F, 0.0F, 1.0F);
        GL11.glEnable(32826); // GL_RESCALE_NORMAL
        Minecraft.getMinecraft().getTextureManager().bindTexture(player.getLocationSkin());

        // Translate and animate
        GL11.glTranslatef(.4F, .15F, 0F);
        GL11.glTranslatef(animX, 0, animZ);

        prevAnimX = animX;
        prevAnimZ = animZ;

        // Render both player arms (i = 0 is left arm, i = 1 is right arm)
        for(int i = 0; i < 2; ++i) {
            int armSide = i * 2 - 1; // Results in -1 for left, 1 for right
            GL11.glPushMatrix();
            GL11.glTranslatef(.25F + 0.2F * armTiltAngle, -.95F - 0.05F * armTiltAngle, .6F * (float)armSide);

            GL11.glRotatef(-50.0F + 10F * armTiltAngle, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef((float) (-25 * armSide), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef((float)(45 * armSide), 0.0F, 1.0F, 0.0F);

            Render render = RenderManager.instance.getEntityRenderObject(Minecraft.getMinecraft().thePlayer);
            RenderPlayer renderPlayer = (RenderPlayer)render;

            float armScale = 1.0F;
            GL11.glScalef(armScale, armScale, armScale);
            renderPlayer.renderFirstPersonArm(Minecraft.getMinecraft().thePlayer);
            GL11.glPopMatrix();
        }

        // Setup the handpan rendering transform
        GL11.glRotatef(armTiltAngle * -25.0F + 25F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(armTiltAngle * .25F, 0F, 0F);

        swingProgress = player.getSwingProgress(partialTicks); // Re-fetch for item offset
        swingSin = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
        swingSqrtSin = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI);

        float baseScale = 0.38F;
        GL11.glScalef(baseScale, baseScale, baseScale);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(50.0F, 1.0F, 0.0F, 0.0F);

        float pixelScale = 0.019625F; // Roughly 1/51, scaling the giant quads down to item size
        GL11.glTranslatef(-0F, .8F, 1.75F);
        GL11.glScalef(pixelScale, pixelScale, pixelScale);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TEX);
        Tessellator tsl = Tessellator.instance;

        // Draw base
        GL11.glNormal3f(0.0F, 0.0F, -1.0F);
        tsl.startDrawingQuads();
        tsl.addVertexWithUV(-64, 64, 0.0F, 0.5F, 0F);
        tsl.addVertexWithUV(64, 64, 0.0F, 1.0F, 0F);
        tsl.addVertexWithUV(64, -64, 0.0F, 1.0F, 0.5F);
        tsl.addVertexWithUV(-64, -64, 0.0F, .5F, .5F);
        tsl.draw();

        // Draw front rim
        GL11.glTranslatef(0.0F, 0.0F, 104);
        GL11.glNormal3f(1F, 0F, 0.0F);
        tsl.startDrawingQuads();
        tsl.addVertexWithUV(64, -64, 0F, 0.5F, 1F);
        tsl.addVertexWithUV(-64, -64, 0F, 1.0F, 1F);
        tsl.addVertexWithUV(-64, -64, -128.0F, 1.0F, 0.5F);
        tsl.addVertexWithUV(64, -64, -128.0F, .5F, .5F);
        tsl.draw();
        GL11.glTranslatef(0.0F, 0.0F, -104);

        // Draw back
        for (int side = 0; side < 4; ++side) {
            GL11.glNormal3f(1F, 0F, 0.0F);
            tsl.startDrawingQuads();
            tsl.addVertexWithUV(64, -64, -128.0F, 0.5F, 1F);
            tsl.addVertexWithUV(-64, -64, -128.0F, 1.0F, 1F);
            tsl.addVertexWithUV(-64, -64, 0F, 1.0F, 0.5F);
            tsl.addVertexWithUV(64, -64, 0F, .5F, .5F);
            tsl.draw();
            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
        }

        // Draw mesh
        renderMesh(tsl, subtype);

        // Draw block
        if (item.hasTagCompound() && item.getTagCompound().hasKey("handpanContent")) {
            // Get block ID from tags
            short blockID = item.getTagCompound().getShort("handpanContent");
            if (blockID == 0)
                return;

            // Get block and render
            Block block = Block.getBlock(blockID);
            renderBlock(tsl, progress, block);
        }
    }

    /**
     * Render mesh based on item subtype
     * @param tsl Tessellator instance
     * @param subtype Item subtype
     */
    static private void renderMesh(Tessellator tsl, int subtype) {
        if (subtype == 0)
            return;

        // Draw mesh
        float meshU1 = 0.0F;
        float meshU2 = 0.5F;
        float meshV0 = 0.0F + 0.5F * (subtype - 1);
        float meshV1 = 0.5F + 0.5F * (subtype - 1);
        GL11.glNormal3f(0.0F, 0.0F, -1.0F);
        GL11.glTranslatef(0.0F, 0.0F, -16);
        tsl.startDrawingQuads();
        tsl.addVertexWithUV(-64, 64, 0.0F, meshU1, meshV0);
        tsl.addVertexWithUV(64, 64, 0.0F, meshU2, meshV0);
        tsl.addVertexWithUV(64, -64, 0.0F, meshU2, meshV1);
        tsl.addVertexWithUV(-64, -64, 0.0F, meshU1, meshV1);
        tsl.draw();
    }

    static private void renderBlock(Tessellator tsl, float useTime, Block block) {
        // Clamp to prevent the block from glitching inside out at 0
        float blockHeight = Math.max(0.01F, useTime);

        GL11.glPushMatrix();
        GL11.glScaled(100, 100, 100);
        GL11.glRotatef(-90, 1, 0, 0);
        GL11.glTranslatef(0, 0.375F, 0);

        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

        blockRender.setRenderBounds(0D, 0D, 0D, 1D, blockHeight, 1D);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);

        tsl.startDrawingQuads();
        tsl.setNormal(0F, -1F, 0F);
        blockRender.renderFaceYNeg(block, 0D, 0D, 0D, block.getIcon(0, 0));
        tsl.draw();

        tsl.startDrawingQuads();
        tsl.setNormal(0F, 1F, 0F);
        blockRender.renderFaceYPos(block, 0D, 0D, 0D, block.getIcon(1, 0));
        tsl.draw();

        tsl.startDrawingQuads();
        tsl.setNormal(0F, 0F, -1F);
        blockRender.renderFaceZNeg(block, 0D, 0D, 0D, block.getIcon(2, 0));
        tsl.draw();

        tsl.startDrawingQuads();
        tsl.setNormal(0F, 0F, 1F);
        blockRender.renderFaceZPos(block, 0D, 0D, 0D, block.getIcon(3, 0));
        tsl.draw();

        tsl.startDrawingQuads();
        tsl.setNormal(-1F, 0F, 0F);
        blockRender.renderFaceXNeg(block, 0D, 0D, 0D, block.getIcon(4, 0));
        tsl.draw();

        tsl.startDrawingQuads();
        tsl.setNormal(1F, 0F, 0F);
        blockRender.renderFaceXPos(block, 0D, 0D, 0D, block.getIcon(5, 0));
        tsl.draw();

        GL11.glPopMatrix();
    }
}
