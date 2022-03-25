package de.rusticprism.kreisclient;

import com.google.gson.Gson;
import de.rusticprism.kreisclient.accountmanager.Config;
import de.rusticprism.kreisclient.discord.Discord;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.impl.client.keybinding.KeyBindingRegistryImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class KreisClient implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MOD_ID = "kreisclient";
	public static final Logger LOGGER = LogManager.getLogger("KreisClient");
	public static KreisClient INSTANCE;
	public static MinecraftClient MC = MinecraftClient.getInstance();
	public static final boolean modMenu = FabricLoader.getInstance().isModLoaded("modmenu");
	public static final Gson GSON = new Gson();
	public boolean perspectiveEnabled;
	public float cameraPitch;
	public float cameraYaw;
	private boolean held = false;
	private static KeyBinding toggleKey;

	@Override
	public void onInitialize() {
		LOGGER.info("KreisClient Started");
		INSTANCE = this;


		KeyBinding openmodmenu = KeyBindingHelper.registerKeyBinding(new KeyBinding(
				"Open ModMenu",
				InputUtil.Type.KEYSYM,
				344,
				"KreisClient"
		));

		Discord.startRPC();


		KeyBindingRegistryImpl.addCategory("KreisClient");
		KeyBindingRegistryImpl.registerKeyBinding(toggleKey = new KeyBinding("Perspective Toggle", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_F4, "KreisClient"));

		ClientTickEvents.START_CLIENT_TICK.register(e -> {
			if (MC.player != null) {
					this.perspectiveEnabled = toggleKey.isPressed();

					if (this.perspectiveEnabled && !this.held) {
						this.held = true;
						this.cameraPitch = MC.player.getPitch();
						this.cameraYaw = MC.player.getYaw();
						MC.options.setPerspective(Perspective.THIRD_PERSON_BACK);
					}
					if (!this.perspectiveEnabled && this.held) {
						this.held = false;
						MC.options.setPerspective(Perspective.FIRST_PERSON);
					}

					if (this.perspectiveEnabled && MC.options.getPerspective() != Perspective.THIRD_PERSON_BACK) {
						this.perspectiveEnabled = false;
					}
			}
		});
	}
}
