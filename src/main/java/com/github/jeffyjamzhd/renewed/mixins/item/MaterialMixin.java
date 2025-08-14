package com.github.jeffyjamzhd.renewed.mixins.item;

import com.github.jeffyjamzhd.renewed.api.IMaterial;
import net.minecraft.Material;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Material.class)
public class MaterialMixin implements IMaterial {
    @Shadow protected String name;

    @Override
    public String mr$getName() {
        return this.name;
    }
}
