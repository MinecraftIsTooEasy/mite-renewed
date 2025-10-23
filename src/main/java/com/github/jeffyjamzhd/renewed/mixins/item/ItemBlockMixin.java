package com.github.jeffyjamzhd.renewed.mixins.item;

import net.minecraft.ItemBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ItemBlock.class)
public class ItemBlockMixin {
    @ModifyConstant(method = "getBurnTime", constant = @Constant(intValue = 800, ordinal = 0))
    public int nerfTorchTime(int constant) {
        return 400;
    }
}
