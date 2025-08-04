package com.github.jeffyjamzhd.renewed.item.recipe;

import com.github.jeffyjamzhd.renewed.mixins.accessor.ShapelessRecipesAccessor;
import net.minecraft.*;

import java.util.List;

public class ShapelessToolRecipe extends ShapelessRecipes {
    private int damage;

    /**
     * Creates a new ShapelessToolRecipe instance
     * @param output Item to give upon craft completion
     * @param inputs Ingredients of this recipe
     */
    public ShapelessToolRecipe(ItemStack output, List<ItemStack> inputs) {
        super(output, inputs, false);
    }

    /**
     * Sets the damage value to apply to the provided tool
     * @param value Amount to damage tool
     * @return This instance
     */
    public ShapelessToolRecipe setDamage(int value) {
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
        List<ItemStack> ingredients = ((ShapelessRecipesAccessor)(this)).getRecipeItems();

        // Iterate through grid
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack current = inv.getStackInSlot(i);
            // If an item is there (not air)
            if (current != null) {
                // Check for tool item
                if (current.isTool()) {
                    // Item is the right tool, make sure there is only one defined
                    if (tool != null)
                        return false;

                    tool = current;
                }
                // Item is not tool, match against ingredients
                for (ItemStack ingredient : ingredients) {
                    if (ingredient.itemID == current.itemID) {
                        incomingIngCount++;
                        break;
                    }
                }
            }
        }

        return tool != null && incomingIngCount == this.getRecipeSize();
    }

    @Override
    public int getRecipeSize() {
        return ((ShapelessRecipesAccessor)(this)).getRecipeItems().size();
    }

}
