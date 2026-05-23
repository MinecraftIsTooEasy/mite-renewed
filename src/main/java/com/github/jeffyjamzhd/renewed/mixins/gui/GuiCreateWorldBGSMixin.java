package com.github.jeffyjamzhd.renewed.mixins.gui;

import com.bawnorton.mixinsquared.TargetHandler;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.sugar.Local;
import moddedmite.xylose.bettergamesetting.mixin.client.gui.GuiCreateWorldMixin;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.GuiButton;
import net.minecraft.GuiCreateWorld;
import net.minecraft.GuiScreen;
import net.minecraft.I18n;
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
abstract public class GuiCreateWorldBGSMixin extends GuiScreen {
    @Shadow
    protected abstract void updateButtonText();

    @Unique
    private GuiButton buttonDifficulty;
    @Unique
    private GuiButton buttonDifficultyConfig;
    @Unique
    private int difficultyIndice;

    @TargetHandler(
            mixin = "moddedmite.xylose.bettergamesetting.mixin.client.gui.GuiCreateWorldMixin",
            name = "recreateButtons"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At("HEAD"))
    private void addButtons(CallbackInfo ci) {
        this.buttonList.add(this.buttonDifficulty = new GuiButton(50, this.width / 2 - 104, this.height / 5 + 50, 188, 20, getNameOfDifficulty()));
        this.buttonList.add(this.buttonDifficultyConfig = new GuiButton(51, this.width / 2 + 84, this.height / 5 + 50, 20, 20, "C"));
    }

    @Inject(method = "updateButtonText", at = @At("TAIL"))
    private void updateDifficultyButtonText(CallbackInfo ci) {
        if (this.buttonDifficulty != null)
            this.buttonDifficulty.displayString = getNameOfDifficulty();
    }

    @Inject(method = "actionPerformed", at = @At("TAIL"))
    private void onActionPerformed(GuiButton btn, CallbackInfo ci) {
        switch (btn.id) {
            case 50: // Difficulty button
                this.difficultyIndice++;
                if (this.difficultyIndice > RenewedDifficulties.LIST.size() - 1) {
                    this.difficultyIndice = 0;
                }
                this.updateButtonText();
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

    @Unique
    private Difficulty getSelectedDifficulty() {
        return RenewedDifficulties.LIST.get(difficultyIndice);
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
