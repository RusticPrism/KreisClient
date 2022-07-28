package de.rusticprism.kreisclient.accountmanager.gui;

import de.rusticprism.kreisclient.accountmanager.account.MicrosoftAccount;
import de.rusticprism.kreisclient.buttons.ButtonWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

import java.awt.*;
import java.lang.management.MonitorInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountScreen extends Screen {
    private TextRenderer renderer;
    private Screen prev;
    private Text title;
    private List<MicrosoftAccount> list;
    public AccountScreen(Screen prev) {
        super(Text.of("AccountScreen"));
        title = getTitle();
        this.prev = prev;
    }

    @Override
    protected void init() {
        renderer = MinecraftClient.getInstance().textRenderer;
        list = new ArrayList<>();
        list.add(new MicrosoftAccount("RusticPrism", "1234244", "131", UUID.randomUUID()));
        list.add(new MicrosoftAccount("RusticPrism1", "1234244", "131", UUID.randomUUID()));
        list.add(new MicrosoftAccount("RusticPrism2", "1234244", "131", UUID.randomUUID()));
        list.add(new MicrosoftAccount("RusticPrism3", "1234244", "131", UUID.randomUUID()));
        list.add(new MicrosoftAccount("RusticPrism4", "1234244", "131", UUID.randomUUID()));
        list.add(new MicrosoftAccount("RusticPrism5", "1234244", "131", UUID.randomUUID()));
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        this.renderBackground(matrices);
        renderer.draw(matrices,title,this.width / 2F - textRenderer.getWidth(title) / 2F, 10, Color.white.getRGB());
        for(int i = 0; i < list.size(); i++) {
            renderer.draw(matrices,list.get(i).alias(),this.width / 2F - textRenderer.getWidth(list.get(i).alias()) / 2F, i*20 + 50,Color.white.getRGB());
        }
        super.render(matrices,mouseX,mouseY,delta);
    }
}
