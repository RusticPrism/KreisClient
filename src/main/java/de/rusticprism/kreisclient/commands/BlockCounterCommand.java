package de.rusticprism.kreisclient.commands;

import de.rusticprism.kreisclient.KreisClient;
import de.rusticprism.kreisclient.mods.BlockCounter;
import de.rusticprism.kreisclient.utils.KreisClientCommand;
import de.rusticprism.kreisclient.utils.Prefix;
import net.minecraft.text.LiteralText;

public class BlockCounterCommand implements KreisClientCommand {
    @Override
    public void performCommand(String command, String[] args) {
        if(args.length == 0) {
            KreisClient.MC.player.sendMessage(new LiteralText("§8Die Anzahl ist §1" + BlockCounter.getamount() + "!"),false   );
        }else KreisClient.MC.player.sendMessage(new LiteralText("§cBitte benutze nur " + Prefix.getPrefix() + "blockcounter!"),false);
    }
}
