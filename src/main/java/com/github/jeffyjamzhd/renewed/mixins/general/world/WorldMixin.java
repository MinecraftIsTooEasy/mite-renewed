package com.github.jeffyjamzhd.renewed.mixins.general.world;

import com.github.jeffyjamzhd.renewed.api.IWorld;
import com.github.jeffyjamzhd.renewed.api.IWorldInfo;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import net.minecraft.Block;
import net.minecraft.World;
import net.minecraft.WorldInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public abstract class WorldMixin implements IWorld {
    @Shadow public abstract Block getBlock(int x, int y, int z);

    @Shadow public abstract int getBlockMetadata(int x, int y, int z);

    @Shadow
    public abstract WorldInfo getWorldInfo();

    @Inject(method = "getBlockHardness", at = @At("HEAD"), cancellable = true)
    public void getBlockHardness(int x, int y, int z,
                                 CallbackInfoReturnable<Float> cir) {
        Block block = this.getBlock(x, y, z);
        int metadata = this.getBlockMetadata(x, y, z);
        cir.setReturnValue(block.mr$getBlockHardness((World) (Object) this, x, y, z, metadata));
    }

    @Override
    public Difficulty mr$getDifficulty() {
        return ((IWorldInfo)this.getWorldInfo()).mr$getDifficulty();
    }
}
