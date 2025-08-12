package com.github.jeffyjamzhd.renewed.render;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.api.IMaterial;
import com.github.jeffyjamzhd.renewed.entity.EntityPolearm;
import com.github.jeffyjamzhd.renewed.item.ItemPolearm;
import com.github.jeffyjamzhd.renewed.registry.RenewedItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

@Environment(EnvType.CLIENT)
public class RenderPolearm extends Render {
    private static final HashMap<String, ResourceLocation> tex = new HashMap<>();
    private final static String[] materials = new String[]{
        "flint", "bone", "obsidian", "copper", "silver", "gold", "iron", "ancient_metal", "mithril", "adamantium"
    };

    public RenderPolearm() {
        addTextures();
    }

    public void addTextures() {
        for (String name : materials) {
            tex.put(name, new ResourceLocation(MiTERenewed.RESOURCE_ID + "textures/entity/polearm/%s.png".formatted(name)));
        }
    }

    public void renderPolearm(EntityPolearm entity, double x, double y, double z, float par8, float par9) {
        this.bindEntityTexture(entity);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glRotatef(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * par9 - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * par9, 0.0F, 0.0F, 1.0F);
        Tessellator tss = Tessellator.instance;
        byte var11 = 0;
        
        float var12 = 0F; // Shaft U0
        float var13 = 16F / 32F; // Shaft U1
        float var14 = 0F / 32F; // Shaft V0
        float var15 = 5F / 32F; // Shaft 1V
        
        float var16 = 0.0F; // Back piece U0
        float var17 = 0.15625F; // Back piece U1
        float var18 = 5F / 32.0F; // Back piece V0
        float var19 = 10F / 32.0F; // Back piece V1

        float stringU0 = 7F / 32.0F;
        float stringU1 = 12F / 32.0F;
        float stringV0 = 5F / 32.0F;
        float stringV1 = 13F / 32.0F;

        float scale = 0.10625F; // Render scale
        float var21 = entity.polearmShake - par9; // Rotation mod

        GL11.glEnable(32826);

        if (var21 > 0.0F) {
            float var22 = -MathHelper.sin(var21 * 3.0F) * var21;
            GL11.glRotatef(var22, 0.0F, 0.0F, 1.0F);
        }

        // Shaft
        GL11.glScalef(scale * 1.25f, scale, scale);
        GL11.glTranslatef(-5.0F, 0F, 0F);
        for(int var23 = 0; var23 < 4; ++var23) {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, scale);
            tss.startDrawingQuads();
            tss.addVertexWithUV(-8.0F, -2.0F, 0.0F, var12, var14);
            tss.addVertexWithUV(8.0F, -2.0F, 0.0F, var13, var14);
            tss.addVertexWithUV(8.0F, 2.0F, 0.0F, var13, var15);
            tss.addVertexWithUV(-8.0F, 2.0F, 0.0F, var12, var15);
            tss.draw();
        }

        // Back piece
        GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(-1.0F, 0.0F, 0.0F);
        GL11.glNormal3f(scale, 0.0F, 0.0F);
        GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
        tss.startDrawingQuads();
        tss.addVertexWithUV(-7F, -2.0F, -2.0F, var16, var18);
        tss.addVertexWithUV(-7F, -2.0F, 2.0F, var17, var18);
        tss.addVertexWithUV(-7F, 2.0F, 2.0F, var17, var19);
        tss.addVertexWithUV(-7F, 2.0F, -2.0F, var16, var19);
        tss.draw();
        GL11.glNormal3f(-scale, 0.0F, 0.0F);
        tss.startDrawingQuads();
        tss.addVertexWithUV(-7F, 2.0F, -2.0F, var16, var18);
        tss.addVertexWithUV(-7F, 2.0F, 2.0F, var17, var18);
        tss.addVertexWithUV(-7F, -2.0F, 2.0F, var17, var19);
        tss.addVertexWithUV(-7F, -2.0F, -2.0F, var16, var19);
        tss.draw();
        GL11.glRotatef(-45.0F, 1.0F, 0.0F, 0.0F);

        // String
        GL11.glTranslatef(4.0F, 0F, 0F);
        GL11.glScalef(1.15F, 1.25F, 1.25F);
        GL11.glRotatef(225.0F, 1.0F, 0.0F, 0.0F);
        GL11.glRotatef(45.0F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(0F, 0F, -0.15F);
        for (int i = 0; i < 4; ++i) {
            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
            GL11.glNormal3f(scale, 0.0F, 0.0F);
            tss.startDrawingQuads();
            tss.addVertexWithUV(0.0F, 2.0F, -4.0F, stringU0, stringV1);
            tss.addVertexWithUV(0.0F, 2.0F, 2.0F, stringU0, stringV0);
            tss.addVertexWithUV(0.0F, -2.0F, 2.0F, stringU1, stringV0);
            tss.addVertexWithUV(0.0F, -2.0F, -4.0F, stringU1, stringV1);
            tss.draw();
        }
        GL11.glRotatef(-45.0F, 0.0F, 0.0F, 1.0F);
        GL11.glTranslatef(-3.0F, 0.0F, 0.15F);

        GL11.glDisable(32826);
        GL11.glPopMatrix();
    }

    @Override
    public void doRender(Entity entity, double x, double y, double z, float g, float h) {
        renderPolearm((EntityPolearm) entity, x, y, z, g, h);
    }

    // Placeholder
    protected ResourceLocation getPolearmTexture(EntityPolearm polearm) {
        return getTexFromItem(polearm.getItem());
    }

    protected ResourceLocation getTexFromItem(ItemPolearm item) {
        return tex.getOrDefault(((IMaterial)item.getToolMaterial()).mr$getName(), tex.get("flint"));
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return getPolearmTexture((EntityPolearm) entity);
    }
}
