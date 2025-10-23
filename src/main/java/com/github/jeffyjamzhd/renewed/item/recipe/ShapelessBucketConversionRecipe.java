package com.github.jeffyjamzhd.renewed.item.recipe;

import net.minecraft.*;

import java.util.List;
import java.util.Optional;

public class ShapelessBucketConversionRecipe extends ShapelessRecipes {
    private final Item emptyBucket;
    private final Item stoneBucket;
    private boolean damagesItem = true;

    /**
     * Creates a new ShapelessBucketConversionRecipe instance
     * @param material Bucket material
     */
    public ShapelessBucketConversionRecipe(Material material) {
        super(
                new ItemStack(Item.bucketCopperEmpty.getPeerForVesselMaterial(material)),
                List.of(new ItemStack(Item.bucketCopperStone.getPeerForVesselMaterial(material))),
                false
        );
        this.emptyBucket = Item.bucketCopperEmpty.getPeerForVesselMaterial(material);
        this.stoneBucket = Item.bucketCopperStone.getPeerForVesselMaterial(material);
    }

    public ShapelessBucketConversionRecipe doesntDamageItem() {
        this.damagesItem = false;
        return this;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World world) {
        Optional<ItemStack> bucket = getBucket(inv);
        return bucket.isPresent();
    }

    private Optional<ItemStack> getBucket(InventoryCrafting inv) {
        ItemStack bucket = null;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack current = inv.getStackInSlot(i);

            // Check for bucket
            if (current != null) {
                if (current.getItem() == this.stoneBucket) {
                    if (bucket != null) {
                        // Too many buckets!
                        return Optional.empty();
                    }

                    bucket = current;
                    if (bucket.getItemDamage() + 1 >= bucket.getMaxDamage()) {
                        // Too damaged, return stone instead
                        return Optional.of(new ItemStack(Block.cobblestone));
                    }
                }
            }
        }
        return Optional.ofNullable(bucket);
    }

    @Override
    public CraftingResult getCraftingResult(InventoryCrafting inv) {
        Optional<ItemStack> bucket = getBucket(inv);
        if (bucket.isPresent()) {
            ItemStack bucketStack = bucket.get();
            ItemStack outputStack;
            if (bucketStack.isBlock()) {
                outputStack = bucketStack;
            } else {
                outputStack = new ItemStack(this.emptyBucket).setItemDamage(bucketStack.getItemDamage() + 1);
            }
            return new CraftingResult(outputStack, this.getUnmodifiedDifficulty(), this.getSkillsets(), this);
        }
        return super.getCraftingResult(inv);
    }

    @Override
    public int getRecipeSize() {
        return 1;
    }
}

