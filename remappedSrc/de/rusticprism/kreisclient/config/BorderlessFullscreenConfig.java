package de.rusticprism.kreisclient.config;

import com.google.gson.Gson;
import de.rusticprism.kreisclient.KreisClient;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;

public class BorderlessFullscreenConfig {
    private static final transient Path configFile = OtherUtil.getPath(Config.getConfigPath("borderlessfullscreen.txt"));
    private static final transient Logger LOGGER = KreisClient.LOGGER;

    private BorderlessFullscreenConfig() {
    }

    private static transient BorderlessFullscreenConfig INSTANCE = null;

    public static BorderlessFullscreenConfig getInstance() {
        if (INSTANCE == null) {
            Gson gson = new Gson();
            try (FileReader reader = new FileReader(configFile.toFile())) {
                INSTANCE = gson.fromJson(reader, BorderlessFullscreenConfig.class);
            } catch (FileNotFoundException ignored) {
                // Do nothing!
            } catch (IOException e) {
                LOGGER.error("Failed to read configuration", e);
            }
            if (INSTANCE == null) {
                INSTANCE = new BorderlessFullscreenConfig();
            }
        }
        return INSTANCE;
    }

    public CustomWindowDimensions customWindowDimensions = CustomWindowDimensions.INITIAL;
    public int forceWindowMonitor = -1;

    public static class CustomWindowDimensions {
        public static transient final CustomWindowDimensions INITIAL = new CustomWindowDimensions();

        public final boolean enabled;
        public final int x;
        public final int y;
        public final int width;
        public final int height;
        public boolean useMonitorCoordinates;

        private CustomWindowDimensions() {
            enabled = false;
            x = 0;
            y = 0;
            width = 0;
            height = 0;
            useMonitorCoordinates = true;
        }

    }
}
