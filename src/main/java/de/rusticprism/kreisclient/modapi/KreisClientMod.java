package de.rusticprism.kreisclient.modapi;

import de.rusticprism.kreisclient.config.Config;
import de.rusticprism.kreisclient.utils.config.FileConfiguration;
import de.rusticprism.kreisclient.utils.config.YamlConfiguration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;

public abstract class KreisClientMod extends Mod {
    private boolean canmove = false;
    public String modname;
    public MinecraftClient client;
    public TextRenderer textRenderer;
    public GameRenderer gameRenderer;

    private static FileConfiguration config;
   public void render(MatrixStack matrices,float Xpostion, float Yposition,int color) {

   }
   public KreisClientMod(String modname) {
       this.modname = modname;
       config = new YamlConfiguration(modname + ".txt");
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

    public String getModname() {
        return modname;
    }
    public void move(float mouseX,float mouseY,boolean mousepressed) {
        if(mousepressed) {

                if(isEnabled()) {
                    if(mouseX >= config.getDouble("Xpos") && mouseY >= config.getDouble("Ypos") && mouseY<= config.getDouble("Ypos") + 5&& mouseX<= config.getDouble("Xpos") + 30) {
                        canmove = true;
                    }
                }
                if(canmove) {
                    config.set("Xpos", mouseX);
                    config.set("Ypos", mouseY);
                }
        }else canmove = false;
    }
}
