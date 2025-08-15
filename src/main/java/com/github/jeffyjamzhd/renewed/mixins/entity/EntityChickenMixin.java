package com.github.jeffyjamzhd.renewed.mixins.entity;

import com.github.jeffyjamzhd.renewed.registry.RenewedItems;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(EntityChicken.class)
public abstract class EntityChickenMixin extends EntityLivestock {
    public EntityChickenMixin(World world) {
        super(world);
    }

    @Inject(method = "dropFewItems", at = @At("TAIL"))
    private void addGizzardChance(boolean hitByPlayer, DamageSource source, CallbackInfo ci) {
        if (this.isWell()) {
            Random rng = this.getRNG();
            boolean drop = .2F >= rng.nextFloat();
            if (drop)
                this.dropItemStack(new ItemStack(RenewedItems.raw_poultry, 1, 2));
        }
    }
}
