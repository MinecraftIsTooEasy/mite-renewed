package com.github.jeffyjamzhd.renewed.mixins.general.gui;

import com.github.jeffyjamzhd.renewed.api.compat.IGuiWorldOption;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.network.C2SAssignDifficulty;
import com.github.jeffyjamzhd.renewed.render.gui.GuiConfigureButton;
import com.github.jeffyjamzhd.renewed.render.gui.GuiCustomizeWorldDifficulty;
import moddedmite.rustedironcore.network.Network;
import moddedmite.xylose.bettergamesetting.client.gui.GuiWorldOption;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.GuiButton;
import net.minecraft.GuiScreen;
import net.minecraft.I18n;
import net.minecraft.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(GuiWorldOption.class)
@Environment(EnvType.CLIENT)
public abstract class GuiWorldOptionMixin extends GuiScreen implements IGuiWorldOption {
    @Unique
    private GuiButton difficultyButton;
    @Unique
    private GuiButton difficultyConfigButton;
    @Unique
    private int currentDifficultyIndice;

    @Redirect(method = "initGui", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 2))
    private <E> boolean addRenewedDifficultyButtons(List<E> instance, E e) {
        if (this.mc.theWorld == null) {
            return instance.add(e);
        }

        this.buttonList.add(this.difficultyButton = new GuiButton(1000, this.width / 2 - 155, 60, 130, 20, getNameOfDifficulty()));
        this.buttonList.add(this.difficultyConfigButton = new GuiConfigureButton(1001, this.width / 2 - 25, 60));

        boolean isOP = this.mc.thePlayer.canCommandSenderUseCommand(2, "");
        boolean isSP = Minecraft.isSingleplayer();
        this.difficultyButton.enabled = isOP || isSP;
        this.difficultyConfigButton.enabled = isOP || isSP;

        return false;
    }

    @Inject(method = "actionPerformed", at = @At("TAIL"))
    private void addDifficultyButtonFunction(GuiButton button, CallbackInfo ci) {
        if (button.enabled) {
            switch (button.id) {
                case 1000: // Difficulty button

                    break;
                case 1001: // Config button
                    GuiCustomizeWorldDifficulty screen = new GuiCustomizeWorldDifficulty(this, Difficulty.getFromWorld(mc.theWorld).orElseThrow());
                    this.mc.displayGuiScreen(screen);
                    break;
            }
        }
    }

    @Unique
    private String getNameOfDifficulty() {
        Difficulty difficulty = Difficulty.getFromWorld(mc.theWorld).orElseThrow();
        return "%s %s".formatted(I18n.getString("difficulty.prefix"), difficulty.getLocalizedName());
    }

    @Override
    public void mr$enableEditOptions() {
        this.difficultyButton.enabled = true;
        this.difficultyConfigButton.enabled = true;
    }

    @Override
    public void mr$attemptAssigningCustomDifficulty(Difficulty difficulty) {
        Network.sendToServer(new C2SAssignDifficulty(this.mc.thePlayer, difficulty));
    }
}
