package com.github.jeffyjamzhd.renewed.mixins.entity;

import net.minecraft.EntityBoneLord;
import net.minecraft.EntitySkeleton;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityBoneLord.class)
public abstract class EntityBoneLordMixin extends EntitySkeleton {
    public EntityBoneLordMixin(World par1World) {
        super(par1World);
    }

    @Override
    public void makeSound(String sound) {
        super.makeSound(sound, 1F, .75F);
    }
}
