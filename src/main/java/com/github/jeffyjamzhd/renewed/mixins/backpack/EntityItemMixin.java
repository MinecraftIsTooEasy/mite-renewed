package com.github.jeffyjamzhd.renewed.mixins.backpack;

import com.jeffyjamzhd.jeffylib.api.impl.IItem;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityItem.class)
public abstract class EntityItemMixin extends Entity {
    @Shadow
    public abstract ItemStack getEntityItem();

    public EntityItemMixin(World par1World) {
        super(par1World);
    }

    @Redirect(method = "onCollideWithPlayer", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/InventoryPlayer;addItemStackToInventory(Lnet/minecraft/ItemStack;)Z"))
    private boolean markInventoryOp(InventoryPlayer instance, ItemStack stack) {
        return instance.mr$addStackFromWorld(stack);
    }

    @Inject(method = "attackEntityFrom", at = @At(
            value = "INVOKE", target = "Lnet/minecraft/EntityItem;setDead()V", ordinal = 1))
    private void addDestroyHook(Damage damage, CallbackInfoReturnable<EntityDamageResult> cir) {
        // Get item and run hook
        ItemStack stack = this.getEntityItem();
        if (stack != null && stack.getItem() instanceof IItem item)
            item.jl$onItemDestroyed(stack, worldObj, posX, posY, posZ);
    }
}
