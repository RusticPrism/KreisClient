package de.rusticprism.kreisclient.utils;

import de.rusticprism.kreisclient.config.Config;

public class Prefix {
    public static String getCommandPrefix() {
        if(Config.get("prefix.txt","Prefix") == null) {
            Prefix.setCommandPrefix("+");
        }
        return String.valueOf(Config.get("prefix.txt","Prefix"));
    }
    public static void setCommandPrefix(String prefix) {
        Config.set("prefix.txt","Prefix",prefix);
    }

    public static String getPrefix() {
        return "§1KreisClient §8§l>> ";
    }
}
