package de.rusticprism.kreisclient.config;

import de.rusticprism.kreisclient.utils.WindowHooks;
import de.rusticprism.kreisclient.utils.config.FileConfiguration;
import de.rusticprism.kreisclient.utils.config.Configuration;
import net.minecraft.client.MinecraftClient;

public class BorderlessFullscreenConfig {
    private static final FileConfiguration configFile = new Configuration("borderlessfullscreen.txt");

    private BorderlessFullscreenConfig(boolean enabled,boolean vanillasetting) {
       this.enableBorderlessFullscreen = enabled;
        this.addToVanillaVideoSettings = vanillasetting;
    }

    private static BorderlessFullscreenConfig INSTANCE = null;

    public static BorderlessFullscreenConfig getInstance() {
        if(INSTANCE == null) {
            INSTANCE = new BorderlessFullscreenConfig(configFile.getBoolean("Enabled"),configFile.getBoolean("VanillaSetting"));
            INSTANCE.save(false);
        }
        System.out.println(INSTANCE.enableBorderlessFullscreen);
        INSTANCE.enabledPending = INSTANCE.enableBorderlessFullscreen;
        return INSTANCE;
    }

    private boolean enableBorderlessFullscreen;
    public boolean addToVanillaVideoSettings;
    public boolean enableMacOS = false;

    public CustomWindowDimensions customWindowDimensions = CustomWindowDimensions.INITIAL;
    public int forceWindowMonitor = -1;

    public static class CustomWindowDimensions {
        public static final CustomWindowDimensions INITIAL = new CustomWindowDimensions();

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

        public CustomWindowDimensions(boolean enabled, int x, int y, int width, int height, boolean useMonitorCoordinates) {
            this.enabled = enabled;
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.useMonitorCoordinates = useMonitorCoordinates;
        }

        public CustomWindowDimensions setEnabled(boolean enabled) {
            return new CustomWindowDimensions(enabled, x, y, width, height, useMonitorCoordinates);
        }

        public CustomWindowDimensions setX(int x) {
            return new CustomWindowDimensions(enabled, x, y, width, height, useMonitorCoordinates);
        }

        public CustomWindowDimensions setY(int y) {
            return new CustomWindowDimensions(enabled, x, y, width, height, useMonitorCoordinates);
        }

        public CustomWindowDimensions setWidth(int width) {
            return new CustomWindowDimensions(enabled, x, y, width, height, useMonitorCoordinates);
        }

        public CustomWindowDimensions setHeight(int height) {
            return new CustomWindowDimensions(enabled, x, y, width, height, useMonitorCoordinates);
        }

        public CustomWindowDimensions setUseMonitorCoordinates(boolean useMonitorCoordinates) {
            return new CustomWindowDimensions(enabled, x, y, width, height, useMonitorCoordinates);
        }
    }

    private transient boolean enabledPending = true;
    private transient boolean enabledDirty = false;

    public void setEnabledPending(boolean en) {
        if (enabledPending != en) {
            enabledPending = en;
            enabledDirty = (en != isEnabled());
        }
    }

    public boolean isEnabledOrPending() {
        return enabledDirty ? enabledPending : isEnabled();
    }

    public boolean isEnabledDirty() {
        return enabledDirty;
    }

    public boolean isEnabled() {
        return enableBorderlessFullscreen;
    }

    public void setEnabled(boolean enabled) {
        this.enableBorderlessFullscreen = enabled;
    }

    public void save() {
        //noinspection ConstantConditions
        WindowHooks window = (WindowHooks) (Object) MinecraftClient.getInstance().getWindow();
        save(window.borderlessfullscreen_getFullscreenState());
    }

    private void save(boolean destFullscreenState) {
        if (enabledDirty) {
            //noinspection ConstantConditions
            WindowHooks window = (WindowHooks) (Object) MinecraftClient.getInstance().getWindow();
            boolean currentState = window.borderlessfullscreen_getFullscreenState();

            // This must be done before changing window mode/pos/size as changing those restarts FullScreenOptionMixin
            enabledDirty = false;

            window.borderlessfullscreen_updateEnabledState(isEnabled(), currentState, destFullscreenState);
        }
    }
}
