package com.github.jeffyjamzhd.renewed;

import com.github.jeffyjamzhd.renewed.api.IWorldInfo;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.api.event.CraftingSoundRegisterEvent;
import com.github.jeffyjamzhd.renewed.api.event.HandpanRegisterEvent;
import com.github.jeffyjamzhd.renewed.api.event.MusicConditionRegisterEvent;
import com.github.jeffyjamzhd.renewed.command.CommandTransform;
import com.github.jeffyjamzhd.renewed.network.S2CSyncDifficulty;
import com.github.jeffyjamzhd.renewed.registry.*;
import com.google.common.eventbus.Subscribe;
import moddedmite.rustedironcore.api.event.Handlers;
import moddedmite.rustedironcore.network.Network;
import net.minecraft.WorldServer;
import net.xiaoyu233.fml.reload.event.*;

public class EventListen extends Handlers {

    @Subscribe
    public void onItemRegister(ItemRegistryEvent event) {
        RenewedItems.register(event);
    }

    @Subscribe
    public void onRecipeRegister(RecipeRegistryEvent event) {
        RenewedRecipes.registerRecipes(event);
    }

    @Subscribe
    public void onRecipesModify(RecipeModifyEvent event) {
        //RenewedRecipes.modifyRecipes(event);
    }

    @Subscribe
    public void onEntityRegister(EntityRegisterEvent event) {
        RenewedEntity.register(event);
    }

    @Subscribe
    public void onEntityRendererRegister(EntityRendererRegistryEvent event) {
        RenewedEntityRenderer.register(event);
    }

    @Subscribe
    public void onSoundsRegister(SoundsRegisterEvent event) {
        RenewedSounds.register(event);
    }

    @Subscribe
    public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
        WorldServer world = event.getPlayer().getWorldServer();
        Difficulty difficulty = ((IWorldInfo)world.getWorldInfo()).mr$getDifficulty();
        Network.sendToClient(event.getPlayer(), new S2CSyncDifficulty(difficulty));
    }

    @Subscribe
    public void handleChatCommand(HandleChatCommandEvent event) {
    }

    @Subscribe
    public void onCommandRegister(CommandRegisterEvent event) {
        event.register(new CommandTransform());
    }

    public static void register() {
        EntityTracker.register(new RenewedTracker());
        CraftingSoundRegisterEvent.register(new RenewedCraftingSounds());
        HandpanRegisterEvent.register(new RenewedHandpanRecipes());
        MusicConditionRegisterEvent.register(new RenewedMusicConditions());
    }
}
