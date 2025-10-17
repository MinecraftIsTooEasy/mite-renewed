package com.github.jeffyjamzhd.renewed.mixins.gui;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.api.ISoundManager;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = GuiMainMenu.class, priority = 2000)
public abstract class GuiMainMenuMixin extends GuiScreen {
    @Shadow private int field_92021_u;
    @Shadow private int field_92021_u_MITE;
    @Shadow private int field_92019_w;
    @Shadow private int field_92019_w_MITE;
    @Shadow private int panoramaTimer;
    @Unique
    private static final ResourceLocation RENEWED_TEX = new ResourceLocation(MiTERenewed.RESOURCE_ID + "textures/gui/logo.png");
    private static final ResourceLocation RENEWED_TEX_CHINA = new ResourceLocation(MiTERenewed.RESOURCE_ID + "textures/gui/logo_zh.png");
    @Unique
    private static final ResourceLocation[] PANORAMA_TEX = new ResourceLocation[]{
            new ResourceLocation(MiTERenewed.RESOURCE_ID + "textures/gui/panorama/panorama_0.png"),
            new ResourceLocation(MiTERenewed.RESOURCE_ID + "textures/gui/panorama/panorama_1.png"),
            new ResourceLocation(MiTERenewed.RESOURCE_ID + "textures/gui/panorama/panorama_2.png"),
            new ResourceLocation(MiTERenewed.RESOURCE_ID + "textures/gui/panorama/panorama_3.png"),
            new ResourceLocation(MiTERenewed.RESOURCE_ID + "textures/gui/panorama/panorama_4.png"),
            new ResourceLocation(MiTERenewed.RESOURCE_ID + "textures/gui/panorama/panorama_5.png"),
    };

    @ModifyArg(method = "drawPanorama", at = @At(value = "INVOKE", target = "Lnet/minecraft/TextureManager;bindTexture(Lnet/minecraft/ResourceLocation;)V"))
    ResourceLocation modifyPanorama(ResourceLocation rl, @Local(name = "var10") int i) {
        return PANORAMA_TEX[i];
    }

    @ModifyArg(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiMainMenu;drawString(Lnet/minecraft/FontRenderer;Ljava/lang/String;III)V", ordinal = 0), index = 1)
    String modifyString(String par2) {
        return Minecraft.getVersionDescriptor(true);
    }

    @ModifyArg(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/TextureManager;bindTexture(Lnet/minecraft/ResourceLocation;)V", ordinal = 0))
    ResourceLocation modifyBindGL(ResourceLocation _a) {
        return isChinese() ? RENEWED_TEX_CHINA : RENEWED_TEX;
    }

    @WrapOperation(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiMainMenu;drawTexturedModalRect(IIIIII)V", ordinal = 5))
    void modifyTitleRect(
            GuiMainMenu instance,
            int a, int b, int c,
            int d, int e, int f,
            Operation<Void> original) {
        // Draw rectangle
        this.drawGradientRect(0, 20, this.width, 95 + (isChinese() ? 16 : 0), 0x70000000, 0xB0000000);

        // Draw logo
        int width = this.width / 2;
        original.call(instance, width - (146 / 2), 30, 0, 0, 155, 74);
    }

    @ModifyVariable(method = "initGui", at = @At(value = "INVOKE", target = "Lnet/minecraft/Minecraft;isDemo()Z"), index = 3)
    int modifyHeight(int value) {
        return value - 18;
    }

    @Inject(method = "initGui", at = @At("TAIL"))
    void moveWarning(CallbackInfo ci) {
        this.field_92021_u -= 56;
        this.field_92021_u_MITE -= 56;
        this.field_92019_w -= 56;
        this.field_92019_w_MITE -= 56;
    }

    @Inject(method = "initGui", at = @At("TAIL"))
    private void kickstartMusic(CallbackInfo ci) {
        mc.sndManager.mr$setTicksToPlay(20);
    }

    @Redirect(method = "initGui", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 3))
    <E> boolean doNotAddForum(List instance, E e) {
        return false;
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiMainMenu;drawGradientRect(IIIIII)V"))
    void removeGradient(GuiMainMenu instance, int a, int b, int c, int d, int e, int f) {}

    @Inject(method = "updateScreen", at = @At(value = "TAIL"))
    private void playRandomMusic(CallbackInfo ci) {
        ISoundManager snd = ((ISoundManager) mc.sndManager);
        if (snd.mr$isLoaded()) {
            if (!snd.mr$isMusicPlaying()) {
                mc.sndManager.playRandomMusicIfReady();
                snd.mr$setMusicPitch(.93F);
            } else {
                snd.mr$setTicksToPlay(200);
            }
        }
    }

    @Inject(method = "addSingleplayerMultiplayerButtons", at = @At("TAIL"))
    void moveButtons(int par1, int par2, CallbackInfo ci,
                     @Local(name = "button_singleplayer") GuiButton button1,
                     @Local(name = "button_multiplayer") GuiButton button2) {
        button1.yPosition += 36;
        button2.yPosition += 36;
        if (Minecraft.MITE_resource_pack == null) {
            button1.drawButton = false;
            button2.drawButton = false;
        }
    }

    @Redirect(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiMainMenu;drawTexturedModalRect(IIIIII)V", ordinal = 6))
    void removeSecondTitleRect(GuiMainMenu instance, int a, int b, int c, int d, int e, int f) {
    }

    @WrapOperation(method = "drawScreen", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V", ordinal = 0))
    void moveSplash(float x, float y, float z, Operation<Void> original) {
        original.call((this.width / 2 + 90F), 90F, 0F);
    }

    @Unique
    boolean isChinese() {
        return Minecraft.getMinecraft().getLanguageManager()
                .getCurrentLanguage()
                .getLanguageCode()
                .equals("zh_CN");
    }
}
