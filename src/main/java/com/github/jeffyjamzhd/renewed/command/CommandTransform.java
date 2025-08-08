package com.github.jeffyjamzhd.renewed.command;

import net.minecraft.*;


public class CommandTransform extends CommandBase {
    @Override
    public String getCommandName() {
        return "transform";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "commands.transform.usage";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] strings) {
        ServerPlayer player = getCommandSenderAsPlayer(sender);

        if (strings.length < 2)
            throw new WrongUsageException("No angles supplied / Not enough args");

        player.mountEntity(null);
        player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, Float.parseFloat(strings[0]), Float.parseFloat(strings[1]));
    }
}
