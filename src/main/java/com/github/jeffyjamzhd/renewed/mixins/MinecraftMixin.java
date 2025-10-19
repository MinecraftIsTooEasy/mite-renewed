package com.github.jeffyjamzhd.renewed.mixins;

import com.github.jeffyjamzhd.renewed.MiTERenewed;
import com.github.jeffyjamzhd.renewed.render.gui.GuiMusic;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(value = Minecraft.class, priority = 1)
public abstract class MinecraftMixin {
    @Shadow public SoundManager sndManager;
    @Shadow public static Minecraft theMinecraft;
    @Shadow public EntityClientPlayerMP thePlayer;

    @ModifyReturnValue(method = "getVersionDescriptor", at = @At("RETURN"))
    private static String modifyVersion(String original) {
        return MiTERenewed.getVersionString();
    }

    @ModifyArg(method = "startGame", at = @At(value = "INVOKE", target = "Lorg/lwjgl/opengl/Display;setTitle(Ljava/lang/String;)V"), index = 0)
    private String setTitle(String newTitle) {
        return Minecraft.getVersionDescriptor(true);
    }

    @ModifyArg(method = "clickMouse", at = @At(value = "INVOKE", target = "Lnet/minecraft/RightClickFilter;setExclusive(I)Lnet/minecraft/RightClickFilter;"))
    public int modifyClickMouseArg(int allowed_action) {
        ItemStack stack = thePlayer.getHeldItemStack();
        return stack != null && stack.getItem().mr$isAutoUse(stack) ? 8 : allowed_action;
    }

    @Inject(method = "loadWorld(Lnet/minecraft/WorldClient;Ljava/lang/String;)V", at = @At(value = "HEAD"))
    private void stopMusicOnLoad(WorldClient world, String str, CallbackInfo ci) {
        Random rand = new Random();
        this.sndManager.mr$stopMusic();
        this.sndManager.mr$setTicksToPlay(1000 + rand.nextInt(4000));
    }

    @Inject(method = "startGame", at = @At("TAIL"))
    private void createMusicDisplay(CallbackInfo ci) {
        this.sndManager.mr$getMusicEngine().provideGuiReference(new GuiMusic(theMinecraft));
    }

    @Inject(method = "runGameLoop", at = @At(value = "INVOKE", target = "Lnet/minecraft/GuiAchievement;updateAchievementWindow()V"))
    private void updateMusicDisplay(CallbackInfo ci) {
        this.sndManager.mr$getMusicEngine().gui.updateDisplay();
    }


}
