package de.rusticprism.kreisclient.screens;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.rusticprism.kreisclient.buttons.ButtonWidget;
import de.rusticprism.kreisclient.modapi.KreisClientMod;
import de.rusticprism.kreisclient.modapi.ModRegistery;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModMenuScreen extends Screen {
    public Screen previous;
    public boolean mousepressed = false;
    public ModMenuScreen(Screen prev) {
        super(Text.literal("ModMenu"));
        previous = prev;
    }

    @Override
    public void init() {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        mousepressed = true;
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        mousepressed = false;
        return true;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return true;
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if(!(mouseX >= width - 30 || mouseY >= height || mouseX <= 0 || mouseY <= 0)) {
            for (KreisClientMod mod : ModRegistery.mods) {
                mod.move(mouseX, mouseY, mousepressed);
            }
        }
        Identifier MODMENU_OVERLAY = new Identifier("kreisclient:backgrounds/modmenu.png");
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, MODMENU_OVERLAY);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1);
        drawTexture(matrices, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);

        super.render(matrices,mouseX,mouseY,delta);
        this.addDrawableChild(new ButtonWidget(this.width - 98,0,98,20,Text.translatable("kreisclient.gui.cancel"), onpress-> MinecraftClient.getInstance().currentScreen.close()));
    }
}
