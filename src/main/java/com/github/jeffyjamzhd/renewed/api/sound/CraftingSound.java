package com.github.jeffyjamzhd.renewed.api.sound;

import net.minecraft.*;

public class CraftingSound {
    public int itemID;
    public Material material;
    private ConditionalSound sound;

    public CraftingSound(ConditionalSound sound) {
        this.sound = sound;
        this.itemID = -1;
        this.material = null;
    }

    public CraftingSound(Material material, ConditionalSound sound) {
        this(sound);
        this.material = material;
    }

    public CraftingSound(int id, ConditionalSound sound) {
        this(sound);
        this.itemID = id;
    }

    public void attemptSound(ItemStack output, IRecipe recipe, World world, EntityPlayer player) {
        this.sound.play(output, recipe, world, player);
    }

    public static CraftingSound of(Material material, ConditionalSound sound) {
        return new CraftingSound(material, sound);
    }

    public static CraftingSound of(int id, ConditionalSound sound) {
        return new CraftingSound(id, sound);
    }

    public record Sound(String id, float volume, float pitch) {
        public static Sound entry(String id, float volume, float pitch) {
            return new Sound(id, volume, pitch);
        }
    }

    public static Basic basicSound(String id, float volume, float pitch) {
        return new Basic(id, volume, pitch);
    }

    public static Metal metalSound(Material mat) {
        return new Metal(mat);
    }


    /**
     * Generic recipe craft sound, with no extra checks/variation
     */
    public static class Basic implements ConditionalSound {
        private String id;
        private float volume;
        private float pitch;

        public Basic(String id, float volume, float pitch) {
            this.id = id;
            this.volume = volume;
            this.pitch = pitch;
        }

        public void playSound(World world, EntityPlayer player) {
            world.playSoundAtEntity(player, this.id, this.volume, this.pitch);
        }

        @Override
        public void play(ItemStack output, IRecipe recipe, World world, EntityPlayer player) {
            playSound(world, player);
        }
    }

    /**
     * Metal craft sound
     */
    public static class Metal implements ConditionalSound {
        private Material material;

        public Metal(Material mat) {
            this.material = mat;
        }

        @Override
        public void play(ItemStack output, IRecipe recipe, World world, EntityPlayer player) {
            float pitch = 1.4F;
            ItemStack[] ingredients = recipe.getComponents();

            for (ItemStack component : ingredients) {
                if (component == null)
                    continue;
                if (component.hasMaterial(material))
                    pitch -= .1F;
            }
            world.playSoundAtEntity(player, "random.anvil_use", 0.6F, pitch);
        }
    }

    @FunctionalInterface
    public interface ConditionalSound {
        void play(ItemStack output, IRecipe recipe, World world, EntityPlayer player);
    }
}
