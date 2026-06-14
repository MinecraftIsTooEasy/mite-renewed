package com.github.jeffyjamzhd.renewed.mixins.general.gui;

import com.bawnorton.mixinsquared.TargetHandler;
import com.github.jeffyjamzhd.renewed.api.IWorldInfo;
import com.github.jeffyjamzhd.renewed.api.IWorldSettings;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.github.jeffyjamzhd.renewed.render.gui.GuiConfigureButton;
import com.github.jeffyjamzhd.renewed.render.gui.GuiCustomizeWorldDifficulty;
import com.github.jeffyjamzhd.renewed.render.gui.GuiLockDifficultyButton;
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

@Mixin(value = GuiCreateWorld.class, priority = 1500)
@Environment(EnvType.CLIENT)
abstract public class GuiCreateWorldBGSMixin extends GuiScreen implements com.github.jeffyjamzhd.renewed.api.IGuiCreateWorld {
    @Shadow
    protected abstract void updateButtonText();
    @Unique
    private GuiButton renewedDifficulty;
    @Unique
    private GuiConfigureButton buttonDifficultyConfig;
    @Unique
    private GuiLockDifficultyButton buttonDifficultyLock;
    @Unique
    private int difficultyIndice;
    @Unique
    private boolean difficultyLocked = false;
    @Unique
    private Difficulty customDifficulty;
    @Unique
    private GuiButton nativeDifficultyButton;

    @TargetHandler(
            mixin = "moddedmite.xylose.bettergamesetting.mixin.client.gui.GuiCreateWorldMixin",
            name = "recreateButtons"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At("HEAD"))
    private void addButtons(CallbackInfo ci) {
        this.buttonList.add(this.renewedDifficulty = new GuiButton(50, this.width / 2 - 104, this.height / 5 + 50, 168, 20, getNameOfDifficulty()));
        this.buttonList.add(this.buttonDifficultyConfig = new GuiConfigureButton(51, this.width / 2 + 64, this.height / 5 + 50));
        this.buttonList.add(this.buttonDifficultyLock = new GuiLockDifficultyButton(52, this.width / 2 + 84, this.height / 5 + 50));
        this.buttonDifficultyLock.toggled = this.difficultyLocked;
    }

    @TargetHandler(
            mixin = "moddedmite.xylose.bettergamesetting.mixin.client.gui.GuiCreateWorldMixin",
            name = "recreateButtons"
    )
    @ModifyArg(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2))
    private <E> E getNativeDifficultyButton(E element) {
        this.nativeDifficultyButton = (GuiButton) element;
        return element;
    }

    @TargetHandler(
            mixin = "moddedmite.xylose.bettergamesetting.mixin.client.gui.GuiCreateWorldMixin",
            name = "updateButtonVisibilityNAbility"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At("HEAD"))
    private void updateDifficultyButtons(CallbackInfo ci) {
        this.renewedDifficulty.drawButton = false;
        this.buttonDifficultyConfig.drawButton = false;
        this.buttonDifficultyLock.drawButton = false;

        if (((IGuiCreateWorld) this).bgs$getCurrentTab() == 100) {
            this.renewedDifficulty.drawButton = true;
            this.buttonDifficultyConfig.drawButton = true;
            this.buttonDifficultyLock.drawButton = true;
        }
    }

    @Inject(method = "updateButtonText", at = @At("TAIL"))
    private void updateDifficultyButtonText(CallbackInfo ci) {
        if (this.renewedDifficulty != null)
            this.renewedDifficulty.displayString = getNameOfDifficulty();
    }

    @Inject(method = "actionPerformed", at = @At(value = "INVOKE", target = "Lnet/minecraft/WorldSettings;func_82750_a(Ljava/lang/String;)Lnet/minecraft/WorldSettings;"))
    private void supplyDifficultyObject(GuiButton btn, CallbackInfo ci, @Local WorldSettings settings) {
        IWorldSettings cast = (IWorldSettings) settings;

        cast.mr$setDifficulty(getSelectedDifficulty());
        cast.mr$setDifficultyLocked(this.buttonDifficultyLock.toggled);
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

    @TargetHandler(
            mixin = "moddedmite.xylose.bettergamesetting.mixin.client.gui.GuiCreateWorldMixin",
            name = "updateButtonVisibilityNAbility"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At("TAIL"))
    private void updateButtonVisibility(CallbackInfo ci) {
        // Hide the default difficulty button
        this.nativeDifficultyButton.drawButton = false;
    }


    @TargetHandler(
            mixin = "moddedmite.xylose.bettergamesetting.mixin.client.gui.GuiCreateWorldMixin",
            name = "drawHoverText"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Ljava/util/Map;get(Ljava/lang/Object;)Ljava/lang/Object;"))
    private void drawDifficultyTooltip(int mouseX, int mouseY, CallbackInfo ci, @Local GuiButton button) {
        String text = switch (button.id) {
            case 50 -> getTooltipOfDifficulty();
            case 51 -> I18n.getString("difficulty.customize.tooltip");
            case 52 -> I18n.getString("difficulty.%s.tooltip".formatted(this.buttonDifficultyLock.toggled ? "unlock" : "lock"));
            default -> "";
        };

        if (!text.isEmpty()) {
            ScreenUtil.getInstance().drawTooltip(text, mouseX, mouseY);
        }
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
