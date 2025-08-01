package com.github.jeffyjamzhd.renewed.item.recipe;

import net.minecraft.*;

import java.util.ArrayList;
import java.util.List;

public class ShapelessToolRecipe implements IRecipe {
    public final ItemStack output;
    public final ItemStack tool;
    public final List<ItemStack> ingredients;
    private int[] skillsets;
    private boolean lowestDifficultyDetermination = false;
    private Material hardnessMaterial;
    private float difficulty;
    private int damage;

    /**
     * Creates a new ShapelessToolRecipe instance
     * @param output Item to give upon craft completion
     * @param tool Tool to use in crafting
     * @param inputs Ingredients of this recipe
     */
    public ShapelessToolRecipe(ItemStack output, ItemStack tool, List<ItemStack> inputs) {
        this.output = output;
        this.tool = tool;
        this.ingredients = inputs;
        this.damage = 66;
        this.difficulty = 2000F;

        RecipeHelper.addRecipe(this, true);
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
        ArrayList<ItemStack> ingredients = new ArrayList<>();

        // Iterate through grid
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack current = inv.getStackInSlot(i);
            // If an item is there (not air)
            if (current != null) {
                // Check for tool item
                if (current.isTool()) {
                    // Item is tool, we only want one
                    if (tool != null)
                        return false;

                    tool = current;
                } else {
                    // Item is not tool, match against ingredients
                    boolean match = false;
                    for (ItemStack ingredient : this.ingredients) {
                        if (ingredient.itemID == current.itemID) {
                            match = true;
                            break;
                        }
                    }

                    // No match, no pass
                    if (!match)
                        return false;

                    ingredients.add(current);
                }
            }
        }

        return tool != null && ingredients.size() == this.ingredients.size();
    }

    @Override
    public CraftingResult getCraftingResult(InventoryCrafting matrix) {
        return new CraftingResult(this.output.copy(), this.difficulty, this.skillsets, this);
    }

    @Override
    public int getRecipeSize() {
        return this.ingredients.size();
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.output;
    }

    @Override
    public ItemStack[] getComponents() {
        ItemStack[] recipeItems = new ItemStack[this.ingredients.size() + 1];

        // Set items
        recipeItems[0] = this.tool;
        for (int i = 0; i < this.ingredients.size(); ++i)
            recipeItems[i] = this.ingredients.get(i);

        return new ItemStack[0];
    }

    /**
     * Sets the difficulty value (craft time) of this recipe
     * @param difficulty Difficulty value
     * @return This instance
     */
    @Override
    public IRecipe setDifficulty(float difficulty) {
        this.difficulty = difficulty;
        return this;
    }

    @Override
    public IRecipe scaleDifficulty(float factor) {
        this.difficulty *= factor;
        return this;
    }

    @Override
    public float getUnmodifiedDifficulty() {
        return this.difficulty;
    }

    @Override
    public void setIncludeInLowestCraftingDifficultyDetermination() {
        this.lowestDifficultyDetermination = true;
    }

    @Override
    public boolean getIncludeInLowestCraftingDifficultyDetermination() {
        return this.lowestDifficultyDetermination;
    }

    @Override
    public void setSkillsets(int[] skillsets) {
        this.skillsets = skillsets;
    }

    @Override
    public void setSkillset(int skill) {
        int[] nArray;
        if (skill == 0) {
            nArray = null;
        } else {
            int[] nArray2 = new int[1];
            nArray = nArray2;
            nArray2[0] = skill;
        }
        this.skillsets = nArray;
    }

    @Override
    public int[] getSkillsets() {
        return this.skillsets;
    }

    @Override
    public void setMaterialToCheckToolBenchHardnessAgainst(Material material) {
        this.hardnessMaterial = material;
    }

    @Override
    public Material getMaterialToCheckToolBenchHardnessAgainst() {
        return this.hardnessMaterial;
    }
}
