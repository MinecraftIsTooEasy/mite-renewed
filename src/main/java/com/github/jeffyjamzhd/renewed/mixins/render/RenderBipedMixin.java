package com.github.jeffyjamzhd.renewed.mixins.render;

import com.github.jeffyjamzhd.renewed.render.fix.RenderBipedFix;
import net.minecraft.*;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RenderBiped.class)
public abstract class RenderBipedMixin extends RenderLiving {
    public RenderBipedMixin(ModelBase par1ModelBase, float par2) {
        super(par1ModelBase, par2);
    }

    @Inject(method = "func_130005_c", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/GL11;glTranslatef(FFF)V", ordinal = 2))
    private void addInversionForZombie(EntityLiving par1EntityLiving, float par2, CallbackInfo ci) {
        if (par1EntityLiving instanceof EntityZombie) {
            RenderBipedFix.fixZombieItem();
        }
    }
}
