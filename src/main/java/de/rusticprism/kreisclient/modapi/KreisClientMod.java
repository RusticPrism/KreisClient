package de.rusticprism.kreisclient.modapi;

import de.rusticprism.kreisclient.config.Config;
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

    public String getModname() {
        return modname;
    }
    public void move(float mouseX,float mouseY,boolean mousepressed) {
        if(mousepressed) {

                if(isEnabled()) {
                    if(mouseX >= Double.parseDouble(Config.get(getModname() + ".txt","Xpos")) && mouseY >= Double.valueOf(Config.get(getModname() + ".txt","Ypos")) && mouseY<= Double.valueOf(Config.get(getModname() + ".txt","Ypos")) + 5&& mouseX<= Double.parseDouble(Config.get(getModname() + ".txt","Xpos")) + 30) {
                        canmove = true;
                    }
                }
                if(canmove) {
                    Config.set(getModname() + ".txt","Xpos",String.valueOf(mouseX));
                    Config.set(getModname() + ".txt","Ypos",String.valueOf(mouseY));
                }
        }else canmove = false;
    }
}
