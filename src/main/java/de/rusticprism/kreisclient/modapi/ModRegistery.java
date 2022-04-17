package de.rusticprism.kreisclient.modapi;

import de.rusticprism.kreisclient.mods.BlockCounter;
import de.rusticprism.kreisclient.mods.NarratorOffMod;
import de.rusticprism.kreisclient.mods.TestMod;

import java.util.ArrayList;
import java.util.List;

public class ModRegistery {
    public static List<KreisClientMod> mods;

    public ModRegistery() {
        mods = new ArrayList<>();
        register(new BlockCounter());
        register(new TestMod());
       // register(new NarratorOffMod());
    }
    public static void register(KreisClientMod mod) {
        mods.add(mod);
    }
}
