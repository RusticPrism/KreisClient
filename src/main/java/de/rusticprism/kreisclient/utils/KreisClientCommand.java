package de.rusticprism.kreisclient.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

public abstract class KreisClientCommand {

    public abstract void performCommand(String command, String[] args);

    public static void info(String text) {
        MinecraftClient.getInstance().player.sendMessage(new LiteralText(Prefix.getPrefix() +"§8" + text),false);
    }
    public static void warning(String text) {
        MinecraftClient.getInstance().player.sendMessage(new LiteralText(Prefix.getPrefix() + "§c" + text),false);
    }
}
