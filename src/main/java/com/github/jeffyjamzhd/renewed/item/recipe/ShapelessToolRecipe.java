package com.github.jeffyjamzhd.renewed.item.recipe;

import com.github.jeffyjamzhd.renewed.mixins.general.accessor.ShapelessRecipesAccessor;
import net.minecraft.*;

import java.util.HashSet;
import java.util.List;

public class ShapelessToolRecipe<T extends Item> extends ShapelessRecipes {
    public final Class<T> toolClass;
    public final HashSet<Class<? extends Item>> excludedClasses = new HashSet<>();
    private int damage;


    /**
     * Creates a new ShapelessToolRecipe instance
     * @param output Item to give upon craft completion
     * @param inputs Ingredients of this recipe
     */
    public ShapelessToolRecipe(Class<T> toolClass, ItemStack output, List<ItemStack> inputs) {
        super(output, inputs, false);
        this.toolClass = toolClass;
    }

    /**
     * Excludes any item of {@code clazz} from being considered a valid tool
     * @param clazz Class to exclude
     * @return This instance
     */
    public ShapelessToolRecipe<T> addExclusion(Class<? extends Item> clazz) {
        this.excludedClasses.add(clazz);
        return this;
    }

    /**
     * Sets the damage value to apply to the provided tool
     * @param value Amount to damage tool
     * @return This instance
     */
    public ShapelessToolRecipe<T> setDamage(int value) {
        this.damage = value;
        return this;
    }

    /**
     * Gives the amount of damage dealt to tool
     * @return Damage amount
     */
    public int getDamage() {
        return this.damage;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        ItemStack tool = null;
        int incomingIngCount = 0;
        List<ItemStack> ingredients = ((ShapelessRecipesAccessor)(this)).mr$getRecipeItems();

        // Iterate through grid
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack current = inv.getStackInSlot(i);
            // If an item is there (not air)
            if (current != null) {
                Item currentItem = current.getItem();

                // 1. Check if item is in our exclusion list first
                boolean isExcluded = false;
                for (Class<? extends Item> excluded : this.excludedClasses) {
                    if (excluded.isInstance(currentItem)) {
                        isExcluded = true;
                        break;
                    }
                }

                // 2. Perform tool matching if not excluded
                if (!isExcluded && this.toolClass.isInstance(currentItem)) {
                    if (tool != null) return false;
                    tool = current;
                } else {
                    // Match against ingredients
                    boolean valid = false;
                    for (ItemStack ingredient : ingredients) {
                        if (ingredient.itemID == current.itemID &&
                                (ingredient.getItemSubtype() == Short.MAX_VALUE || ingredient.getItemSubtype() == current.getItemSubtype())) {
                            incomingIngCount++;
                            valid = true;
                            break;
                        }
                    }
                    if (!valid) return false;
                }
            }
        }

        return tool != null && incomingIngCount == this.getRecipeSize();
    }

    @Override
    public int getRecipeSize() {
        return ((ShapelessRecipesAccessor)(this)).mr$getRecipeItems().size();
    }

}
