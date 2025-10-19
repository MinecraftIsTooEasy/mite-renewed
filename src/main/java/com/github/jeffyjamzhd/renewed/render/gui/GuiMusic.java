package com.github.jeffyjamzhd.renewed.render.gui;

import com.github.jeffyjamzhd.renewed.api.music.MusicMetadata;
import net.minecraft.*;
import net.minecraft.client.main.Main;
import org.lwjgl.opengl.GL11;

public class GuiMusic extends Gui {
    private static final ResourceLocation background =
            new ResourceLocation("textures/gui/achievement/achievement_background.png");
    private final Minecraft mc;
    private final RenderItem itemRenderer;

    private float width;
    private float height;
    private long displayTime = 0L;
    private String artistName = "";
    private String trackName = "";

    public GuiMusic(Minecraft mc) {
        this.mc = mc;
        this.itemRenderer = new RenderItem();
    }

    public void queueMusic(MusicMetadata metadata) {
        this.displayTime = Minecraft.getSystemTime();
        this.artistName = metadata.artist();
        this.trackName = metadata.title();
    }

    private void updateWindowScale() {
        GL11.glViewport(0, 0, this.mc.displayWidth, this.mc.displayHeight);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        ScaledResolution var1 = new ScaledResolution(this.mc.gameSettings, this.mc.displayWidth, this.mc.displayHeight);
        this.width = var1.getScaledWidth();
        this.height = var1.getScaledHeight();
        GL11.glClear(256);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0F, this.width, this.height, 0F, 1000F, 3000F);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
        GL11.glTranslatef(0.0F, 0.0F, -2000.0F);
    }

    public void updateDisplay() {
        if (Minecraft.theMinecraft.gameSettings.gui_mode != 0
            || Main.is_MITE_DS
            || this.mc.gameSettings.musicVolume <= 0F)
        {
            this.displayTime = 0L;
            this.artistName = "";
            this.trackName = "";
            return;
        }

        if (this.displayTime != 0L) {
            double anim = (Minecraft.getSystemTime() - this.displayTime) / 6000F;
            if (!(anim < 0F) && !(anim > 1F)) {
                GL11.glPushMatrix();
                this.updateWindowScale();
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(false);
                double var3 = anim * (double) 2F;
                if (var3 > 1F) {
                    var3 = 2F - var3;
                }

                var3 *= 4F;
                var3 = 1F - var3;
                if (var3 < 0F) {
                    var3 = 0F;
                }

                var3 *= var3;
                var3 *= var3;
                int var5 = 0;
                int var6 = -(int) (var3 * 36F);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                this.mc.getTextureManager().bindTexture(background);
                GL11.glDisable(GL11.GL_LIGHTING);

                // Draw
                this.drawTexturedModalRect(var5, var6, 96, 202, 160, 32);
                this.mc.fontRenderer.drawString(this.artistName, var5 + 30, var6 + 7, 0xFF44FF00, true);
                this.mc.fontRenderer.drawString(this.trackName, var5 + 30, var6 + 18, -1, true);

                RenderHelper.enableGUIStandardItemLighting();
                GL11.glEnable(32826);
                GL11.glEnable(GL11.GL_COLOR_MATERIAL);
                GL11.glEnable(GL11.GL_LIGHTING);
                this.itemRenderer.renderItemAndEffectIntoGUI(this.mc.fontRenderer, this.mc.getTextureManager(), Item.recordCat.getItemStackForStatsIcon(), var5 + 8, var6 + 8);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(true);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glPopMatrix();
            } else {
                this.displayTime = 0L;
            }
        }
    }
}
