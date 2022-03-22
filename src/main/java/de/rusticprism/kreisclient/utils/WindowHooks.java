package de.rusticprism.kreisclient.utils;

public interface WindowHooks {
    boolean borderlessmining_getFullscreenState();
    void borderlessmining_updateEnabledState(boolean destEnabledState, boolean currentFullscreenState, boolean destFullscreenState);
}
