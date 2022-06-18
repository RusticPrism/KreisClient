package de.rusticprism.kreisclient.commands;

import de.rusticprism.kreisclient.KreisClient;
import de.rusticprism.kreisclient.utils.KreisClientCommand;
import de.rusticprism.kreisclient.utils.Prefix;

public class PrefixCommand extends KreisClientCommand {
    @Override
    public void performCommand(String command, String[] args) {
        if(args.length == 1) {
            KreisClient.LOGGER.info(Prefix.getCommandPrefix());
            Prefix.setCommandPrefix(args[0]);
            info("§8Der Prefix ist nun §1" + args[0] + " §8!");
        }else warning("§cBitte benutze "+Prefix.getCommandPrefix() +"prefix <prefix>!");
    }
}
