package de.rusticprism.kreisclient.utils;

import de.rusticprism.kreisclient.KreisClient;

public class Zoom {
    private static boolean currentlyZoomed;
    private static boolean originalSmoothCameraEnabled;

    public static final double zoomLevel = 0.23;
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
}
