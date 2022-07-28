package de.rusticprism.kreisclient.utils;

import de.rusticprism.kreisclient.utils.config.FileConfiguration;
import de.rusticprism.kreisclient.utils.config.Configuration;

public class Prefix {
  private static final FileConfiguration config = new Configuration("prefix.txt");
    public static String getCommandPrefix() {
        if(config.getString("Prefix") == null) {
            Prefix.setCommandPrefix("+");
        }
        return config.getString("Prefix");
    }
    public static void setCommandPrefix(String prefix) {
        config.set("Prefix", prefix);
        config.save();
    }

    public static String getPrefix() {
        return "§1KreisClient §8§l>> ";
    }
}
