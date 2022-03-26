package de.rusticprism.kreisclient.utils;

import de.rusticprism.kreisclient.KreisClient;
import de.rusticprism.kreisclient.commands.FullbrightCommand;
import de.rusticprism.kreisclient.commands.PrefixCommand;

import java.util.concurrent.ConcurrentHashMap;

public class CommandManager {
    public ConcurrentHashMap<String, KreisClientCommand> commands;
    public CommandManager() {
        this.commands = new ConcurrentHashMap<>();
        this.commands.put("fullbright", new FullbrightCommand());
        this.commands.put("prefix",new PrefixCommand());
    }

    public void perform(String command,String[] args) {
        KreisClientCommand cmd;
        if((cmd = this.commands.get(command.toLowerCase().substring(1))) != null) {
            cmd.performCommand(command,args);
        }
    }
}
