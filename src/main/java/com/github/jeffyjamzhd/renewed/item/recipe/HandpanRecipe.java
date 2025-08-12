package com.github.jeffyjamzhd.renewed.item.recipe;

import net.minecraft.Block;
import net.minecraft.ItemStack;
import net.minecraft.Minecraft;
import net.minecraft.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.function.Consumer;

public class HandpanRecipe {
    // Handpan input
    private ItemStack input;
    // Chance outputs
    private List<HandpanOutput> outputs;
    // Pan subtype
    private byte subtype;
    // Damage to deal to handpan
    private int damage;
    // Speed of extraction
    private int speed;

    public HandpanRecipe(ItemStack input, List<HandpanOutput> outputs, byte subtype, int damage, int speed) {
        this.input = input;
        this.outputs = outputs;
        this.subtype = subtype;
        this.damage = damage;
        this.speed = speed;
    }

    public HandpanRecipe(ItemStack input, List<HandpanOutput> outputs) {
        new HandpanRecipe(input, outputs, (byte) 1, 10, 200);
    }

    public ItemStack getInput() {
        return this.input;
    }

    public List<HandpanOutput> getOutputs() {
        return this.outputs;
    }

    public byte getSubtype() {
        return this.subtype;
    }

    public int getDamage() {
        return damage;
    }

    public int getSpeed() {
        return this.speed;
    }

    public HandpanOutput getOutputAt(int ind) {
        return this.outputs.get(ind);
    }

    public List<ItemStack> generateOutput(World world) {
        // Declare variables
        Random random = world.rand;
        List<ItemStack> generated = new java.util.ArrayList<>();

        // Iterate
        for (HandpanOutput output : this.outputs) {
            // Roll random
            if (output.rollDrop(random))
                generated.add(output.getItem().copy());
        }

        return generated;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        HandpanRecipe that = (HandpanRecipe) o;
        return Objects.equals(input, that.input) && Objects.equals(outputs, that.outputs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(input, outputs);
    }

    public static class Builder {
        private ItemStack input;
        private List<HandpanOutput> outputs = new ArrayList<>();
        private byte subtype;
        private int damage;
        private int speed;

        private Builder(ItemStack input) {
            this.input = input;
            this.damage = 10;
            this.speed = 200;
        }

        public static Builder input(ItemStack input) {
            return new Builder(input);
        }

        public Builder output(HandpanOutput output) {
            this.outputs.add(output);
            return this;
        }

        public Builder output(ItemStack output, float weight) {
            this.outputs.add(new HandpanOutput(output, weight));
            return this;
        }

        public Builder output(Block output, float weight) {
            this.outputs.add(new HandpanOutput(new ItemStack(output), weight));
            return this;
        }

        public Builder subtype(int value) {
            this.subtype = (byte) value;
            return this;
        }

        public Builder damage(int value) {
            this.damage = value;
            return this;
        }

        public Builder speed(int value) {
            this.speed = value;
            return this;
        }

        public void build(Consumer<HandpanRecipe> register) {
            register.accept(new HandpanRecipe(this.input, this.outputs, this.subtype, this.damage, this.speed));
        }
    }
}
