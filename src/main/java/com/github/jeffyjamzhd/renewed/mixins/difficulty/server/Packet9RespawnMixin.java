package com.github.jeffyjamzhd.renewed.mixins.difficulty.server;

import com.github.jeffyjamzhd.renewed.api.IPacket9Respawn;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.util.NBTSizeCalculator;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.NBTTagCompound;
import net.minecraft.Packet9Respawn;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@Mixin(Packet9Respawn.class)
public class Packet9RespawnMixin implements IPacket9Respawn {
    @Unique
    NBTTagCompound difficulty;
    @Unique
    boolean locked;

    @Inject(method = "readPacketData", at = @At("TAIL"))
    private void readDifficultyData(DataInput input, CallbackInfo ci) {
        try {
            this.locked = input.readBoolean();
            this.difficulty = (NBTTagCompound) NBTTagCompound.readNamedTag(input);
        } catch (IOException e) {
        }
    }

    @Inject(method = "writePacketData", at = @At("TAIL"))
    private void writeDifficultyData(DataOutput output, CallbackInfo ci) {
        try {
            output.writeBoolean(this.locked);
            NBTTagCompound.writeNamedTag(this.difficulty, output);
        } catch (IOException e) {
        }
    }

    @ModifyReturnValue(method = "getPacketSize", at = @At("RETURN"))
    private int adjustPacketSize(int original) {
        return original + (int) NBTSizeCalculator.getCompoundByteSize(this.difficulty) + 1;
    }

    @Override
    public void mr$setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty.asTagCompound();
    }

    @Override
    public Difficulty mr$getDifficulty() {
        return Difficulty.createFromTagCompound(this.difficulty);
    }

    @Override
    public void mr$setDifficultyLock(boolean locked) {
        this.locked = locked;
    }

    @Override
    public boolean mr$getDifficultyLock() {
        return this.locked;
    }
}
