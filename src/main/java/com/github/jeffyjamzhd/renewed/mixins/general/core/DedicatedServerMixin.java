package com.github.jeffyjamzhd.renewed.mixins.general.core;

import com.github.jeffyjamzhd.renewed.api.IDedicatedServer;
import com.github.jeffyjamzhd.renewed.api.IWorldInfo;
import com.github.jeffyjamzhd.renewed.api.difficulty.Difficulty;
import com.github.jeffyjamzhd.renewed.api.server.RenewedYAML;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.DedicatedServer;
import net.minecraft.ILogAgent;
import net.minecraft.WorldServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;

@Mixin(DedicatedServer.class)
@Environment(EnvType.SERVER)
public abstract class DedicatedServerMixin extends MinecraftServer implements IDedicatedServer {
    public DedicatedServerMixin(File par1File) {
        super(par1File);
    }

    @Shadow
    public abstract ILogAgent getLogAgent();

    @Unique
    private RenewedYAML renewedYAML;

    @Inject(method = "startServer", at = @At(value = "INVOKE", target = "Lnet/minecraft/DedicatedServer;loadAllWorlds(Ljava/lang/String;Ljava/lang/String;JLnet/minecraft/WorldType;Ljava/lang/String;)V", shift = At.Shift.AFTER))
    private void createRenewedYAML(CallbackInfoReturnable<Boolean> cir) {
        this.renewedYAML = new RenewedYAML(new File("renewed.yaml"), this.getLogAgent());
        Difficulty difficulty = this.renewedYAML.loadFromYaml();
        assignDifficultyToWorlds(difficulty);
    }

    @Unique
    private void assignDifficultyToWorlds(Difficulty difficulty) {
        for (WorldServer server : this.worldServers) {
            if (server == null) continue;
            IWorldInfo info = (IWorldInfo) server.getWorldInfo();
            info.mr$setDifficulty(difficulty);
        }
    }

    @Override
    public void mr$writeToYaml() {
        IWorldInfo info = (IWorldInfo) this.getOverworld().getWorldInfo();
        this.renewedYAML.saveToYaml(info);
    }
}
