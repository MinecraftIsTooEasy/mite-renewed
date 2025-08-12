package com.github.jeffyjamzhd.renewed.mixins.entity;

import com.github.jeffyjamzhd.renewed.api.IEntity;
import net.minecraft.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Entity.class)
public class EntityMixin implements IEntity {
    @Shadow private int fire;

    @Override
    public boolean mr$onFire() {
        return this.fire > 0;
    }
}
