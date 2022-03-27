package de.rusticprism.kreisclient.mixin.zoom;

import de.rusticprism.kreisclient.utils.Zoom;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public class ZoomMixin {

    @Inject(method = "getFov(Lnet/minecraft/client/render/Camera;FZ)D", at = @At("RETURN"), cancellable = true)
    public void onZoom(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        if(Zoom.isZooming()) {
            double fov = cir.getReturnValue();
            cir.setReturnValue(fov * Zoom.zoomLevel);
        }

        Zoom.manageSmoothCamera();
    }
}
