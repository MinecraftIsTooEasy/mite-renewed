package com.github.jeffyjamzhd.renewed.mixins.gui;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.sugar.Local;
import moddedmite.xylose.bettergamesetting.util.ScreenUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.GuiButton;
import net.minecraft.GuiCreateWorld;
import net.minecraft.GuiScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = GuiCreateWorld.class, priority = 1500)
@Environment(EnvType.CLIENT)
abstract public class GuiCreateWorldBGSMixin extends GuiScreen {
    @Unique
    private GuiButton buttonDifficulty;
    @Unique
    private GuiButton buttonDifficultyConfig;

    @TargetHandler(
            mixin = "moddedmite.xylose.bettergamesetting.mixin.client.gui.GuiCreateWorldMixin",
            name = "recreateButtons"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At("HEAD"))
    private void addButtons(CallbackInfo ci) {
        // Todo: replace hardcoded strings
        this.buttonList.add(this.buttonDifficulty = new GuiButton(50, this.width / 2 - 104, this.height / 5 + 50, 188, 20, "Difficulty: Extreme"));
        this.buttonList.add(this.buttonDifficultyConfig = new GuiButton(51, this.width / 2 + 84, this.height / 5 + 50, 20, 20, "C"));
    }

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
            ScreenUtil.getInstance().drawTooltip("The intended difficulty of MiTE. A devious blend of survival and combat challenges. Prepare to die!", mouseX, mouseY);
        }
    }
}
