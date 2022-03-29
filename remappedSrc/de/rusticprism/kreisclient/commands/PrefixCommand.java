package de.rusticprism.kreisclient.commands;

import de.rusticprism.kreisclient.KreisClient;
import de.rusticprism.kreisclient.utils.KreisClientCommand;
import de.rusticprism.kreisclient.utils.Prefix;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

public class PrefixCommand implements KreisClientCommand {
    @Override
    public void performCommand(String command, String[] args) {
        if(args.length == 1) {
            KreisClient.LOGGER.info(Prefix.getPrefix());
            Prefix.setPrefix(args[0]);
            MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText("§8Der Prefix ist nun §1" + args[0] + " §8!"));
        }else MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText("§cBitte benutze "+Prefix.getPrefix() +"prefix <prefix>!"));
    }
}
