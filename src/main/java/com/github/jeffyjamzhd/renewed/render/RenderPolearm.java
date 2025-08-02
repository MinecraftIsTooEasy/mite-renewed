package com.github.jeffyjamzhd.renewed.render;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.entity.EntityPolearm;
import com.github.jeffyjamzhd.renewed.item.ItemPolearm;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;

@Environment(EnvType.CLIENT)
public class RenderPolearm extends Render {
    private static ResourceLocation[] tex;

    public RenderPolearm() {
        if (tex == null)
            addTextures();
    }

    public void addTextures() {
        tex = new ResourceLocation[2];
        tex[0] = new ResourceLocation(MiTERenewed.RESOURCE_ID + "textures/entity/polearm/flint.png");
        tex[1] = new ResourceLocation(MiTERenewed.RESOURCE_ID + "textures/entity/polearm/bone.png");
    }

    public void renderPolearm(EntityPolearm entity, double x, double y, double z, float par8, float par9) {
        this.bindEntityTexture(entity);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * par9 - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9, 0.0F, 0.0F, 1.0F);
        Tessellator var10 = Tessellator.instance;
        byte var11 = 0;
        float var12 = 0.0F;
        float var13 = 0.5F;
        float var14 = (float)0 / 32.0F;
        float var15 = (float)5 / 32.0F;
        float var16 = 0.0F;
        float var17 = 0.15625F;
        float var18 = (float)5 / 32.0F;
        float var19 = (float)10 / 32.0F;
        float var20 = 0.10625F;
        GL11.glEnable(32826);
        float var21 = (float)entity.polearmShake - par9;
        if (var21 > 0.0F) {
            float var22 = -MathHelper.sin(var21 * 3.0F) * var21;
            GL11.glRotatef(var22, 0.0F, 0.0F, 1.0F);
        }

        GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(var20 * 1.2f, var20, var20);
        GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
        GL11.glNormal3f(var20, 0.0F, 0.0F);
        var10.startDrawingQuads();
        var10.addVertexWithUV(-7.0F, -2.0F, -2.0F, var16, var18);
        var10.addVertexWithUV(-7.0F, -2.0F, 2.0F, var17, var18);
        var10.addVertexWithUV(-7.0F, 2.0F, 2.0F, var17, var19);
        var10.addVertexWithUV(-7.0F, 2.0F, -2.0F, var16, var19);
        var10.draw();
        GL11.glNormal3f(-var20, 0.0F, 0.0F);
        var10.startDrawingQuads();
        var10.addVertexWithUV(-7.0F, 2.0F, -2.0F, var16, var18);
        var10.addVertexWithUV(-7.0F, 2.0F, 2.0F, var17, var18);
        var10.addVertexWithUV(-7.0F, -2.0F, 2.0F, var17, var19);
        var10.addVertexWithUV(-7.0F, -2.0F, -2.0F, var16, var19);
        var10.draw();

        for(int var23 = 0; var23 < 4; ++var23) {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, var20);
            var10.startDrawingQuads();
            var10.addVertexWithUV(-8.0F, -2.0F, 0.0F, var12, var14);
            var10.addVertexWithUV(8.0F, -2.0F, 0.0F, var13, var14);
            var10.addVertexWithUV(8.0F, 2.0F, 0.0F, var13, var15);
            var10.addVertexWithUV(-8.0F, 2.0F, 0.0F, var12, var15);
            var10.draw();
        }

        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float g, float h) {
        renderPolearm((EntityPolearm) entity, x, y, z, g, h);
    }

    // Placeholder
    protected ResourceLocation getPolearmTexture(EntityPolearm polearm) {
        return tex[0];
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return getPolearmTexture((EntityPolearm) entity);
    }
}
