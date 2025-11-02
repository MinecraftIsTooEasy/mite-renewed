package com.github.jeffyjamzhd.renewed.mixins.entity;

import net.minecraft.EntityRevenant;
import net.minecraft.EntityZombie;
import net.minecraft.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(EntityRevenant.class)
public abstract class EntityRevenantMixin extends EntityZombie {
    public EntityRevenantMixin(World par1World) {
        super(par1World);
    }

    @Override
    public void makeSound(String sound) {
        super.makeSound(sound, 1F, .6F);
    }
}
