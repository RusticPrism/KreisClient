package de.rusticprism.kreisclient.mixin;

import de.rusticprism.kreisclient.discord.Discord;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.TranslatableText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(MultiplayerScreen.class)
public class MultiplayerScreenMixin extends Screen {
	private MultiplayerScreenMixin() {
		super(null);
	}

	@Inject(method = "render", at = @At("TAIL"))
	public void onRender(MatrixStack ms, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		if (client.getSession().getAccessToken().equals("0") || client.getSession().getAccessToken().equals("-")) {
			List<OrderedText> list = textRenderer.wrapLines(new TranslatableText("ias.offline"), width);
			for (int i = 0; i < list.size(); i++) {
				textRenderer.drawWithShadow(ms, list.get(i), width / 2 - textRenderer.getWidth(list.get(i)) / 2, i * 9 + 1, 16737380);
			}
		}
	}
	
	@Inject(method = "init", at = @At("TAIL"))
	public void onInit(CallbackInfo ci) {
		Discord.update("Idle","MultiPlayerMenu");
	}
}
