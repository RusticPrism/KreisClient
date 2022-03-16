package de.rusticprism.kreisclient;

import com.google.gson.Gson;
import de.rusticprism.kreisclient.accountmanager.Config;
import de.rusticprism.kreisclient.accountmanager.utils.SkinRenderer;
import de.rusticprism.kreisclient.discord.Discord;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

import java.io.InputStream;

public class KreisClient implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "kreisclient";
	public static final Logger LOGGER = LogManager.getLogger("KreisClient");
	private static KeyBinding keyBinding;
	public static KreisClient INSTANCE;
	public static MinecraftClient MC = MinecraftClient.getInstance();
	public static final boolean modMenu = FabricLoader.getInstance().isModLoaded("modmenu");
	public static final Gson GSON = new Gson();

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		LOGGER.info("KreisClient Started");
		INSTANCE = this;


		keyBinding = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"Open ModMenu",
				InputUtil.Type.KEYSYM,
				GLFW.GLFW_KEY_RIGHT_SHIFT,
				"KreisClient"
		));

		Discord.startRPC();


		ClientLifecycleEvents.CLIENT_STARTED.register(mc -> {
			Config.load(mc);
			SkinRenderer.loadAllAsync(MinecraftClient.getInstance(), false, () -> {});
		});

	}
}
