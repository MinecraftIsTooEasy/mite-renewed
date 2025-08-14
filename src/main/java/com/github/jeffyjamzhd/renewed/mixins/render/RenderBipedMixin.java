package com.github.jeffyjamzhd.renewed.mixins.render;

import com.github.jeffyjamzhd.renewed.api.IModelBiped;
import com.github.jeffyjamzhd.renewed.item.ItemPolearm;
import com.github.jeffyjamzhd.renewed.render.ItemRenderPolearm;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = RenderPlayer.class)
public abstract class RenderBipedMixin extends RendererLivingEntity {
    @Shadow private ModelBiped modelBipedMain;
    @Shadow private ModelBiped modelArmorChestplate;
    @Shadow private ModelBiped modelArmor;

    public RenderBipedMixin(ModelBase par1ModelBase, float par2) {
        super(par1ModelBase, par2);
    }

    @Inject(method = "renderSpecials", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V", ordinal = 4, shift = At.Shift.AFTER), cancellable = true)
    protected void hookHeldItemRender(
            AbstractClientPlayer clientPlayer, float par2, CallbackInfo ci, @Local(name = "var28")ItemStack stack) {
        if (stack.getItem() instanceof ItemPolearm) {
            ci.cancel();
            ItemRenderPolearm.renderPolearm3D(clientPlayer);
            this.renderManager.itemRenderer.renderItem(clientPlayer, stack, 2);
            GL11.glPopMatrix();
        }
    }
}
