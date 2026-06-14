package com.github.jeffyjamzhd.renewed.mixins.general.gui;

import com.github.jeffyjamzhd.renewed.api.IWorldInfo;
import com.github.jeffyjamzhd.renewed.api.compat.IGuiWorldOption;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.network.C2SAssignDifficulty;
import com.github.jeffyjamzhd.renewed.network.ValidatePlayerAuth;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.github.jeffyjamzhd.renewed.render.gui.GuiConfigureButton;
import com.github.jeffyjamzhd.renewed.render.gui.GuiCustomizeWorldDifficulty;
import moddedmite.rustedironcore.network.Network;
import moddedmite.xylose.bettergamesetting.client.gui.GuiWorldOption;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;
import net.minecraft.server.MinecraftServer;
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

        Difficulty difficulty = Difficulty.getFromWorld(mc.theWorld).orElseThrow();
        this.buttonList.add(this.difficultyButton = new GuiButton(1000, this.width / 2 - 155, 60, 130, 20, getNameOfDifficulty()));
        this.buttonList.add(this.difficultyConfigButton = new GuiConfigureButton(1001, this.width / 2 - 25, 60));
        this.currentDifficultyIndice = Difficulty.getIndiceForDifficulty(difficulty);

        if (!isSingleplayer()) {
            mr$updateButtonUsability(false);
            Network.sendToServer(new ValidatePlayerAuth.C2S(this.mc.thePlayer));
        }

        return false;
    }

    @Inject(method = "actionPerformed", at = @At("TAIL"))
    private void addDifficultyButtonFunction(GuiButton button, CallbackInfo ci) {
        if (button.enabled) {
            switch (button.id) {
                case 1000: // Difficulty button
                    this.currentDifficultyIndice = (this.currentDifficultyIndice + 1) % RenewedDifficulties.LIST.size();
                    mr$assignOrSendDifficulty(RenewedDifficulties.LIST.get(this.currentDifficultyIndice));
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

    @Unique
    private boolean canUseMenu(EntityPlayer player) {
        if (isSingleplayer()) return true;

        boolean isOP = player.canCommandSenderUseCommand(2, "");
        boolean isOwner = MinecraftServer.isPlayerHostingGame(player);
        return isOP || isOwner;
    }

    @Unique
    private boolean isSingleplayer() {
        return Minecraft.isSingleplayer() && this.mc.getIntegratedServer() != null;
    }

    @Override
    public void mr$updateButtonUsability(boolean mode) {
        this.difficultyButton.enabled = mode;
        this.difficultyConfigButton.enabled = mode;
    }

    @Override
    public void mr$updateButtonText() {
        this.difficultyButton.displayString = getNameOfDifficulty();
    }

    @Override
    public void mr$assignOrSendDifficulty(Difficulty difficulty) {
        if (isSingleplayer()) {
            // On singleplayer we set the integrated server info
            // (player always has authority in singleplayer)
            IWorldInfo mainInfo = (IWorldInfo) mc.theWorld.getWorldInfo();
            mainInfo.mr$setDifficulty(difficulty);

            WorldServer[] servers = mc.getIntegratedServer().worldServers;
            for (WorldServer server : servers) {
                IWorldInfo info = (IWorldInfo) server.getWorldInfo();
                info.mr$setDifficulty(difficulty);
            }

            mr$updateButtonText();
        } else {
            // Lan or net we just send the packet
            Network.sendToServer(new C2SAssignDifficulty(this.mc.thePlayer, difficulty));
        }
    }
}
