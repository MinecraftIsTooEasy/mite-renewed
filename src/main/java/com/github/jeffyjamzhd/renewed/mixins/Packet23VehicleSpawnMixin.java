package com.github.jeffyjamzhd.renewed.mixins;

import com.github.jeffyjamzhd.renewed.entity.EntityPolearm;
import com.github.jeffyjamzhd.renewed.registry.RenewedTracker;
import net.minecraft.Entity;
import net.minecraft.Packet23VehicleSpawn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@Mixin(Packet23VehicleSpawn.class)
public class Packet23VehicleSpawnMixin {
    @Shadow public int arrow_item_id;
    @Shadow public int arrow_tile_x;
    @Shadow public int arrow_tile_y;
    @Shadow public int arrow_tile_z;
    @Shadow public double exact_pos_x;
    @Shadow public double exact_pos_y;
    @Shadow public double exact_pos_z;
    @Shadow public double exact_motion_x;
    @Shadow public double exact_motion_y;
    @Shadow public double exact_motion_z;

    @Shadow public int type;

    @Inject(method = "<init>(Lnet/minecraft/Entity;II)V", at = @At("TAIL"))
    public void initExtendPolearm(Entity entity, int type, int thrower_entity_id, CallbackInfo ci) {
        if (entity instanceof EntityPolearm polearm) {
            this.arrow_tile_x = polearm.tX;
            this.arrow_tile_y = polearm.tY;
            this.arrow_tile_z = polearm.tZ;
            this.exact_pos_x = polearm.posX;
            this.exact_pos_y = polearm.posY;
            this.exact_pos_z = polearm.posZ;
            this.exact_motion_x = polearm.motionX;
            this.exact_motion_y = polearm.motionY;
            this.exact_motion_z = polearm.motionZ;
            this.arrow_item_id = polearm.getItem().itemID;
        }
    }

    @Inject(method = "writePacketData", at = @At("TAIL"))
    public void writePacketPolearm(DataOutput out, CallbackInfo ci) throws IOException {
        if (this.type == RenewedTracker.POLEARM_TYPE) {
            out.writeShort(this.arrow_item_id);
            out.writeInt(this.arrow_tile_x);
            out.writeInt(this.arrow_tile_y);
            out.writeInt(this.arrow_tile_z);
            out.writeDouble(this.exact_pos_x);
            out.writeDouble(this.exact_pos_y);
            out.writeDouble(this.exact_pos_z);
            out.writeDouble(this.exact_motion_x);
            out.writeDouble(this.exact_motion_y);
            out.writeDouble(this.exact_motion_z);
        }
    }

    @Inject(method = "readPacketData", at = @At("TAIL"))
    public void readPacketPolearm(DataInput inp, CallbackInfo ci) throws IOException {
        if (this.type == RenewedTracker.POLEARM_TYPE) {
            this.arrow_item_id = inp.readShort();
            this.arrow_tile_x = inp.readInt();
            this.arrow_tile_y = inp.readInt();
            this.arrow_tile_z = inp.readInt();
            this.exact_pos_x = inp.readDouble();
            this.exact_pos_y = inp.readDouble();
            this.exact_pos_z = inp.readDouble();
            this.exact_motion_x = inp.readDouble();
            this.exact_motion_y = inp.readDouble();
            this.exact_motion_z = inp.readDouble();
        }
    }
    
}
