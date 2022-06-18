package de.rusticprism.kreisclient.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public abstract class KreisClientCommand {

    public abstract void performCommand(String command, String[] args);

    public static void info(String text) {
        MinecraftClient.getInstance().player.sendMessage(Text.literal(Prefix.getPrefix() +"§8" + text),false);
    }
    public static void warning(String text) {
        MinecraftClient.getInstance().player.sendMessage(Text.literal(Prefix.getPrefix() + "§c" + text),false);
    }
    public static void info(Text text) {
        MinecraftClient.getInstance().player.sendMessage(text,false);
    }
}
