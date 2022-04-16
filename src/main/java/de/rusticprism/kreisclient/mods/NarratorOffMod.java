package de.rusticprism.kreisclient.mods;

import de.rusticprism.kreisclient.modapi.KreisClientMod;

public class NarratorOffMod extends KreisClientMod {
    public static NarratorOffMod instance;
    public NarratorOffMod() {
        super("narratoroff");
        instance = this;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }
}
