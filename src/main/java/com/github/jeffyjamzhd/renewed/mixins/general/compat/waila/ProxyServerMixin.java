package com.github.jeffyjamzhd.renewed.mixins.general.compat.waila;

import com.github.jeffyjamzhd.renewed.compat.waila.RenewedWailaPlugin;
import mcp.mobius.waila.api.impl.ModuleRegistrar;
import mcp.mobius.waila.server.ProxyServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ProxyServer.class)
public class ProxyServerMixin {
    @Inject(method = "registerMods", at = @At("TAIL"))
    private void registerRenewedWailaPlugin(CallbackInfo ci) {
        // Order matters with accessing vanilla plugin strings, so we
        // must ensure Renewed's plugin is in front of the stock Waila plugins
        RenewedWailaPlugin.register(ModuleRegistrar.instance());
    }
}
