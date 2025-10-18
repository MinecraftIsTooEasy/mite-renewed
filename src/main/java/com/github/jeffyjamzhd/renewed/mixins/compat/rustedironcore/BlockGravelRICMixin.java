package com.github.jeffyjamzhd.renewed.mixins.compat.rustedironcore;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moddedmite.rustedironcore.api.event.Handlers;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(value = BlockGravel.class, priority = 2000)
abstract class BlockGravelRICMixin extends BlockFalling {
    public BlockGravelRICMixin(int par1, Material material, BlockConstants constants) {
        super(par1, material, constants);
    }

    @TargetHandler(
            mixin = "moddedmite.rustedironcore.mixin.block.BlockGravelMixin",
            name = "dropBlockAsEntityItem"
    )
    @Inject(method = "@MixinSquared:Handler", at = @At("HEAD"), cancellable = true)
    private void dropBlockAsEntityItemFist(BlockBreakInfo info, CallbackInfoReturnable<Integer> cir) {
        if (!info.wasExploded() && info.wasHarvestedByPlayer()) {
            // Do not run if we're using an item
            if (info.getHarvesterItem() != null && info.getHarvesterItem() instanceof ItemTool) {
                return;
            }

            // Handle gravel + flint drop rates
            Random rand = info.world.rand;
            float gravelChance = Handlers.GravelDrop.onDropAsGravelChanceModify(info, .65F);

            // Drop gravel
            if (rand.nextFloat() < gravelChance) {
                cir.setReturnValue(super.dropBlockAsEntityItem(info));
                return;
            }

            float chipChance = Handlers.GravelDrop.onDropAsFlintChanceModify(info, .9F);
            if (rand.nextFloat() < chipChance) {
                cir.setReturnValue(dropBlockAsEntityItem(info, Item.chipFlint));
            } else {
                cir.setReturnValue(dropBlockAsEntityItem(info, Item.flint));
            }
            info.getResponsiblePlayer().triggerAchievement(AchievementList.flintFinder);
        }
    }
}
