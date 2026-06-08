package com.github.jeffyjamzhd.renewed.render.tile;

import com.github.jeffyjamzhd.renewed.block.entity.TileEntityCrate;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

public class TileEntityCrateRenderer extends TileEntitySpecialRenderer {
    private final RenderItem customRenderItem = new RenderItem() {
        @Override
        public void renderItemAndEffectIntoGUI(FontRenderer fontRenderer, TextureManager textureManager, ItemStack stack, int x, int y) {
            if (stack == null) {
                return;
            }
            Tessellator var8 = Tessellator.instance;
            float var16 = 0.0625F;

            this.renderItemIntoGUI(fontRenderer, textureManager, stack, x, y);
            if (stack.hasEffect()) {
                GL11.glDepthFunc(GL11.GL_EQUAL);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(false);
                textureManager.bindTexture(RES_ITEM_GLINT);
                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glColor4f(.5F * .76F, 0.25F * .76F, 0.8F * .76F, 1.0F);
                renderGlint(x, y, 16, 16);
                GL11.glDepthMask(true);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDepthFunc(GL11.GL_LEQUAL);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            }
        }

        private void renderGlint(int x, int y, int w, int h)
        {
            for (int i = 0; i < 2; ++i)
            {
                GL14.glBlendFuncSeparate(772, 1, 0, 0);
                float uScale = 0.00390625F;
                float vScale = 0.00390625F;
                float u = (Minecraft.getSystemTime() % (3000 + i * 1873)) / (3000.0F + i * 1873) * 256.0F;
                float v = 0.0F;

                float hScale = (i < 1) ? 4.0F : -1.0F;

                Tessellator tessellator = Tessellator.instance;
                tessellator.startDrawingQuads();
                tessellator.addVertexWithUV(x + 0, y + h, 0, (u + (float)h * hScale) * uScale, (v + (float)h) * vScale);
                tessellator.addVertexWithUV(x + w, y + h, 0, (u + (float)w + (float)h * hScale) * uScale, (v + (float)h) * vScale);
                tessellator.addVertexWithUV(x + w, y + 0, 0, (u + (float)w) * uScale, (v + 0.0F) * vScale);
                tessellator.addVertexWithUV(x + 0, y + 0, 0, (u + 0.0F) * uScale, (v + 0.0F) * vScale);
                tessellator.draw();
            }
        }
    };

    public TileEntityCrateRenderer() {
        this.customRenderItem.setRenderManager(RenderManager.instance);
    }

    @Override
    public void renderTileEntityAt(TileEntity tile, double x, double y, double z, float partialTicks) {
        TileEntityCrate crate = (TileEntityCrate) tile;
        ItemStack displayStack = crate.getStackInSlot(0);
        Minecraft mc = Minecraft.getMinecraft();

        // Do not display without proper item
        if (displayStack == null) return;
        Icon icon = displayStack.getIconIndex();
        if (icon == null) return;

        // Begin render
        GL11.glPushMatrix();
        GL11.glTranslatef((float) x + 0.5F, (float) y + 0.5F, (float) z + 0.5F);

        // Determine rotation and scale
        int meta = tile.getBlockMetadata();
        EnumDirection facing = tile.getBlockType().getDirectionFacing(meta);
        float rotation = 0.0F;
        float offset = 0.475F;

        switch (facing) {
            case NORTH: rotation = 180.0F; GL11.glTranslatef( 0F,  0F, -offset); break;
            case EAST:  rotation =  90.0F; GL11.glTranslatef( offset, 0F,  0F);  break;
            case SOUTH: rotation =   0.0F; GL11.glTranslatef( 0F,  0F,  offset); break;
            case WEST:  rotation = -90.0F; GL11.glTranslatef(-offset, 0F,  0F);  break;
        }

        GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
        float scale = 0.03125F;
        GL11.glScalef(scale, -scale, scale);

        // Lightmap setup, sample the block face the icon is on
        int ambLight = tile.getWorldObj().getLightBrightnessForSkyBlocks(
                facing.getNeighborX(tile.xCoord),
                facing.getNeighborY(tile.yCoord),
                facing.getNeighborZ(tile.zCoord), 0);
        int lu = ambLight % 65536;
        int lv = ambLight / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lu, lv);

        GL11.glDisable(GL11.GL_LIGHTING);

        // Begin to draw item
        GL11.glPushMatrix();
        if (displayStack.isBlock()) {
            GL11.glScalef(1F, 1F, 0.001F);
        }

        this.customRenderItem.renderItemAndEffectIntoGUI(
                mc.fontRenderer, mc.getTextureManager(), displayStack, -8, -8);

        GL11.glPopMatrix();

        // Item count label
        if (crate.heldItemCount > 1) {
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glScalef(0.5F, 0.5F, 0.5F);
            GL11.glTranslatef(8F, 8F, 0.05F);
            String countStr = String.valueOf(crate.heldItemCount);

            // Shadow (drawn slightly behind)
            GL11.glTranslatef(0F, 0F, -0.01F);
            mc.fontRenderer.drawString(countStr,
                    10 - mc.fontRenderer.getStringWidth(countStr), 2,
                    (16777215 & 16579836) >> 2 | 16777215 & -16777216);

            // Text (drawn in front)
            GL11.glTranslatef(0F, 0F, 0.01F);
            mc.fontRenderer.drawString(countStr,
                    9 - mc.fontRenderer.getStringWidth(countStr), 1, 16777215);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }

        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }
}
