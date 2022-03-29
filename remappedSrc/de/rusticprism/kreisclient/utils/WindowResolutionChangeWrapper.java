package de.rusticprism.kreisclient.utils;

import net.minecraft.client.WindowEventHandler;

public class WindowResolutionChangeWrapper implements WindowEventHandler {
    private boolean enabled = true;
    private final WindowEventHandler child;

    public WindowResolutionChangeWrapper(WindowEventHandler child) {
        this.child = child;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public void onWindowFocusChanged(boolean focused) {
        child.onWindowFocusChanged(focused);
    }

    @Override
    public void onResolutionChanged() {
        if (enabled) {
            child.onResolutionChanged();
        }
    }

    @Override
    public void onCursorEnterChanged() {
        child.onCursorEnterChanged();
    }
}
