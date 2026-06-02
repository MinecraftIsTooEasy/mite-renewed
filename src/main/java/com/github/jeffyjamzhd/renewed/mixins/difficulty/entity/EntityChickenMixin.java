package com.github.jeffyjamzhd.renewed.mixins.difficulty.entity;

import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.registry.RenewedDifficulties;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityChicken.class)
public abstract class EntityChickenMixin extends EntityLivestock {
    public EntityChickenMixin(World world) {
        super(world);
    }

    @ModifyReturnValue(method = "isFoodItem", at = @At("RETURN"))
    private boolean foodCheck(boolean original, @Local(argsOnly = true) ItemStack stack) {
        if (stack == null) {
            return original;
        }

        Difficulty difficulty = Difficulty.getFromWorld(this.getWorld()).orElseThrow();
        int param = difficulty.getParamValue(RenewedDifficulties.CHICKEN_BREEDING_ITEM);
        return isValidForParameter(stack.getItem(), param);
    }

    @Unique
    private boolean isValidForParameter(Item item, int param) {
        return switch (param) {
            case 0 -> item == Item.seeds || item == Item.melonSeeds || item == Item.pumpkinSeeds;
            case 1 -> item == Item.wormRaw || item == Item.wormCooked;
            default -> false;
        };
    }
}
