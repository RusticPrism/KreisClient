package de.rusticprism.kreisclient.modapi;

import net.minecraft.client.MinecraftClient;

public class Mod {
   private boolean isEnabled = false;

   protected final MinecraftClient mc;

   public Mod() {
       this.mc = MinecraftClient.getInstance();
   }

    public void setEnabled(boolean enabled) {
        isEnabled = enabled;
    }

    public boolean isEnabled() {
        return isEnabled;
    }
}
