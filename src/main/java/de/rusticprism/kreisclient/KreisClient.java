package de.rusticprism.kreisclient;

import com.google.gson.Gson;
import de.rusticprism.kreisclient.config.BorderlessFullscreenConfig;
import de.rusticprism.kreisclient.discord.Discord;
import de.rusticprism.kreisclient.keys.Perspectivekey;
import de.rusticprism.kreisclient.modapi.ModMenu;
import de.rusticprism.kreisclient.modapi.ModRegistery;
import de.rusticprism.kreisclient.utils.CommandManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class KreisClient implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "kreisclient";
	public static final String version = "1.0.0";
	public static CommandManager cmdMan;
	public static ModRegistery modRegistery;
	public static final Logger LOGGER = LogManager.getLogger("KreisClient");
	public static KreisClient INSTANCE;
	public static MinecraftClient MC = MinecraftClient.getInstance();
	public static final boolean modMenu = FabricLoader.getInstance().isModLoaded("modmenu");
	public static final Gson GSON = new Gson();
	public boolean perspectiveEnabled;
	public float cameraPitch;
	public float cameraYaw;
	private static KeyBinding perspectiveKey;
	public static KeyBinding zoomKey;

	@Override
	public void onInitialize() {
		LOGGER.info("KreisClient Started");
		INSTANCE = this;
		cmdMan = new CommandManager();
		modRegistery = new ModRegistery();

		KeyBinding openmodmenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"Open ModMenu",
				InputUtil.Type.KEYSYM,
				344,
				"KreisClient"
		));


		Discord.startRPC();

		perspectiveKey = new KeyBinding("Perspective Toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "KreisClient");
		KeyBindingHelper.registerKeyBinding(zoomKey = new KeyBinding("Zoom",InputUtil.Type.KEYSYM,67,"KreisClient"));
		ClientTickEvents.START_CLIENT_TICK.register(e ->  {
			Perspectivekey.call(perspectiveKey);
			ModMenu.call(openmodmenu, e.currentScreen);
		});

	}
}
