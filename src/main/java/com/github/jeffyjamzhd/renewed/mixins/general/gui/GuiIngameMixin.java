package com.github.jeffyjamzhd.renewed.mixins.general.gui;

import com.github.jeffyjamzhd.renewed.RenewedConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.GuiIngame;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = GuiIngame.class, priority = 50)
@Environment(EnvType.CLIENT)
public class GuiIngameMixin {
    @Unique
    private static int PADDING = 10;

    @Inject(method = "renderGameOverlay", at = @At("HEAD"))
    private void getPaddingValue(float a, boolean b, int c, int d, CallbackInfo ci) {
        PADDING = RenewedConfig.HUD_PADDING.getIntegerValue();
    }

    @ModifyArg(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiIngame;func_110327_a(II)V"), index = 1)
    private int modifyYOfHearts(int par1) {
        return par1 - PADDING;
    }

    @ModifyArg(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiIngame;renderInventorySlot(IIIF)V"), index = 2)
    private int modifyYOfSlots(int par1) {
        return par1 - PADDING;
    }

    @ModifyArg(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/FontRenderer;drawStringWithShadow(Ljava/lang/String;III)I"), index = 2,
    slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/FontRenderer;drawStringWithShadow(Ljava/lang/String;III)I", ordinal = 0),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/FontRenderer;drawStringWithShadow(Ljava/lang/String;III)I", ordinal = 1)
    ))
    private int modifyYOfToolHighlight(int par2) {
        return par2 - PADDING;
    }

    @ModifyArg(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V"), index = 1)
    private float modifyYOfGlTranslate(float x) {
        return x - PADDING;
    }

    @ModifyArg(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiIngame;drawTexturedModalRect(IIIIII)V"), index = 1,
    slice = @Slice(
            from = @At(value = "INVOKE", target = "Lnet/minecraft/GuiIngame;drawTexturedModalRect(IIIIII)V", ordinal = 4),
            to = @At(value = "INVOKE", target = "Lnet/minecraft/GuiIngame;drawTexturedModalRect(IIIIII)V", ordinal = 7)
    ))
    private int modifyYOfRectElements(int par1) {
        return par1 - PADDING;
    }

    @ModifyArg(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiIngame;drawTexturedModalRect(IIIIII)V"), index = 1,
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/GuiIngame;drawTexturedModalRect(IIIIII)V", ordinal = 1),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/GuiIngame;drawTexturedModalRect(IIIIII)V", ordinal = 2)
            ))
    private int modifyYOfRectHotbar(int par1) {
        return par1 - PADDING;
    }

    @ModifyArg(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiIngame;drawTexturedModalRect(IIIIII)V", ordinal = 2), index = 5)
    private int modifyTexUOfHotbarSelector(int par1) {
        return 24;
    }

    @ModifyArg(method = "renderGameOverlay", at = @At(value = "INVOKE", target = "Lnet/minecraft/FontRenderer;drawString(Ljava/lang/String;III)I"), index = 2,
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/FontRenderer;drawString(Ljava/lang/String;III)I", ordinal = 0),
                    to = @At(value = "INVOKE", target = "Lnet/minecraft/FontRenderer;drawString(Ljava/lang/String;III)I", ordinal = 4)
            ))
    private int modifyYOfStringElements(int par3) {
        return par3 - PADDING;
    }

}
