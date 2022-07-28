package de.rusticprism.kreisclient;

import com.google.gson.Gson;
import de.rusticprism.kreisclient.discord.Discord;
import de.rusticprism.kreisclient.keybindings.KeyBindingHelper;
import de.rusticprism.kreisclient.modapi.ModRegistery;
import de.rusticprism.kreisclient.utils.CommandManager;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class KreisClient implements ModInitializer {
	public static final String MOD_ID = "kreisclient";
	public static final String version = "1.0.0";
	public static CommandManager cmdMan;
	public static ModRegistery modRegistery;
	public static final Logger LOGGER = LogManager.getLogger("KreisClient");
	public static KreisClient INSTANCE;
	public MinecraftClient MC = MinecraftClient.getInstance();
	public final Gson GSON = new Gson();
	public boolean perspectiveEnabled;
	public float cameraPitch;
	public float cameraYaw;
	public KeyBinding perspectiveKey;
	public KeyBinding zoomKey;

	@Override
	public void onInitialize() {
		LOGGER.info("KreisClient Started");
		INSTANCE = this;
		cmdMan = new CommandManager();
		modRegistery = new ModRegistery();

		Discord.startRPC();
		zoomKey = new KeyBinding("Zoom Key", InputUtil.Type.KEYSYM,GLFW.GLFW_KEY_C,"KreisClient")
;		perspectiveKey = new KeyBinding("Perspective Toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_G, "KreisClient");
		KeyBindingHelper.registerKeyBinding(perspectiveKey);
		KeyBindingHelper.registerKeyBinding(zoomKey);
	}
}
