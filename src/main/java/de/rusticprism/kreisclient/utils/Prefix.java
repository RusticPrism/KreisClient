package de.rusticprism.kreisclient.utils;

import de.rusticprism.kreisclient.config.Config;

import java.io.File;

public class Prefix {
    public static String getPrefix() {
        if(Config.get(new File("./config/prefix.yml")) == null) {
            Prefix.setPrefix("+");
        }
        return String.valueOf(Config.get(new File("./config/prefix.yml")));
    }
    public static void setPrefix(String prefix) {
        Config.set(new File("./config/prefix.yml"),prefix);
    }
}
