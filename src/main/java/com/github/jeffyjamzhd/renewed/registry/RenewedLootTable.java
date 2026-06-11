package com.github.jeffyjamzhd.renewed.registry;

import moddedmite.rustedironcore.api.event.listener.ILootTableRegisterListener;
import net.minecraft.WeightedRandomChestContent;

import java.util.List;

public class RenewedLootTable implements ILootTableRegisterListener {
    @Override
    public void onDungeonOverworldRegister(List<WeightedRandomChestContent> original) {
        original.add(new WeightedRandomChestContent(RenewedItems.bag_of_holding.itemID, 0, 1, 1, 3));
    }

    @Override
    public void onDungeonUnderworldRegister(List<WeightedRandomChestContent> original) {
        original.add(new WeightedRandomChestContent(RenewedItems.bag_of_holding.itemID, 0, 1, 1, 2));
    }
}
