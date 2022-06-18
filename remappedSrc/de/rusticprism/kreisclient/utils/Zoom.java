package de.rusticprism.kreisclient.utils;

import de.rusticprism.kreisclient.KreisClient;
import net.minecraft.client.option.GameOptions;
import net.minecraft.util.math.MathHelper;

public class Zoom {
    private static boolean currentlyZoomed;
    private static boolean originalSmoothCameraEnabled;
    private static final double defaultLevel = 3;
    private static Double defaultMouseSensitivity;

    public static Double zoomLevel;

    public static void manageSmoothCamera() {
        if (zoomStarting()) {
            zoomStarted();
            enableSmoothCamera();
        }

        if (zoomStopping()) {
            zoomStopped();
            resetSmoothCamera();
        }
    }

    public static boolean isZooming() {
        return KreisClient.zoomKey.isPressed();
    }

    private static boolean isSmoothCamera() {
        return KreisClient.MC.options.smoothCameraEnabled;
    }

    private static void enableSmoothCamera() {
        KreisClient.MC.options.smoothCameraEnabled = true;
    }

    private static void disableSmoothCamera() {
        KreisClient.MC.options.smoothCameraEnabled = false;
    }

    private static boolean zoomStarting() {
        return isZooming() && !currentlyZoomed;
    }

    private static boolean zoomStopping() {
        return !isZooming() && currentlyZoomed;
    }

    private static void zoomStarted() {
        originalSmoothCameraEnabled = isSmoothCamera();
        currentlyZoomed = true;
    }

    private static void zoomStopped() {
        currentlyZoomed = false;
    }

    private static void resetSmoothCamera() {
        if (originalSmoothCameraEnabled) {
            enableSmoothCamera();
        } else {
            disableSmoothCamera();
        }
    }

    public static double changeFovBasedOnZoom(double fov) {
        GameOptions gameOptions = KreisClient.MC.options;

        if (zoomLevel == null)
            zoomLevel = defaultLevel;

        if (!isZooming()) {
            zoomLevel = defaultLevel;

            if (defaultMouseSensitivity != null) {
                gameOptions.getMouseSensitivity().setValue(defaultMouseSensitivity);
                defaultMouseSensitivity = null;
            }

            return fov;
        }

        if (defaultMouseSensitivity == null)
            defaultMouseSensitivity = gameOptions.getMouseSensitivity().getValue();

        // Adjust mouse sensitivity in relation to zoom level.
        // (fov / currentLevel) / fov is a value between 0.02 (50x zoom)
        // and 1 (no zoom).
        gameOptions.getMouseSensitivity().setValue(defaultMouseSensitivity * (fov / zoomLevel / fov));

        return fov / zoomLevel;
    }

    public static void onMouseScroll(double amount) {
        if (!isZooming())
            return;

        if (zoomLevel == null)
            zoomLevel = defaultLevel;

        if (amount > 0)
            zoomLevel *= 1.1;
        else if (amount < 0)
            zoomLevel *= 0.9;

        zoomLevel = MathHelper.clamp(zoomLevel, 1, 50);
    }
}
