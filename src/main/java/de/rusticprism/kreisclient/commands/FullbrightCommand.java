package de.rusticprism.kreisclient.commands;

import de.rusticprism.kreisclient.utils.KreisClientCommand;
import de.rusticprism.kreisclient.utils.Prefix;
import net.minecraft.client.MinecraftClient;

public class FullbrightCommand extends KreisClientCommand {
    private boolean enabled = false;
    @Override
    public void performCommand(String command, String[] args) {
        if(args.length == 0) {
            if(!enabled || MinecraftClient.getInstance().options.getGamma().getValue() <= 1.0) {
                enabled = true;
               MinecraftClient.getInstance().options.getGamma().setValue(500.0);
                info("Fullbright ist nun an!");
            }else {
                MinecraftClient.getInstance().options.getGamma().setValue(1.0);
                enabled = false;
                info("Fullbright ist nun aus!");
            }
        }else warning("§cBitte benutze nur "+ Prefix.getCommandPrefix() + "fullbright!");
    }
}
