package com.github.jeffyjamzhd.renewed.mixins.entity;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Arrays;

@Environment(EnvType.CLIENT)
@Mixin(value = ClientPlayer.class, priority = 10)
public abstract class ClientPlayerMixin extends EntityPlayer {
    @Shadow public int crafting_ticks;
    @Shadow public int crafting_period;
    @Unique
    private static Material[] tier1Workbench = new Material[]{Material.flint, Material.obsidian};
    @Unique
    private static Material[] tier2Workbench = new Material[]{Material.copper, Material.silver, Material.gold};
    @Unique
    private static Material[] tier3Workbench = new Material[]{Material.iron};
    @Unique
    private static Material[] tier4Workbench = new Material[]{Material.ancient_metal};
    @Unique
    private static Material[] tier5Workbench = new Material[]{Material.mithril};
    @Unique
    private static Material[] tier6Workbench = new Material[]{Material.adamantium};

    public ClientPlayerMixin(World par1World, String par2Str) {
        super(par1World, par2Str);
    }

    @ModifyExpressionValue(method = "getCraftingPeriod", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/ClientPlayer;getLevelModifier(Lnet/minecraft/EnumLevelBonus;)F"))
    private float addWorkbenchBonus(float original) {
        return original + mr$getWorkbenchBonus();
    }

    @ModifyReturnValue(method = "calcUnmodifiedCraftingPeriod", at = @At(value = "RETURN", ordinal = 0))
    private static int uncapPeriod(int original, @Local(argsOnly = true) float adjusted) {
        return Math.round(adjusted);
    }

    @ModifyReturnValue(method = "getCraftingPeriod", at = @At("RETURN"))
    private int calculate(int original, @Local int period, @Local(ordinal = 1) float modifier) {
        float levelBonus = this.getLevelModifier(EnumLevelBonus.CRAFTING);
        float workbenchBonus = this.mr$getWorkbenchBonus();
        float value = (period / (1F + levelBonus + workbenchBonus + modifier));
        return (int) Math.max(value, 1F);
    }

    @Unique
    private float mr$getWorkbenchBonus() {
        Container open = this.openContainer;
        if (!(open instanceof ContainerWorkbench container))
            return 0F;

        Material workbenchMaterial = BlockWorkbench.getToolMaterial(container.getBlockMetadata());

        if (Arrays.stream(tier1Workbench).anyMatch(material -> material == workbenchMaterial))
            return .25F;
        if (Arrays.stream(tier2Workbench).anyMatch(material -> material == workbenchMaterial))
            return 1F;
        if (Arrays.stream(tier3Workbench).anyMatch(material -> material == workbenchMaterial))
            return 2F;
        if (Arrays.stream(tier4Workbench).anyMatch(material -> material == workbenchMaterial))
            return 3F;
        if (Arrays.stream(tier5Workbench).anyMatch(material -> material == workbenchMaterial))
            return 5F;
        if (Arrays.stream(tier6Workbench).anyMatch(material -> material == workbenchMaterial))
            return 15F;
        return 0F;
    }
}
