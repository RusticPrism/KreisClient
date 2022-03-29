package de.rusticprism.kreisclient.modapi;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;

public abstract class KreisClientMod extends Mod {
    public String modname;
    public MinecraftClient client;
    public TextRenderer textRenderer;
    public GameRenderer gameRenderer;
   public void render(MatrixStack matrices,float Xpostion, float Yposition,int color) {

   }
   public KreisClientMod(String modname) {
       this.modname = modname;
   }

    @Override
    public boolean isEnabled() {
        return super.isEnabled();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
    }

    public void init(MinecraftClient client) {
        this.client = client;
        this.textRenderer = client.textRenderer;
        this.gameRenderer = client.gameRenderer;
    }
}
