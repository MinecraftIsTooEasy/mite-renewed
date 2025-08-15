package com.github.jeffyjamzhd.renewed.api.registry;

import com.github.jeffyjamzhd.renewed.api.sound.CraftingSound;
import net.minecraft.Material;

import java.util.HashMap;
import java.util.function.Consumer;

import static com.github.jeffyjamzhd.renewed.MiTERenewed.LOGGER;

public class CraftingSoundRegistry {
    public static CraftingSoundRegistry INSTANCE = new CraftingSoundRegistry();
    public static HashMap<Integer, CraftingSound> ITEM_SOUNDS = new HashMap<>();
    public static HashMap<Material, CraftingSound> MATERIAL_SOUNDS = new HashMap<>();
    private static Consumer<CraftingSound> REGISTRY = CraftingSoundRegistry::register;

    private static void register(CraftingSound sound) {
        if (sound.material != null)
            MATERIAL_SOUNDS.put(sound.material, sound);
        else
            ITEM_SOUNDS.put(sound.itemID, sound);
    }

    /**
     * Registers with checks for existing sounds
     * @param sound Craft sound to register
     */
    public void registerSafe(CraftingSound sound) {
        if (sound.material != null) {
            if (!MATERIAL_SOUNDS.containsKey(sound.material)) {
                REGISTRY.accept(sound);
            } else {
                LOGGER.error("CraftingSoundRegistry - Sound exists for %s!".formatted(sound.material.mr$getName()));
            }
        } else if (sound.itemID != -1) {
            if (!ITEM_SOUNDS.containsKey(sound.itemID)) {
                REGISTRY.accept(sound);
            } else {
                LOGGER.error("CraftingSoundRegistry - Sound exists for ItemID %d!".formatted(sound.itemID));
            }
        } else {
            LOGGER.error("CraftingSoundRegistry - Malformed crafting sound!");
        }
    }

    /**
     * Registers without checks
     * @param sound Craft sound to register
     */
    public void registerUnsafe(CraftingSound sound) {
        REGISTRY.accept(sound);
    }
}
