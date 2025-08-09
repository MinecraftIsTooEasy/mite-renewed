package com.github.jeffyjamzhd.renewed.item.recipe;

import net.minecraft.Block;
import net.minecraft.Item;
import net.minecraft.ItemStack;

import java.util.Random;

public class HandpanOutput {
    private final ItemStack item;
    private final float chance;

    public HandpanOutput(Item item, float chance) {
        this.item = new ItemStack(item);
        this.chance = chance;
    }

    public HandpanOutput(Block block, float chance) {
        this.item = new ItemStack(block);
        this.chance = chance;
    }

    public HandpanOutput(ItemStack item, float chance) {
        this.item = item;
        this.chance = chance;
    }

    public static HandpanOutput of(Item item, float chance) {
        return new HandpanOutput(item, chance);
    }

    public static HandpanOutput of(Block block, float chance) {
        return new HandpanOutput(block, chance);
    }

    public ItemStack getItem() {
        return this.item;
    }

    public float getChance() {
        return this.chance;
    }

    public boolean rollDrop(Random rand) {
        return rand.nextFloat() < this.chance;
    }
}