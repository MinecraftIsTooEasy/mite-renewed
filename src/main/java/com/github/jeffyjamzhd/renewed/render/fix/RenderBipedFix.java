package com.github.jeffyjamzhd.renewed.render.fix;

import org.lwjgl.opengl.GL11;

public class RenderBipedFix {
    /**
     * This is a hacky fix for Zombie item rendering
     * having inverted normals
     */
    public static void fixZombieItem() {
        GL11.glScalef(1F, -1F, 1F);
        GL11.glRotatef(10F, 1, 0, 0);
        GL11.glTranslatef(0F, -1F, 0F);
    }
}
