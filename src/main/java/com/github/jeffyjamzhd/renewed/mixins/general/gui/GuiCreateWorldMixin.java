package com.github.jeffyjamzhd.renewed.mixins.general.gui;

import com.bawnorton.mixinsquared.TargetHandler;
import com.github.jeffyjamzhd.renewed.api.IGuiCreateWorld;
import com.github.jeffyjamzhd.renewed.api.IWorldInfo;
import com.github.jeffyjamzhd.renewed.api.IWorldSettings;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.github.jeffyjamzhd.renewed.render.gui.GuiConfigureButton;
import com.github.jeffyjamzhd.renewed.render.gui.GuiCustomizeWorldDifficulty;
import com.github.jeffyjamzhd.renewed.render.gui.GuiLockDifficultyButton;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.loader.impl.util.StringUtil;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * Unused
 */
@Mixin(value = GuiCreateWorld.class, priority = 500)
abstract public class GuiCreateWorldMixin extends GuiScreen implements IGuiCreateWorld {
    @Shadow
    private boolean moreOptions;
    @Shadow
    private String gameModeDescriptionLine1;
    @Shadow
    private String gameModeDescriptionLine2;

    @Shadow
    protected abstract void updateButtonText();

    @Unique
    private static final int BUTTON_GM_X = 154;
    @Unique
    private static final int BUTTON_GM_Y = 104;
    @Unique
    private GuiButton buttonDifficulty;
    @Unique
    private GuiConfigureButton buttonDifficultyConfig;
    @Unique
    private GuiLockDifficultyButton buttonDifficultyLock;
    @Unique
    private int difficultyIndice = 0;
    @Unique
    private boolean difficultyLocked = false;
    @Unique
    private Difficulty customDifficulty;

    // Add new button and functionality

    @Inject(method = "initGui", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiCreateWorld;func_82288_a(Z)V"))
    private void addButtons(CallbackInfo ci) {
        this.buttonList.add(this.buttonDifficulty = new GuiButton(50, this.width / 2 + 2, BUTTON_GM_Y, 112, 20, "Difficulty: Extreme"));
        this.buttonList.add(this.buttonDifficultyConfig = new GuiConfigureButton(51, this.width / 2 + 114, BUTTON_GM_Y));
        this.buttonList.add(this.buttonDifficultyLock = new GuiLockDifficultyButton(52, this.width / 2 + 134, BUTTON_GM_Y));
    }

    @Inject(method = "actionPerformed", at = @At("TAIL"))
    private void onActionPerformed(GuiButton btn, CallbackInfo ci) {
        switch (btn.id) {
            case 50: // Difficulty button
                this.difficultyIndice = (this.difficultyIndice + 1) % RenewedDifficulties.LIST.size();
                this.updateButtonText();
                this.customDifficulty = null;

                break;

            case 51: // Difficulty configuration button
                GuiCustomizeWorldDifficulty newScreen;

                if (this.customDifficulty != null) {
                    newScreen = new GuiCustomizeWorldDifficulty(this, this.customDifficulty);
                } else {
                    newScreen = new GuiCustomizeWorldDifficulty(this, this.difficultyIndice);
                }

                this.mc.displayGuiScreen(newScreen);
                break;
            case 52: // Difficulty lock button
                GuiLockDifficultyButton cast = (GuiLockDifficultyButton) btn;
                cast.toggled = !cast.toggled;
                this.difficultyLocked = cast.toggled;
        }
    }

    @Inject(method = "updateButtonText", at = @At("TAIL"))
    private void updateDifficultyButtonText(CallbackInfo ci) {
        if (this.buttonDifficulty != null)
            this.buttonDifficulty.displayString = getNameOfDifficulty();
    }

    @Inject(method = "func_82288_a", at = @At("TAIL"))
    private void updateDifficultyButtons(CallbackInfo ci) {
        this.buttonDifficulty.drawButton = !this.moreOptions;
        this.buttonDifficultyConfig.drawButton = !this.moreOptions;
        this.buttonDifficultyLock.drawButton = !this.moreOptions;
    }

    @Inject(method = "actionPerformed", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldSettings;func_82750_a(Ljava/lang/String;)Lnet/minecraft/WorldSettings;"))
    private void supplyDifficultyObject(GuiButton btn, CallbackInfo ci, @Local WorldSettings settings) {
        IWorldSettings cast = (IWorldSettings) settings;

        cast.mr$setDifficulty(getSelectedDifficulty());
        cast.mr$setDifficultyLocked(this.buttonDifficultyLock.toggled);
    }

    // Add text draw for gamemode

    @Inject(method = "drawScreen", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiCreateWorld;drawString(Lnet/minecraft/FontRenderer;Ljava/lang/String;III)V", ordinal = 6), cancellable = true)
    private void drawDifficultyText(int par1, int par2, float par3, CallbackInfo ci) {
        drawGamemodeText();
        drawDifficultyText();

        super.drawScreen(par1, par2, par3);
        ci.cancel();
    }

    @Unique
    private void drawGamemodeText() {
        String combined = this.gameModeDescriptionLine1.trim() + " " + this.gameModeDescriptionLine2.trim();
        String wrapped = StringUtil.wrapLines(combined, 30);
        Stream<String> lines = wrapped.lines();
        AtomicInteger count = new AtomicInteger(0);

        lines.forEachOrdered(s -> this.drawString(this.fontRenderer, s, this.width / 2 - 154, BUTTON_GM_Y + 24 + (12 * count.getAndAdd(1)), 10526880));
    }

    @Unique
    private void drawDifficultyText() {
        String wrapped = StringUtil.wrapLines(getTooltipOfDifficulty(), 30);
        Stream<String> lines = wrapped.lines();
        AtomicInteger count = new AtomicInteger(0);

        lines.forEachOrdered(s -> this.drawString(this.fontRenderer, s, this.width / 2 + 2, BUTTON_GM_Y + 24 + (12 * count.getAndAdd(1)), 10526880));
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

    @Inject(method = "func_82286_a", at = @At("TAIL"))
    private void assignDifficulty(WorldInfo info, CallbackInfo ci) {
        this.customDifficulty = ((IWorldInfo) info).mr$getDifficulty();
    }

    // Unique methods

    @Override
    public void mr$assignCustomDifficulty(Difficulty difficulty) {
        this.customDifficulty = difficulty;
        this.updateButtonText();
    }

    @Unique
    private Difficulty getSelectedDifficulty() {
        return this.customDifficulty != null ? this.customDifficulty : RenewedDifficulties.LIST.get(difficultyIndice);
    }

    @Unique
    private String getNameOfDifficulty() {
        return "%s %s".formatted(I18n.getString("difficulty.prefix"), getSelectedDifficulty().getLocalizedName());
    }

    @Unique
    private String getTooltipOfDifficulty() {
        return getSelectedDifficulty().getLocalizedDescription();
    }
}
