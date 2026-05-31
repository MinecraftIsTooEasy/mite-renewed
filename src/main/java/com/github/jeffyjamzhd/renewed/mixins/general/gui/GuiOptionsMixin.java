package com.github.jeffyjamzhd.renewed.mixins.general.gui;

import com.github.jeffyjamzhd.renewed.api.IWorldInfo;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GuiOptions.class)
@Environment(EnvType.CLIENT)
abstract public class GuiOptionsMixin extends GuiScreen {
    @Inject(method = "initGui", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 1))
    private void setDifficultyButton(CallbackInfo ci, @Local GuiSmallButton btn) {
        if (this.mc.theWorld == null) {
            return;
        }

        WorldInfo info = this.mc.theWorld.getWorldInfo();
        Difficulty difficulty = ((IWorldInfo) info).mr$getDifficulty();
        btn.displayString = "%s %s".formatted(I18n.getString("difficulty.prefix"), difficulty.getLocalizedName());
    }
}
