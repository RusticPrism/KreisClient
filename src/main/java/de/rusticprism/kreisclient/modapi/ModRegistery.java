package de.rusticprism.kreisclient.modapi;

import de.rusticprism.kreisclient.KreisClient;
import de.rusticprism.kreisclient.mods.BlockCounter;

import java.util.concurrent.ConcurrentHashMap;

public class ModRegistery {
    public static ConcurrentHashMap<String, KreisClientMod> mods;

    public ModRegistery() {
        mods = new ConcurrentHashMap<>();
        register("blockcounter",new BlockCounter(1,1));
    }
    public static void register(String modname, KreisClientMod mod) {
        mods.put(modname,mod);
    }
}
