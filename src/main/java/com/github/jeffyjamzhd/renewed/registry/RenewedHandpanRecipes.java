package com.github.jeffyjamzhd.renewed.registry;

import com.github.jeffyjamzhd.renewed.api.event.listener.HandpanRegisterListener;
import com.github.jeffyjamzhd.renewed.item.recipe.HandpanOutput;
import com.github.jeffyjamzhd.renewed.item.recipe.HandpanRecipeProcessor;
import net.minecraft.Block;
import net.minecraft.Item;

public class RenewedHandpanRecipes implements HandpanRegisterListener {
    @Override
    public void register(HandpanRecipeProcessor registry) {
        // Sinew mesh recipes
        registry.registerHandpanRecipe(
                Block.gravel, 10, 120, 1,
                new HandpanOutput(Block.gravel, .5F),
                new HandpanOutput(Item.chipFlint, .33F),
                new HandpanOutput(Item.shardObsidian, .25F),
                new HandpanOutput(Item.copperNugget, .08F),
                new HandpanOutput(Item.flint, .05F)
        );
        registry.registerHandpanRecipe(
                Block.dirt, 5, 160, 1,
                new HandpanOutput(Block.dirt, .5F),
                new HandpanOutput(Item.wormRaw, .3F),
                new HandpanOutput(Item.bone, .05F),
                new HandpanOutput(Item.copperNugget, .05F)
        );
        registry.registerHandpanRecipe(
                Block.sand, 5, 80, 1,
                HandpanOutput.of(Block.sand, .5F),
                HandpanOutput.of(Item.clay, .33F),
                HandpanOutput.of(Item.reed, .1F)
        );

        // Silk/string mesh
        registry.registerHandpanRecipe(
                Block.gravel, 15, 160, 2,
                HandpanOutput.of(Block.gravel, .33F),
                HandpanOutput.of(Item.copperNugget, .5F),
                HandpanOutput.of(Item.silverNugget, .25F),
                HandpanOutput.of(Item.goldNugget, .075F),
                HandpanOutput.of(Item.shardDiamond, .02F)
        );
        registry.registerHandpanRecipe(
                Block.dirt, 10, 220, 2,
                HandpanOutput.of(Block.dirt, .33F),
                HandpanOutput.of(Item.wormRaw, .5F),
                HandpanOutput.of(Item.seeds, .75F),
                HandpanOutput.of(Item.pumpkinSeeds, .1F),
                HandpanOutput.of(Item.bone, .075F),
                HandpanOutput.of(Item.copperNugget, .1F)
        );
        registry.registerHandpanRecipe(
                Block.sand, 10, 120, 2,
                HandpanOutput.of(Block.sand, .33F),
                HandpanOutput.of(Item.reed, .2F),
                HandpanOutput.of(Item.shardGlass, .33F),
                HandpanOutput.of(Item.shardEmerald, .04F),
                HandpanOutput.of(Item.copperNugget, .1F)
        );
    }
}
