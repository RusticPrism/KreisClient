package de.rusticprism.kreisclient.mixin;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import de.rusticprism.kreisclient.KreisClient;
import de.rusticprism.kreisclient.accountmanager.gui.AccountScreen;
import de.rusticprism.kreisclient.buttons.ButtonWidget;
import de.rusticprism.kreisclient.config.OtherUtil;
import de.rusticprism.kreisclient.discord.Discord;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.awt.*;
import java.io.InputStream;

@Mixin(TitleScreen.class)
public abstract class TitleScreenMixin extends Screen {

	@Shadow public abstract void removed();

	@Shadow protected abstract void initWidgetsNormal(int y, int spacingY);
	@Mutable
	@Shadow @Final private static Identifier PANORAMA_OVERLAY;
	private int kreisclientTextX;

	private TitleScreenMixin() {
		super(null);
	}

	@Inject(method = "init", at = @At("HEAD"), cancellable = true)
	public void onInit(CallbackInfo ci) {
		ci.cancel();
		int j = this.height / 4 + 48;
		Discord.update("Idle","MainMenu");
			this.initWidgetsNormal(j, 24);
		InputStream inputStream16 = OtherUtil.getResource("assets/kreisclient/icons/icon_16x16.png");
		InputStream inputStream32 = OtherUtil.getResource("assets/kreisclient/icons/icon_32x32.png");
		MinecraftClient.getInstance().getWindow().setIcon(inputStream16,inputStream32);
		int kreisclientTextWidth = this.textRenderer.getWidth("§1KreisClient by §8RusticPrism");
		this.kreisclientTextX = this.width - kreisclientTextWidth - 2;
		this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, j + 72 + 12, 98, 20, Text.translatable("menu.options"), (button) -> this.client.setScreen(new OptionsScreen(this, this.client.options))));
		this.addDrawableChild(new ButtonWidget(this.width / 2 + 2, j + 72 + 12, 98, 20, Text.translatable("menu.quit"), (button) -> this.client.scheduleStop()));
		this.addDrawableChild(new ButtonWidget(this.width /2 -100, j + 48, 98,20 , Text.translatable("kreisclient.settings"), (button) -> KreisClient.LOGGER.info("Settings")));
		this.addDrawableChild(new ButtonWidget(this.width /2 + 2, j + 48, 98,20 , Text.translatable("kreisclient.cosmetics"), (button) -> KreisClient.LOGGER.info("Cosmetics")));
		this.addDrawableChild(new ButtonWidget(0,0,98,20,Text.translatable("kreisclient.accountmanager"), (button -> MinecraftClient.getInstance().setScreen(new AccountScreen(this)))));
	}

	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	public void onTopRender(MatrixStack ms, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		float f = 1.0F;
		PANORAMA_OVERLAY = new Identifier("kreisclient:backgrounds/background.png");
		RenderSystem.setShader(GameRenderer::getPositionTexShader);
		RenderSystem.setShaderTexture(0, PANORAMA_OVERLAY);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F,MathHelper.ceil(MathHelper.clamp(f, 0.0F, 1.0F)));
		drawTexture(ms, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
		float g = MathHelper.clamp(f - 1.0F, 0.0F, 1.0F);
		int l = MathHelper.ceil(g * 255.0F) << 24;
		drawStringWithShadow(ms, this.textRenderer, "§1KreisClient §8" + MinecraftClient.getInstance().getGame().getVersion().getName(), 2, this.height - 10, 16777215 | l);
		//drawCenteredText(ms, textRenderer, "Your logged in as " + client.getSession().getUsername(), 80, 50, Color.blue.getRGB());
		drawStringWithShadow(ms, this.textRenderer, "§1KreisClient §8by RusticPrism", this.kreisclientTextX, this.height - 10, Color.white.getRGB());

		super.render(ms, mouseX, mouseY, delta);
		ci.cancel();
	}

	@Inject(method = "initWidgetsNormal",at= @At("HEAD"), cancellable = true)
	public void CustomMultiSinglePlayerButtons(int y, int spacingY, CallbackInfo ci) {
		ci.cancel();
		this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, y , 200, 20, Text.translatable("menu.singleplayer"),
				(button) -> this.client.setScreen(new SelectWorldScreen(this))));
		this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, y + spacingY, 200, 20, Text.translatable("menu.multiplayer"), (button) -> {
			this.client.setScreen(new MultiplayerScreen(this));
		}));
	}
}
