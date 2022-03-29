package de.rusticprism.kreisclient.accountmanager.account;

import java.util.UUID;
import java.util.function.Consumer;

import net.minecraft.client.MinecraftClient;

public interface Account {
	String alias();
	void login(MinecraftClient mc, Consumer<Throwable> handler);
	boolean editable();
	boolean online();
	void use();
	int uses();
	long lastUse();
	
	default UUID uuid() {
		return null;
	}
}
