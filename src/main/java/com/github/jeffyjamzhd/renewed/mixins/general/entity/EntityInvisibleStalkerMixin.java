package com.github.jeffyjamzhd.renewed.mixins.general.entity;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.EntityInvisibleStalker;
import net.minecraft.EntityMob;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityInvisibleStalker.class)
public abstract class EntityInvisibleStalkerMixin extends EntityMob {
    public EntityInvisibleStalkerMixin(World par1World) {
        super(par1World);
    }

    @ModifyReturnValue(method = "getLivingSound", at = @At("RETURN"))
    private String noSound(String original) {
        return null;
    }
}
