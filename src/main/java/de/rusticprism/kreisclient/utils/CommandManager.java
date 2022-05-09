package de.rusticprism.kreisclient.utils;

import de.rusticprism.kreisclient.commands.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.LiteralText;

import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {
    public ConcurrentHashMap<String, KreisClientCommand> commands;
    public CommandManager() {
        this.commands = new ConcurrentHashMap<>();
        this.commands.put("fullbright", new FullbrightCommand());
        this.commands.put("prefix",new PrefixCommand());
        this.commands.put("blockcounter",new BlockCounterCommand());
        this.commands.put("narrator",new NarratorOffCommand());
        this.commands.put("server",new ServerCommand());
    }

    public void perform(String command,String[] args) {
        KreisClientCommand cmd;
        if((cmd = this.commands.get(command.toLowerCase().substring(1))) != null) {
            cmd.performCommand(command,args);
        }else MinecraftClient.getInstance().player.sendMessage(new LiteralText(Prefix.getPrefix() + "Kein Command"),false);
    }
}
