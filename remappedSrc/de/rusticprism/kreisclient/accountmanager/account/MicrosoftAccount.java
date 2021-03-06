package de.rusticprism.kreisclient.accountmanager.account;

import com.mojang.util.UUIDTypeAdapter;
import de.rusticprism.kreisclient.mixin.accountmanager.MinecraftClientAccessor;
import de.rusticprism.kreisclient.accountmanager.utils.Auth;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class MicrosoftAccount {
	private String username;
	private String accessToken;
	private String refreshToken;
	private UUID uuid;
	private int uses;
	private long lastUse;
	
	public MicrosoftAccount(String name, String token, String refresh, UUID uuid) {
		this.username = name;
		this.accessToken = token;
		this.refreshToken = refresh;
		this.uuid = uuid;
	}

	public String alias() {
		return username;
	}

	public void login(MinecraftClient mc, Consumer<Throwable> handler) {
		new Thread(() -> {
			try {
				syncRefresh();
			} catch (Throwable t) {
				mc.execute(() -> handler.accept(t));
				return;
			}
			mc.execute(() -> {
				((MinecraftClientAccessor)mc).setSession(new Session(username, UUIDTypeAdapter.fromUUID(uuid),
						accessToken, Optional.empty(), Optional.empty(), Session.AccountType.MOJANG));
				handler.accept(null);
			});
		}, "KreisClient Reauth Thread").start();
	}
	
	/**
	 * Synchronically validate and refresh account.
	 * @throws Throwable If we're unable to refresh account
	 */
	public void syncRefresh() throws Throwable {
		try {
			Auth.checkGameOwnership(accessToken);
			Pair<UUID, String> profile = Auth.getProfile(accessToken);
			uuid = profile.getLeft();
			username = profile.getRight();
		} catch (Throwable t) {
			try {
				Pair<String, String> authRefreshTokens = Auth.refreshToken(refreshToken);
				String refreshToken = authRefreshTokens.getRight();
				String xblToken = Auth.authXBL(authRefreshTokens.getLeft()); //authToken
				Pair<String, String> xstsTokenUserhash = Auth.authXSTS(xblToken);
				String accessToken = Auth.authMinecraft(xstsTokenUserhash.getRight(), xstsTokenUserhash.getLeft());
				Auth.checkGameOwnership(accessToken);
				Pair<UUID, String> profile = Auth.getProfile(accessToken);
				this.uuid = profile.getLeft();
				this.username = profile.getRight();
				this.accessToken = accessToken;
				this.refreshToken = refreshToken;
			} catch (Throwable th) {
				th.addSuppressed(t);
				throw th;
			}
    	}
	}

	public boolean editable() {
		return false;
	}

	public boolean online() {
		return true;
	}

	public int uses() {
		return uses;
	}

	public long lastUse() {
		return lastUse;
	}

	public void use() {
		uses++;
		lastUse = System.currentTimeMillis();
	}

	public UUID uuid() {
		return uuid;
	}
}
