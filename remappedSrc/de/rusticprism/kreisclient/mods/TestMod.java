package de.rusticprism.kreisclient.mods;

import de.rusticprism.kreisclient.modapi.KreisClientMod;
import de.rusticprism.kreisclient.utils.config.FileConfiguration;
import de.rusticprism.kreisclient.utils.config.YamlConfiguration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class TestMod extends KreisClientMod {
    private static FileConfiguration config;
    public TestMod() {
        super("TestMod");
        config = new YamlConfiguration("TestMod.txt");
    }

    @Override
    public void init(MinecraftClient client) {
        super.init(client);
    }

    @Override
    public void setEnabled(boolean enabled) {
        config.set("Enabled", enabled);
    }

    @Override
    public boolean isEnabled() {
        return config.getBoolean("Enabled");
    }

    @Override
    public void render(MatrixStack matrices, float Xpostion, float Yposition, int color) {
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices, Text.literal("TestMod"),config.getFloat("Xpos"),config.getFloat("Ypos"),color);
    }
}
