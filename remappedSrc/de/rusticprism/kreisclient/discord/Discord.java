package de.rusticprism.kreisclient.discord;

import de.rusticprism.kreisclient.KreisClient;

public class Discord {
    private static String discordID = "922209722839465995";
    private static DiscordRichPresence discordRichPresence = new DiscordRichPresence();
    private static DiscordRPC discordRPC = DiscordRPC.INSTANCE;

    public static void startRPC() {
        DiscordEventHandlers eventHandlers = new DiscordEventHandlers();
        eventHandlers.disconnected = (var1, var2) -> KreisClient.LOGGER.info("DiscordRPC disconnected: " + var1 + ", var2: " + var2);

        discordRPC.Discord_Initialize(discordID, eventHandlers, true, null);

        discordRichPresence.startTimestamp = System.currentTimeMillis() / 1000L;
        discordRichPresence.details = "Starting...";
        discordRichPresence.largeImageKey = "large";
        discordRichPresence.state = "Playing";
        discordRPC.Discord_UpdatePresence(discordRichPresence);
    }

    public static void stopRPC() {
        discordRPC.Discord_Shutdown();
        discordRPC.Discord_ClearPresence();
    }
    public static void update(String line1,String line2) {
        discordRichPresence.details = line1;
        discordRichPresence.state = line2;
        discordRPC.Discord_UpdatePresence(discordRichPresence);
    }
}
