package com.github.jeffyjamzhd.renewed;

import com.github.jeffyjamzhd.renewed.api.event.CraftingSoundRegisterEvent;
import com.github.jeffyjamzhd.renewed.api.event.HandpanRegisterEvent;
import com.github.jeffyjamzhd.renewed.api.event.MusicConditionRegisterEvent;
import com.github.jeffyjamzhd.renewed.block.entity.TileEntityCrate;
import com.github.jeffyjamzhd.renewed.command.CommandTransform;
import com.github.jeffyjamzhd.renewed.handler.RenewedTileEntityData;
import com.github.jeffyjamzhd.renewed.registry.*;
import com.github.jeffyjamzhd.renewed.render.tile.TileEntityCrateRenderer;
import com.google.common.eventbus.Subscribe;
import moddedmite.rustedironcore.api.event.Handlers;
import moddedmite.rustedironcore.api.event.events.TileEntityDataTypeRegisterEvent;
import net.xiaoyu233.fml.reload.event.*;

import java.util.function.Consumer;

public class EventListen extends Handlers {

    @Subscribe
    public void onItemRegister(ItemRegistryEvent event) {
        RenewedItems.register(event);
    }

    @Subscribe
    public void onRecipesModify(RecipeModifyEvent event) {
        //RenewedRecipes.modifyRecipes(event);
    }

    @Subscribe
    public void onEntityRegister(EntityRegisterEvent event) {
        TileEntityData.register(new RenewedTileEntityData());
        RenewedEntity.register(event);
    }

    @Subscribe
    public void onEntityRendererRegister(EntityRendererRegistryEvent event) {
        RenewedEntityRenderer.register(event);
    }

    @Subscribe
    public void onTileEntityRegister(TileEntityRegisterEvent event) {
        RenewedBlocks.registerTileEntities(event);
    }

    @Subscribe
    public void onTileEntityRendererRegister(TileEntityRendererRegisterEvent event) {
        event.register(TileEntityCrate.class, new TileEntityCrateRenderer());
    }

    @Subscribe
    public void onSoundsRegister(SoundsRegisterEvent event) {
        RenewedSounds.register(event);
    }


    @Subscribe
    public void onEnchantmentsRegister(EnchantmentRegistryEvent event) {
        RenewedEnchantments.register(event);
    }

    @Subscribe
    public void onCommandRegister(CommandRegisterEvent event) {
        event.register(new CommandTransform());
    }

    public static void register() {
        EntityTracker.register(new RenewedTracker());
        LootTable.register(new RenewedLootTable());
        Crafting.register(RenewedRecipes::registerRecipes);

        CraftingSoundRegisterEvent.register(new RenewedCraftingSounds());
        HandpanRegisterEvent.register(new RenewedHandpanRecipes());
        MusicConditionRegisterEvent.register(new RenewedMusicConditions());

        // Must be called last
        PotionRegistry.register(new RenewedPotion.Register());
    }
}
