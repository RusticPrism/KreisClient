package de.rusticprism.kreisclient.mods;

import de.rusticprism.kreisclient.config.Config;
import de.rusticprism.kreisclient.modapi.KreisClientMod;

public class NarratorOffMod extends KreisClientMod{
    public static NarratorOffMod instance;
    public NarratorOffMod() {
        super("narratoroff");
        instance = this;
    }

    //@Override
    public void setEnabled(boolean enabled) {
        Config.set("Narrator.txt","Enabled", String.valueOf(enabled));
    }

    //@Override
    public boolean isEnabled() {
       return Boolean.parseBoolean(Config.get("Narrator.txt","Enabled"));
    }
}
