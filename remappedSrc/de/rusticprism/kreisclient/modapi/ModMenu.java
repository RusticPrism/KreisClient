package de.rusticprism.kreisclient.modapi;

import de.rusticprism.kreisclient.screens.ModMenuScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;

public class ModMenu {
    public static void call(KeyBinding binding, Screen prev) {
        if(binding.isPressed()) {
            MinecraftClient.getInstance().setScreen(new ModMenuScreen(prev));
        }
    }
}
