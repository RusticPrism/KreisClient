package de.rusticprism.kreisclient.utils;

public interface WindowHooks {
    boolean borderlessfullscreen_getFullscreenState();
    void borderlessfullscreen_updateEnabledState(boolean destEnabledState, boolean currentFullscreenState, boolean destFullscreenState);
}
