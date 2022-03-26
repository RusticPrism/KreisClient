package de.rusticprism.kreisclient.commands;

import de.rusticprism.kreisclient.utils.KreisClientCommand;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

public class FullbrightCommand implements KreisClientCommand {
    private boolean enabled = false;
    @Override
    public void performCommand(String command, String[] args) {
        if(args.length == 0) {
            if(!enabled || MinecraftClient.getInstance().options.gamma <= 1.0) {
                enabled = true;
                MinecraftClient.getInstance().options.gamma = 50.0;
            }else {
                MinecraftClient.getInstance().options.gamma = 1.0;
                enabled = false;
            }
        }else MinecraftClient.getInstance().inGameHud.getChatHud().addMessage(new LiteralText("§cBitte benutze nur +fullbright!"));
    }
}
