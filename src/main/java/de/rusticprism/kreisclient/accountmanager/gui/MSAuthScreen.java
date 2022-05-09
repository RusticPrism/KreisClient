package de.rusticprism.kreisclient.accountmanager.gui;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import de.rusticprism.kreisclient.KreisClient;
import de.rusticprism.kreisclient.accountmanager.Config;
import de.rusticprism.kreisclient.accountmanager.account.Account;
import de.rusticprism.kreisclient.accountmanager.account.AuthException;
import de.rusticprism.kreisclient.accountmanager.account.MicrosoftAccount;
import de.rusticprism.kreisclient.accountmanager.utils.Auth;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Util;
import org.apache.commons.lang3.tuple.Pair;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Screen for adding Microsoft accounts.
 * @author VidTu
 */
public class MSAuthScreen extends Screen {
	public static final String[] symbols = new String[]{"▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃", "_ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄",
			"_ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅", "_ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆", "_ _ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇", "_ _ _ _ _ ▃ ▄ ▅ ▆ ▇ █",
			"_ _ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇", "_ _ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆", "_ _ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅", "_ ▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄",
			"▃ ▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃", "▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _", "▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _", "▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _",
			"▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _ _", "█ ▇ ▆ ▅ ▄ ▃ _ _ _ _ _", "▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _ _", "▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _ _",
			"▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _ _", "▄ ▅ ▆ ▇ █ ▇ ▆ ▅ ▄ ▃ _"};
	public final Screen prev;
	private HttpServer srv;
	private int tick;
	private Text state = new TranslatableText("kreisclient.auth.checkbrowser");
	private List<OrderedText> error;

	public MSAuthScreen(Screen prev) {
		super(new TranslatableText("kreisclient.auth.title"));
		this.prev = prev;
		new Thread(() -> {
			try {
				srv = HttpServer.create(new InetSocketAddress(59125), 0);
	        	srv.createContext("/", ex -> {
					try {
						ex.getResponseHeaders().add("Location", "http://client.kreiscraft.de/closenow");
						ex.sendResponseHeaders(302, -1);
						new Thread(() -> auth(ex.getRequestURI().getQuery()), "KreisClient Auth Thread").start();
					} catch (Throwable t) {
						KreisClient.LOGGER.warn("Unable to process request 'auth' on MS auth server", t);
						try {
							if (srv != null) srv.stop(0);
						} catch (Throwable th) {
							KreisClient.LOGGER.warn("Unable to stop fail-requested MS auth server", th);
						}
					}
				});
	        	srv.start();
	        	Util.getOperatingSystem().open("https://login.live.com/oauth20_authorize.srf" +
                        "?client_id=54fd49e4-2103-4044-9603-2b028c814ec3" +
                        "&response_type=code" +
                        "&scope=XboxLive.signin%20XboxLive.offline_access" +
                        "&redirect_uri=http://localhost:59125" +
                        "&prompt=consent");
			} catch (Throwable t) {
				KreisClient.LOGGER.warn("Unable to start auth server", t);
				try {
					if (srv != null) srv.stop(0);
				} catch (Throwable th) {
					KreisClient.LOGGER.warn("Unable to stop fail-started MS auth server", th);
				}
				error(t);
			}
		}, "KreisClient Auth Server Thread").start();
	}
	
	private void auth(String query) {
		try {
			state = new TranslatableText("kreisclient.auth.progress");
			if (query == null) throw new NullPointerException("query=null");
			if (query.equals("error=access_denied&error_description=The user has denied access to the scope requested by the client application."))
				throw new AuthException(new TranslatableText("kreisclient.auth.error.revoked"));
			if (!query.startsWith("code=")) throw new IllegalStateException("query=" + query);
			Pair<String, String> authRefreshTokens = Auth.authCode2Token(query.replace("code=", ""));
			String refreshToken = authRefreshTokens.getRight();
			String xblToken = Auth.authXBL(authRefreshTokens.getLeft()); //authToken
			Pair<String, String> xstsTokenUserhash = Auth.authXSTS(xblToken);
			String accessToken = Auth.authMinecraft(xstsTokenUserhash.getRight(), xstsTokenUserhash.getLeft());
			Auth.checkGameOwnership(accessToken);
			Pair<UUID, String> profile = Auth.getProfile(accessToken);
			client.execute(() -> {
				if (client.currentScreen == this) {
					new MicrosoftAccount(profile.getRight(), accessToken, refreshToken, profile.getLeft()).login(MinecraftClient.getInstance(),throwable -> {});
					client.setScreen(prev);
				}
			});
		} catch (Throwable t) {
			KreisClient.LOGGER.warn("Unable to auth thru MS", t);
			error(t);
		}
	}
	
	public void error(Throwable t) {
		client.execute(() -> {
			if (t instanceof AuthException) {
				error = textRenderer.wrapLines(((AuthException)t).getText(), width - 20);
			} else {
				error = textRenderer.wrapLines(new TranslatableText("kreisclient.auth.unknown", t.toString()), width - 20);
			}
		});
	}

	@Override
	public void init() {
		addDrawableChild(new ButtonWidget(this.width / 2 - 75, this.height - 28, 150, 20, new TranslatableText("kreisclient.gui.cancel"), btn -> client.setScreen(prev)));
	}
	
	@Override
	public void tick() {
		tick++;
	}
	
	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}
	
	@Override
	public void removed() {
		try {
			if (srv != null) srv.stop(0);
		} catch (Throwable t) {
			KreisClient.LOGGER.warn("Unable to stop MS auth server", t);
		}
		super.removed();
	}
	
	@Override
	public boolean keyPressed(int key, int oldkey, int mods) {
		if (key == GLFW.GLFW_KEY_ESCAPE) {
			client.setScreen(prev);
			return true;
		}
		return super.keyPressed(key, oldkey, mods);
	}
	
	@Override
	public void render(MatrixStack ms, int mouseX, int mouseY, float delta) {
		renderBackground(ms);
		drawCenteredText(ms, textRenderer, this.title, this.width / 2, 7, -1);
		if (error != null) {
			for (int i = 0; i < error.size(); i++) {
				textRenderer.drawWithShadow(ms, error.get(i), this.width / 2 - textRenderer.getWidth(error.get(i)) / 2, height / 2 - 5 + i * 10 - error.size() * 5, 0xFFFF0000);
				if (i > 6) break; //Exceptions can be very large.
			}
		} else {
			drawCenteredText(ms, textRenderer, state, width / 2, height / 2 - 10, -1);
			drawCenteredText(ms, textRenderer, symbols[tick % symbols.length], width / 2, height / 2, 0xFFFF9900);
		}
		super.render(ms, mouseX, mouseY, delta);
	}
}
