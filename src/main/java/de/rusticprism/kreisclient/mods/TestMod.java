package de.rusticprism.kreisclient.mods;

import de.rusticprism.kreisclient.config.Config;
import de.rusticprism.kreisclient.modapi.KreisClientMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class TestMod extends KreisClientMod {
    public TestMod() {
        super("TestMod");
    }

    @Override
    public void init(MinecraftClient client) {
        super.init(client);
    }

    @Override
    public void setEnabled(boolean enabled) {
        Config.set("Blockcounter.txt","Enabled", String.valueOf(enabled));
    }

    @Override
    public boolean isEnabled() {
        return Boolean.parseBoolean(Config.get("Blockcounter.txt","Enabled"));
    }

    @Override
    public void render(MatrixStack matrices, float Xpostion, float Yposition, int color) {
        MinecraftClient.getInstance().textRenderer.drawWithShadow(matrices,new LiteralText("TestMod"),Float.parseFloat(Config.get("TestMod.txt","Xpos")),Float.parseFloat(Config.get("TestMod.txt","Ypos")),color);
    }
}
