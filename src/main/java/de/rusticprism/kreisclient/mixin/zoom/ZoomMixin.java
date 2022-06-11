package de.rusticprism.kreisclient.mixin.zoom;

import de.rusticprism.kreisclient.utils.Zoom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class ZoomMixin implements AutoCloseable, SynchronousResourceReloader {


    @Inject(at = @At("RETURN"),method = "getFov", cancellable = true)
    public void onZoom(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        Zoom.manageSmoothCamera();
        cir.setReturnValue(Zoom.changeFovBasedOnZoom(MinecraftClient.getInstance().options.getFov().getValue()));
    }

    @Shadow
    @Override
    public void reload(ResourceManager manager) {

    }
    @Shadow
    @Override
    public void close() {

    }
}
