package de.rusticprism.kreisclient.mixin.borderlesswindow;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.VideoOptionsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VideoOptionsScreen.class)
public class VideoModeFixMixin extends GameOptionsScreen {
    private VideoModeFixMixin(Screen parent, GameOptions gameOptions, Text title) {
        super(parent, gameOptions, title);
    }

    @Inject(at = @At("HEAD"), method = "removed()V")
    public void screenRemoved(CallbackInfo ci) {
        if (this.client != null) {
            this.client.getWindow().applyVideoMode();
        }
    }
}
