package com.github.jeffyjamzhd.renewed.mixins.backpack;

import com.github.jeffyjamzhd.renewed.network.S2CAnimateSlot;
import moddedmite.rustedironcore.network.Network;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends EntityPlayer {
    public ServerPlayerMixin(World par1World, String par2Str) {
        super(par1World, par2Str);
    }

    @Inject(method = "sendSlotContents", at = @At(value = "INVOKE", target = "Lnet/minecraft/NetServerHandler;sendPacketToPlayer(Lnet/minecraft/Packet;)V", shift = At.Shift.AFTER))
    private void sendSlotAnimation(Container container, int slotID, ItemStack stack, CallbackInfo ci) {
        byte adjustedID = (byte) (slotID - 36);
        if (adjustedID < 0 || adjustedID >= 9) return;

        boolean shouldAnimate = this.inventory.mr$slotNeedsToAnimate(adjustedID);
        if (shouldAnimate) {
            Network.sendToClient((ServerPlayer) (Object) this, new S2CAnimateSlot((ServerPlayer) (Object) this, adjustedID));
        }
    }
}
