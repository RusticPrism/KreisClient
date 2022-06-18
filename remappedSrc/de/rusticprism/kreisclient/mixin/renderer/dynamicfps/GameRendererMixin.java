package de.rusticprism.kreisclient.mixin.renderer.dynamicfps;

import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
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
            MinecraftClient.getInstance().getWindow().setFramerateLimit(1);
            ci.cancel();
            wasfokused = false;
        }else if(!wasfokused){
            MinecraftClient.getInstance().getWindow().setFramerateLimit(MinecraftClient.getInstance().options.getMaxFps().getValue());
            wasfokused = true;
        }
    }
}
