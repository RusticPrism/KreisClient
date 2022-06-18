package de.rusticprism.kreisclient.commands;

import de.rusticprism.kreisclient.mods.NarratorOffMod;
import de.rusticprism.kreisclient.utils.KreisClientCommand;
import de.rusticprism.kreisclient.utils.Prefix;

public class NarratorOffCommand extends KreisClientCommand {
    @Override
    public void performCommand(String command, String[] args) {
        if(args.length == 1) {
            if(args[0].equalsIgnoreCase("off")) {
                NarratorOffMod.instance.setEnabled(true);
                info("Narrator ist nun aus!");
            }else if(args[0].equalsIgnoreCase("on")) {
                NarratorOffMod.instance.setEnabled(false);
                info("Narrator ist nun an!");
            }else warning("§cBitte benutze " + Prefix.getCommandPrefix() + "narrator <on/off>!");
        }else warning("§cBitte benutze " + Prefix.getCommandPrefix() + "narrator <on/off>!");
    }
}
