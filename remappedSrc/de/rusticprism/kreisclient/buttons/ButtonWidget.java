package de.rusticprism.kreisclient.buttons;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.narration.NarrationPart;
import net.minecraft.client.gui.widget.PressableWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ButtonWidget extends PressableWidget {
    private static final Identifier Widget = new Identifier("kreisclient:gui/widgets.png");
    public static final ButtonWidget.TooltipSupplier EMPTY = (button, matrices, mouseX, mouseY) -> {
    };
    public ButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress) {
        this(x, y, width, height, message, onPress, EMPTY);
    }

    public ButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, ButtonWidget.TooltipSupplier tooltipSupplier) {
        super(x, y, width, height, message);
        this.onPress = onPress;
        this.tooltipSupplier = tooltipSupplier;
    }
    protected final PressAction onPress;
    protected final ButtonWidget.TooltipSupplier tooltipSupplier;

    @Override
    public void onPress() {
        this.onPress.onPress(this);
    }

    public void renderTooltip(MatrixStack matrices, int mouseX, int mouseY) {
        this.tooltipSupplier.onTooltip(this, matrices, mouseX, mouseY);
    }

    public void appendNarrations(NarrationMessageBuilder builder) {
        this.appendDefaultNarrations(builder);
        this.tooltipSupplier.supply((text) -> {
            builder.put(NarrationPart.HINT, text);
        });
    }

    @Environment(EnvType.CLIENT)
    public interface TooltipSupplier {
        void onTooltip(ButtonWidget button, MatrixStack matrices, int mouseX, int mouseY);

        default void supply(Consumer<Text> consumer) {
        }
    }

    @Environment(EnvType.CLIENT)
    public interface PressAction {
        void onPress(ButtonWidget button);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        if (this.isHovered()) {
            this.renderTooltip(matrices, mouseX, mouseY);
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        TextRenderer textRenderer = minecraftClient.textRenderer;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, Widget);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.drawTexture(matrices, this.x, this.y, 0, 46 + i * 20, this.width / 2, this.height);
        this.drawTexture(matrices, this.x + this.width / 2, this.y, 200 - this.width / 2, 46 + i * 20, this.width / 2, this.height);
        this.renderBackground(matrices, minecraftClient, mouseX, mouseY);
        int j = this.active ? 16777215 : 10526880;
        drawCenteredText(matrices, textRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);
    }
}
