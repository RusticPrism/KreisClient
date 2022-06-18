package de.rusticprism.kreisclient.mods;

import de.rusticprism.kreisclient.modapi.KreisClientMod;
import de.rusticprism.kreisclient.utils.config.FileConfiguration;
import de.rusticprism.kreisclient.utils.config.YamlConfiguration;

public class NarratorOffMod extends KreisClientMod{
    public static NarratorOffMod instance;
    private static FileConfiguration config;
    public NarratorOffMod() {
        super("Narratoroff");
        instance = this;
        config = new YamlConfiguration("Narratoroff.txt");
    }

    //@Override
    public void setEnabled(boolean enabled) {
        config.set("Enabled", String.valueOf(enabled));
    }

    //@Override
    public boolean isEnabled() {
       return config.getBoolean("Enabled");
    }
}
