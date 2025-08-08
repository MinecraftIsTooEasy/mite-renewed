package com.github.jeffyjamzhd.renewed.mixins.compat.modmenu;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.terraformersmc.modmenu.config.ModMenuConfig;
import net.minecraft.GuiMainMenu;
import net.xiaoyu233.fml.FishModLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiMainMenu.class, priority = 2000)
public class GuiMainMenuMMMixin {
    @TargetHandler(
            mixin = "com.terraformersmc.modmenu.mixin.MixinTitleScreen",
            name = "onInit"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lcom/terraformersmc/modmenu/config/option/EnumConfigOption;getValue()Ljava/lang/Enum;", ordinal = 4))
    private void modifyHeight(CallbackInfo ci, @Local(name = "buttonsY") LocalIntRef height) {
        int value = height.get();
        height.set(value + 36);
    }

    @Inject(method = "initGui", at = @At("HEAD"))
    void forceValue(CallbackInfo ci) {
        if (FishModLoader.hasMod("modmenu"))
            ModMenuConfig.MODS_BUTTON_STYLE.setValue(ModMenuConfig.TitleMenuButtonStyle.ICON);
    }
}
