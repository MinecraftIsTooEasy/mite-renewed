package com.github.jeffyjamzhd.renewed.render;

import net.minecraft.*;
import org.lwjgl.opengl.GL11;

import java.util.WeakHashMap;

public class RenderItemBeam {
    // Tracks when an item stopped moving
    private static final WeakHashMap<EntityItem, FadeTracker> trackers = new WeakHashMap<>();

    public static void renderItemLootBeam(EntityItem item, float cameraYaw, float bobOffset, float delta) {
        // Get fade mult
        float fadeMultiplier = getFadeForMotion(item);
        if (fadeMultiplier <= 0.001F) {
            return;
        }

        // Make sure item exists before pulling rarity
        if (item.getEntityItem() == null) {
            return;
        }

        int rarityColor = item.getEntityItem().getRarity().rarityColor;
        EnumChatFormatting color = EnumChatFormatting.values()[rarityColor];

        float beamR = color.getRedAsFloat();
        float beamG = color.getGreenAsFloat();
        float beamB = color.getBlueAsFloat();

        GL11.glPushMatrix();

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        GL11.glDepthMask(false);
        GL11.glShadeModel(GL11.GL_SMOOTH);

        float time = item.age + delta;
        float pulse = (MathHelper.sin(time * 0.15F) + 1.0F) * 0.5F;
        float beamWidth = (0.07F + (pulse * 0.04F)) * fadeMultiplier;
        float bottomAlpha = (0.40F + (pulse * 0.45F)) * fadeMultiplier;
        float shadowRadius = (0.35F + (pulse * 0.15F)) * fadeMultiplier;

        Tessellator tess = Tessellator.instance;
        GL11.glTranslatef(0.0F, -bobOffset - 0.1F, 0.0F);

        // Beam "shadow"
        tess.startDrawing(4);
        int segments = 8;
        for (int i = 0; i < segments; i++) {
            float angle1 = (float) (i * Math.PI * 2.0D / segments);
            float angle2 = (float) ((i + 1) * Math.PI * 2.0D / segments);

            float x1 = MathHelper.sin(angle1) * shadowRadius;
            float z1 = MathHelper.cos(angle1) * shadowRadius;
            float x2 = MathHelper.sin(angle2) * shadowRadius;
            float z2 = MathHelper.cos(angle2) * shadowRadius;

            // Center
            tess.setColorRGBA_F(beamR, beamG, beamB, bottomAlpha + 0.15F);
            tess.addVertex(0.0D, 0.0D, 0.0D);

            // Edges
            tess.setColorRGBA_F(beamR, beamG, beamB, 0.0F);
            tess.addVertex(x1, 0.0D, z1);
            tess.addVertex(x2, 0.0D, z2);
        }
        tess.draw();

        // Beam plane
        GL11.glRotatef(-cameraYaw, 0.0F, 1.0F, 0.0F);
        float beamHeight = 1.25F;

        tess.startDrawingQuads();
        tess.setColorRGBA_F(beamR, beamG, beamB, 0.0F); // Top
        tess.addVertex(-beamWidth, beamHeight, 0.0D);
        tess.addVertex(beamWidth, beamHeight, 0.0D);

        tess.setColorRGBA_F(beamR, beamG, beamB, Math.max(bottomAlpha - 0.15F, 0F)); // Bottom
        tess.addVertex(beamWidth, 0.0D, 0.0D);
        tess.addVertex(-beamWidth, 0.0D, 0.0D);
        tess.draw();

        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDepthMask(true);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
    }

    private static float getFadeForMotion(EntityItem item) {
        if (item.isDead) return 0F;

        double motionSq = item.motionX * item.motionX + item.motionY * item.motionY + item.motionZ * item.motionZ;
        boolean isMoving = !item.onGround || motionSq > 0.001D;

        long currentTime = System.currentTimeMillis();
        FadeTracker tracker = trackers.get(item);
        if (tracker == null) {
            tracker = new FadeTracker();
            tracker.lastUpdateTime = currentTime;
            trackers.put(item, tracker);
        }

        float dt = (currentTime - tracker.lastUpdateTime) / 1000.0F;
        tracker.lastUpdateTime = currentTime;
        if (dt > 0.5F) dt = 0.016F;

        float fadeInDuration = 0.8F;
        float fadeOutDuration = 0.2F;

        if (isMoving) {
            tracker.currentFade -= (dt / fadeOutDuration);
        } else {
            tracker.currentFade += (dt / fadeInDuration);
        }

        tracker.currentFade = Math.max(0.0F, Math.min(1.0F, tracker.currentFade));
        return tracker.currentFade;
    }

    private static class FadeTracker {
        float currentFade = 0F;
        long lastUpdateTime = System.currentTimeMillis();
    }
}
