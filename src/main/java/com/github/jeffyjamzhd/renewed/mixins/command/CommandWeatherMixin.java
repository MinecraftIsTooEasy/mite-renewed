package com.github.jeffyjamzhd.renewed.mixins.command;

import net.minecraft.*;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Mixin(CommandWeather.class)
public abstract class CommandWeatherMixin extends CommandBase {
    /**
     * @author jeffyjamzhd
     * @reason This command does nothing in MiTE.
     */
    @Overwrite
    public void processCommand(ICommandSender sender, String[] args) throws CommandException {
        if (args.length >= 1 && args.length <= 2) {
            int i = (300 + new Random().nextInt(600)) * 20;
            if (args.length >= 2) {
                i = CommandBase.parseIntBounded(sender, args[1], 1, 1000000) * 20;
            }
            WorldServer world = MinecraftServer.getServer().worldServers[0];
            if ("clear".equalsIgnoreCase(args[0])) {
                world.setRainStrength(0);
                CommandBase.notifyAdmins(sender, "commands.weather.clear", new Object[0]);
            } else if ("rain".equalsIgnoreCase(args[0])) {
                world.setRainStrength(1);
                CommandBase.notifyAdmins(sender, "commands.weather.rain", new Object[0]);
            } else {
                if (!"thunder".equalsIgnoreCase(args[0])) {
                    throw new WrongUsageException("commands.weather.usage", new Object[0]);
                }
            }
        } else {
            throw new WrongUsageException("commands.weather.usage", new Object[0]);
        }
    }
}
