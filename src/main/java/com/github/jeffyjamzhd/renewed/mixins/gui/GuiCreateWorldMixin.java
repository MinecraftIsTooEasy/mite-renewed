package com.github.jeffyjamzhd.renewed.mixins.gui;

import net.minecraft.GuiButton;
import net.minecraft.GuiCreateWorld;
import net.minecraft.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Unused
 */
@Mixin(value = GuiCreateWorld.class, priority = 500)
abstract public class GuiCreateWorldMixin extends GuiScreen {
    @Shadow
    private boolean moreOptions;
    @Unique
    private static final int BUTTON_GM_X = 154;
    @Unique
    private static final int BUTTON_GM_Y = 104;
    @Unique
    private GuiButton buttonDifficulty;
    @Unique
    private GuiButton buttonDifficultyConfig;

    // Add new button and functionality

    @Inject(method = "initGui", at = @At("TAIL"))
    private void addButtons(CallbackInfo ci) {
        // Todo: replace hardcoded strings
        this.buttonList.add(this.buttonDifficulty = new GuiButton(50, this.width / 2 + 2, BUTTON_GM_Y, 132, 20, "Difficulty: Extreme"));
        this.buttonList.add(this.buttonDifficultyConfig = new GuiButton(51, this.width / 2 + 134, BUTTON_GM_Y, 20, 20, "C"));
    }

    // Add text draw for gamemode

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiCreateWorld;drawString(Lnet/minecraft/FontRenderer;Ljava/lang/String;III)V", ordinal = 7))
    private void drawDifficultyText(int par1, int par2, float par3, CallbackInfo ci) {
        this.drawString(this.fontRenderer, "The intended difficulty of MiTE.", this.width / 2 + 2, BUTTON_GM_Y + 24, 10526880);
        this.drawString(this.fontRenderer, "A devious blend of survival and combat", this.width / 2 + 2, BUTTON_GM_Y + 36, 10526880);
        this.drawString(this.fontRenderer, "challenges. Prepare to die!", this.width / 2 + 2, BUTTON_GM_Y + 48, 10526880);
    }

    // Modify gamemode button

    @ModifyArg(method = "initGui", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiButton;<init>(IIIIILjava/lang/String;)V", ordinal = 2), index = 1)
    private int setGameModeXPosition(int par1) {
        return this.width / 2 - BUTTON_GM_X;
    }

    @ModifyArg(method = "initGui", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiButton;<init>(IIIIILjava/lang/String;)V", ordinal = 2), index = 2)
    private int setGameModeYPosition(int par1) {
        return BUTTON_GM_Y;
    }

    // Modify gamemode description

    @ModifyArg(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiCreateWorld;drawString(Lnet/minecraft/FontRenderer;Ljava/lang/String;III)V", ordinal = 6), index = 2)
    private int modifyLine1X(int par3) {
        return this.width / 2 - BUTTON_GM_X;
    }

    @ModifyArg(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiCreateWorld;drawString(Lnet/minecraft/FontRenderer;Ljava/lang/String;III)V", ordinal = 6), index = 3)
    private int modifyLine1Y(int par3) {
        return BUTTON_GM_Y + 24;
    }

    @ModifyArg(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiCreateWorld;drawString(Lnet/minecraft/FontRenderer;Ljava/lang/String;III)V", ordinal = 7), index = 2)
    private int modifyLine2X(int par3) {
        return this.width / 2 - BUTTON_GM_X;
    }

    @ModifyArg(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiCreateWorld;drawString(Lnet/minecraft/FontRenderer;Ljava/lang/String;III)V", ordinal = 7), index = 3)
    private int modifyLine2Y(int par3) {
        return BUTTON_GM_Y + 36;
    }
}
