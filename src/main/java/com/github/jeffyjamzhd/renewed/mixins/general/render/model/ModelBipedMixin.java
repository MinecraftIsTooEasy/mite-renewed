package com.github.jeffyjamzhd.renewed.mixins.general.render.model;

import com.github.jeffyjamzhd.renewed.api.IModelBiped;
import com.github.jeffyjamzhd.renewed.render.ItemRenderPolearm;
import net.minecraft.*;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Will probably implement held item renderers in the future.
 */

@Mixin(ModelBiped.class)
public class ModelBipedMixin extends ModelBase implements IModelBiped {
    @Shadow public boolean aimedBow;
    @Shadow public ModelRenderer bipedRightArm;
    @Shadow public ModelRenderer bipedLeftArm;
    @Shadow public ModelRenderer bipedRightLeg;
    @Shadow public ModelRenderer bipedLeftLeg;
    @Shadow public boolean isSneak;
    @Shadow public ModelRenderer bipedHead;
    @Shadow public ModelRenderer bipedHeadwear;
    @Shadow public ModelRenderer bipedBody;

    @Shadow
    public int heldItemLeft;
    @Shadow
    public int heldItemRight;
    @Unique private boolean mr$aimedPolearm;
    @Unique private float headSpringOffset = 0F;
    @Unique private float headSpringVelocity = 0F;
    @Unique private float headSpringOffsetForward = 0.0F;
    @Unique private float headSpringVelocityForward = 0.0F;
    @Unique private float headSpringOffsetStrafe = 0.0F;
    @Unique private float headSpringVelocityStrafe = 0.0F;

    @Override
    public boolean mr$isAimingPolearm() {
        return this.mr$aimedPolearm;
    }

    @Override
    public void mr$setAimingPolearm(boolean value) {
        mr$aimedPolearm = value;
    }

    @Inject(method = "setRotationAngles", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/MathHelper;sin(F)F", ordinal = 7), cancellable = true)
    private void specialUseAnimation(
            float par1, float par2, float par3,
            float par4, float par5, float par6,
            Entity entity, CallbackInfo ci
    ) {
        if (this.mr$aimedPolearm) {
            float delta = Minecraft.getMinecraft().timer.elapsedPartialTicks;
            ItemRenderPolearm.handlePlayerArms(entity, bipedLeftArm, bipedRightArm, par3, delta);
            ci.cancel();
        }
    }

    @Inject(method = "setRotationAngles", at = @At(value = "FIELD", target = "Lnet/minecraft/ModelBiped;isSneak:Z", ordinal = 0, opcode = Opcodes.GETFIELD))
    private void setArmAnimationForJeffy(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity, CallbackInfo ci) {
        if (entity instanceof EntityPlayer player) {
            String username = player.getCommandSenderName();
            if (!username.equals("jeffyjamzhd")) {
                return;
            }
        } else {
            return;
        }

        boolean isSprinting = entity.isSprinting();
        float swingMultiplier = this.isSneak ? 0.7F : (isSprinting ? 1.5F : 1.2F);
        float legSwingMultiplier = this.isSneak ? 1.0F : (isSprinting ? 2.0F : 1.5F);
        float flailMultiplier = this.isSneak ? 0.3F : (isSprinting ? 1F : 0.6F);

        this.bipedRightArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * swingMultiplier * par2;
        this.bipedLeftArm.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * swingMultiplier * par2;
        this.bipedRightLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F) * legSwingMultiplier * par2;
        this.bipedLeftLeg.rotateAngleX = MathHelper.cos(par1 * 0.6662F + (float)Math.PI) * legSwingMultiplier * par2;
        this.bipedRightArm.rotateAngleZ = ((MathHelper.cos(par1 * 0.2812F) + 1.0F) * par2) * flailMultiplier;
        this.bipedLeftArm.rotateAngleZ = (-(MathHelper.cos(par1 * 0.2212F) + 1.0F) * par2) * flailMultiplier;

        if (this.onGround > -9999F) {
            float swingProgress = this.onGround;
            this.bipedBody.rotateAngleY = MathHelper.sin(MathHelper.sqrt_float(swingProgress) * (float)Math.PI * 2.0F) * 0.2F;

            // Adjust arm pivots to follow the twisting torso
            this.bipedRightArm.rotationPointZ = MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedRightArm.rotationPointX = -MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedLeftArm.rotationPointZ = -MathHelper.sin(this.bipedBody.rotateAngleY) * 5.0F;
            this.bipedLeftArm.rotationPointX = MathHelper.cos(this.bipedBody.rotateAngleY) * 5.0F;

            this.bipedRightArm.rotateAngleY += this.bipedBody.rotateAngleY;
            this.bipedLeftArm.rotateAngleY += this.bipedBody.rotateAngleY;

            // Calculate the arm raise and strike
            float var8 = 1.0F - this.onGround;
            var8 *= var8;
            var8 *= var8;
            var8 = 1.0F - var8;
            float punchRaise = MathHelper.sin(var8 * (float)Math.PI);
            float punchStrike = MathHelper.sin(this.onGround * (float)Math.PI) * -(this.bipedHead.rotateAngleX - 0.7F) * 0.75F;

            // Subtract the punch offsets from the custom right arm X angle
            this.bipedRightArm.rotateAngleX = (float)((double)this.bipedRightArm.rotateAngleX - ((double)punchRaise * 1.2 + (double)punchStrike));
            this.bipedRightArm.rotateAngleY = this.bipedBody.rotateAngleY * 2.0F;
            this.bipedRightArm.rotateAngleZ += MathHelper.sin(this.onGround * (float)Math.PI) * -0.4F;
        }
    }

    @Inject(method = "setRotationAngles", at = @At(value = "TAIL"))
    private void adjustModelForJeffy(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity, CallbackInfo ci) {
        if (entity instanceof EntityPlayer player) {
            String username = player.getCommandSenderName();
            if (!username.equals("jeffyjamzhd")) {
                return;
            }
        } else {
            return;
        }

        boolean isSprinting = entity.isSprinting();
        float bounceMultiplier = this.isSneak ? 1.0F : (isSprinting ? 4.0F : 2F);
        float legExtension = Math.abs(MathHelper.cos(par1 * 0.6662F));
        float bounce = -legExtension * bounceMultiplier * par2;

        if (this.isSneak) {
            this.bipedBody.rotateAngleX = 0.5F;
            this.bipedRightArm.rotateAngleX += 0.4F;
            this.bipedLeftArm.rotateAngleX += 0.4F;
            this.bipedRightLeg.rotationPointZ = 4.0F;
            this.bipedLeftLeg.rotationPointZ = 4.0F;

            this.bipedHead.rotationPointY = 1.0F + bounce;
            this.bipedHeadwear.rotationPointY = 1.0F + bounce;
            this.bipedBody.rotationPointY = bounce; // Base is 0.0F
            this.bipedRightArm.rotationPointY = 2.0F + bounce;
            this.bipedLeftArm.rotationPointY = 2.0F + bounce;
            this.bipedRightLeg.rotationPointY = 9.0F + bounce;
            this.bipedLeftLeg.rotationPointY = 9.0F + bounce;
        } else {
            this.bipedBody.rotateAngleX = 0.0F;
            this.bipedRightLeg.rotationPointZ = 0.1F;
            this.bipedLeftLeg.rotationPointZ = 0.1F;

            this.bipedHead.rotationPointY = bounce;
            this.bipedHeadwear.rotationPointY = bounce;
            this.bipedBody.rotationPointY = bounce;
            this.bipedRightArm.rotationPointY = 2.0F + bounce;
            this.bipedLeftArm.rotationPointY = 2.0F + bounce;
            this.bipedRightLeg.rotationPointY = 12.0F + bounce;
            this.bipedLeftLeg.rotationPointY = 12.0F + bounce;
        }
    }
}
