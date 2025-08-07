package com.github.jeffyjamzhd.renewed;

import com.github.jeffyjamzhd.renewed.registry.*;
import com.google.common.eventbus.Subscribe;
import moddedmite.rustedironcore.api.event.Handlers;
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
    }

    @Subscribe
    public void handleChatCommand(HandleChatCommandEvent event) {
    }

    public static void register() {
        EntityTracker.register(new RenewedTracker());
    }
}
