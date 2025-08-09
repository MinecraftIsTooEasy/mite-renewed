package com.github.jeffyjamzhd.renewed.render;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.item.ItemHandpan;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;

public class ItemRenderHandpan {
    private static ResourceLocation TEX = new ResourceLocation(MiTERenewed.RESOURCE_ID + "textures/items/handpan/render.png");
    private static RenderBlocks blockRender = new RenderBlocks();
    private static float prevAnimX = 0F, prevAnimZ = 0F;

    static public void renderHandpan(float par1, ItemStack item, EntityClientPlayerMP player, float equipTime, float var4) {
        // Get information
        int subtype = item.getItemSubtype();
        int duration = player.getItemInUseDuration();
        int maxUse = ItemHandpan.getIntendedSpeed(item);
        float progress = ItemHandpan.getFractionUsing(item, duration);

        float var21 = 0.8F;
        float var20 = player.getSwingProgress(par1);
        float var22 = MathHelper.sin(var20 * (float)Math.PI);
        float var13 = MathHelper.sin(MathHelper.sqrt_float(var20) * (float)Math.PI);
        GL11.glTranslatef(0F, MathHelper.sin(MathHelper.sqrt_float(var20) * (float)Math.PI * 2.0F) * 0.2F, -var22 * 0.6F);
        var20 = .4F - var4 / 120.0F + 0.1F;
        float var25 = .4F - var4 / 55.0F;
        if (var20 < 0.0F) {
            var20 = 0.0F;
        }

        if (var20 > 1.0F) {
            var20 = 1.0F;
        }

        // Calculate animation
        float value = duration > 0 ? progress * maxUse : 0;
        float animX = (float) ((Math.cos(value / 10F)) * .12F);
        float animZ = (float) ((Math.sin(value / 6F)) * .3F);
        animZ = prevAnimZ + (animZ - prevAnimZ) * par1 * 0.1f;
        animX = prevAnimX + (animX - prevAnimX) * par1 * 0.1f;

        // Prepare for render
        var20 = -MathHelper.cos(var20 * (float)Math.PI) * 0.2F + 0.5F;
        GL11.glTranslatef(0.0F, 0.0F * var21 - (1.0F - equipTime) * 1.2F - var20 * 0.8F + 0.04F, -0.9F * var21);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(var20 * -85.0F, 0.0F, 0.0F, 1.0F);
        GL11.glEnable(32826);
        Minecraft.getMinecraft().getTextureManager().bindTexture(player.getLocationSkin());

        // Translate and animate
        GL11.glTranslatef(.4F, .3F, 0F);
        GL11.glTranslatef(animX, 0, animZ);

        prevAnimX = animX;
        prevAnimZ = animZ;

        for(int i = 0; i < 2; ++i) {
            int var24 = i * 2 - 1;
            GL11.glPushMatrix();
            GL11.glTranslatef(-0.0F, -0.7F, 1.1F * (float)var24);
            GL11.glRotatef((float)(-25 * var24), 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(-90.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(30.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef((float)(-86 * var24), 0.0F, 1.0F, 0.0F);
            Render var27 = RenderManager.instance.getEntityRenderObject(Minecraft.getMinecraft().thePlayer);
            RenderPlayer var26 = (RenderPlayer)var27;
            float var16 = 1.0F;
            GL11.glScalef(var16, var16, var16);
            var26.renderFirstPersonArm(Minecraft.getMinecraft().thePlayer);
            GL11.glPopMatrix();
        }

        GL11.glRotatef(var25 * -25.0F + 15F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(var25 * .25F, 0F, 0F);
        var22 = player.getSwingProgress(par1);
        var13 = MathHelper.sin(var22 * var22 * (float)Math.PI);
        float var14 = MathHelper.sin(MathHelper.sqrt_float(var22) * (float)Math.PI);
        //GL11.glRotatef(-var13 * 20.0F, 0.0F, 1.0F, 0.0F);
        //GL11.glRotatef(-var14 * 20.0F, 0.0F, 0.0F, 1.0F);
        //GL11.glRotatef(-var14 * 80.0F, 1.0F, 0.0F, 0.0F);
        float var15 = 0.38F;
        GL11.glScalef(var15, var15, var15);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(50.0F, 1.0F, 0.0F, 0.0F);
        //GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float var16 = 0.019625F;
        GL11.glTranslatef(-0F, .8F, 1.75F);
        GL11.glScalef(var16, var16, var16);
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
        //GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
        //GL11.glRotatef(0.0F, 0.0F, 0.0F, 1.0F);
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
        GL11.glScaled(100, 100, 100);
        GL11.glRotatef(-270, 1, 0, 0);
        GL11.glTranslatef(0,  (0.35F - useTime) / 2, 0);
        GL11.glScaled(1, useTime, 1);
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        blockRender.renderBlockAsItem(block, 0, 1F);
    }
}
