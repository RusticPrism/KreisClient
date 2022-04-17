package de.rusticprism.kreisclient.commands;

import de.rusticprism.kreisclient.mods.BlockCounter;
import de.rusticprism.kreisclient.utils.KreisClientCommand;
import de.rusticprism.kreisclient.utils.Prefix;

public class BlockCounterCommand extends KreisClientCommand {
    @Override
    public void performCommand(String command, String[] args) {
        if(args.length == 0) {
            BlockCounter.Instance.setEnabled(!BlockCounter.Instance.isEnabled());
            if(BlockCounter.Instance.isEnabled()) {
                info("Blockcounter ist nun an!");
            }else info("Blockcounter ist nun aus!");
        }else warning("§cBitte benutze nur " + Prefix.getCommandPrefix() + "blockcounter!");
    }
}
