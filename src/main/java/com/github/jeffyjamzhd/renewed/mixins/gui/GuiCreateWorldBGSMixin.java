package com.github.jeffyjamzhd.renewed.mixins.gui;

import com.bawnorton.mixinsquared.TargetHandler;
import com.github.jeffyjamzhd.renewed.api.IWorldSettings;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.github.jeffyjamzhd.renewed.render.gui.GuiConfigureButton;
import com.github.jeffyjamzhd.renewed.render.gui.GuiCustomizeWorldDifficulty;
import com.llamalad7.mixinextras.sugar.Local;
import moddedmite.xylose.bettergamesetting.api.IGuiCreateWorld;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = GuiCreateWorld.class, priority = 1500)
@Environment(EnvType.CLIENT)
abstract public class GuiCreateWorldBGSMixin extends GuiScreen implements com.github.jeffyjamzhd.renewed.api.IGuiCreateWorld {
    @Shadow
    protected abstract void updateButtonText();

    @Unique
    private GuiButton buttonDifficulty;
    @Unique
    private GuiConfigureButton buttonDifficultyConfig;
    @Unique
    private int difficultyIndice;
    @Unique
    private Difficulty customDifficulty;

    @TargetHandler(
            mixin = "moddedmite.xylose.bettergamesetting.mixin.client.gui.GuiCreateWorldMixin",
            name = "recreateButtons"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At("HEAD"))
    private void addButtons(CallbackInfo ci) {
        this.buttonList.add(this.buttonDifficulty = new GuiButton(50, this.width / 2 - 104, this.height / 5 + 50, 188, 20, getNameOfDifficulty()));
        this.buttonList.add(this.buttonDifficultyConfig = new GuiConfigureButton(51, this.width / 2 + 84, this.height / 5 + 50));
    }

    @TargetHandler(
            mixin = "moddedmite.xylose.bettergamesetting.mixin.client.gui.GuiCreateWorldMixin",
            name = "updateButtonVisibilityNAbility"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At("HEAD"))
    private void updateDifficultyButtons(CallbackInfo ci) {
        this.buttonDifficulty.drawButton = false;
        this.buttonDifficultyConfig.drawButton = false;

        if (((IGuiCreateWorld) this).bgs$getCurrentTab() == 100) {
            this.buttonDifficulty.drawButton = true;
            this.buttonDifficultyConfig.drawButton = true;
        }
    }

    @Inject(method = "updateButtonText", at = @At("TAIL"))
    private void updateDifficultyButtonText(CallbackInfo ci) {
        if (this.buttonDifficulty != null)
            this.buttonDifficulty.displayString = getNameOfDifficulty();
    }

    @Inject(method = "actionPerformed", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldSettings;func_82750_a(Ljava/lang/String;)Lnet/minecraft/WorldSettings;"))
    private void supplyDifficultyObject(GuiButton btn, CallbackInfo ci, @Local WorldSettings settings) {
        ((IWorldSettings) settings).mr$setDifficulty(getSelectedDifficulty());
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
                this.mc.displayGuiScreen(new GuiCustomizeWorldDifficulty(this, this.difficultyIndice));
        }
    }

//    @TargetHandler(
//            mixin = "moddedmite.xylose.bettergamesetting.mixin.client.gui.GuiCreateWorldMixin",
//            name = "updateButtonVisibilityNAbility"
//    )
//    @Inject(method = "@MixinSquared:Handler", at = @At("TAIL"))
//    private void updateButtonVisibility(CallbackInfo ci) {
//        switch ()
//    }

    @TargetHandler(
            mixin = "moddedmite.xylose.bettergamesetting.mixin.client.gui.GuiCreateWorldMixin",
            name = "recreateButtons"
    )
    @ModifyArg(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiButton;setPosition(II)V", ordinal = 6), index = 1)
    private int moveProfessionsButtonDown(int param) {
        return this.height / 5 + 100;
    }


    @TargetHandler(
            mixin = "moddedmite.xylose.bettergamesetting.mixin.client.gui.GuiCreateWorldMixin",
            name = "drawHoverText"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
    private void drawDifficultyTooltip(int mouseX, int mouseY, CallbackInfo ci, @Local GuiButton button) {
        if (button.id == 50) {
            ScreenUtil.getInstance().drawTooltip(getTooltipOfDifficulty(), mouseX, mouseY);
        }
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
