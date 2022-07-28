package de.rusticprism.kreisclient.mixin.renderer.dynamicfps;

import de.rusticprism.kreisclient.KreisClient;
import de.rusticprism.kreisclient.render.SplashOverlay;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Overlay;
import net.minecraft.client.render.GameRenderer;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow public abstract void render(float tickDelta, long startTime, boolean tick);


    private boolean wasfokused = true;
    @Inject(method = "render",at = @At("HEAD"), cancellable = true)
    private void onRender(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if(GLFW.glfwGetWindowAttrib(MinecraftClient.getInstance().getWindow().getHandle(), GLFW.GLFW_HOVERED) != 0) {
            MinecraftClient.getInstance().getWindow().setFramerateLimit(MinecraftClient.getInstance().options.getMaxFps().getValue());
            wasfokused = true;
        }else if(!MinecraftClient.getInstance().isWindowFocused()) {
            MinecraftClient.getInstance().getWindow().setFramerateLimit(30);
            ci.cancel();
            wasfokused = false;
        }else if(!wasfokused){
            MinecraftClient.getInstance().getWindow().setFramerateLimit(MinecraftClient.getInstance().options.getMaxFps().getValue());
            wasfokused = true;
        }
    }
    @Inject(at = @At("HEAD"), method = "renderWorld", cancellable = true)
    private void onRenderWorld(CallbackInfo callbackInfo) {
        Overlay overlay = MinecraftClient.getInstance().getOverlay();
        if (overlay instanceof SplashOverlay) {
            SplashOverlay splashScreen = (SplashOverlay) overlay;
            if (splashScreen.isReloading()) {
                callbackInfo.cancel();
            }
        }
    }
}
