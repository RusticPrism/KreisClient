package de.rusticprism.kreisclient.mixin.borderlesswindow;

import de.rusticprism.kreisclient.config.BorderlessFullscreenConfig;
import net.minecraft.client.WindowSettings;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.OptionalInt;

@Mixin(WindowSettings.class)
public abstract class WindowSettingsMixin {
    @Shadow
    @Final
    @Mutable
    public boolean fullscreen;


    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    @Inject(at = @At("RETURN"), method = "<init>")
    private void modifyInitialSettings(int width, int height, OptionalInt fullscreenWidth, OptionalInt fullscreenHeight, boolean fullscreen, CallbackInfo info) {
        // If the mod is enabled, set the fullscreen value (from run arguments) to false
        if (this.fullscreen) {
            this.fullscreen = false;
            // Tell WindowMixin that the initial state is to enable borderless fullscreen
            BorderlessFullscreenConfig.getInstance().setEnabled(true);
        }
    }
}
