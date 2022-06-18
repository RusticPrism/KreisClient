package de.rusticprism.kreisclient.mixin.zoom;

import de.rusticprism.kreisclient.utils.Zoom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.CameraSubmersionType;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
public abstract class ZoomMixin implements AutoCloseable, SynchronousResourceReloader {


    @Shadow private float fovMultiplier;
    @Shadow private float lastFovMultiplier;
    @Shadow @Final private MinecraftClient client;
    @Shadow private boolean renderingPanorama;

    @Inject(method = "getFov", at = @At("HEAD"), cancellable = true)
    public void onZoom(Camera camera, float tickDelta, boolean changingFov, CallbackInfoReturnable<Double> cir) {
        if (this.renderingPanorama) {
            cir.setReturnValue(90.0);
        } else {
            double d = 70.0;
            if (changingFov) {
                d = (double) this.client.options.getFov().getValue();
                d *= MathHelper.lerp(tickDelta, this.lastFovMultiplier, this.fovMultiplier);
            }

            if (camera.getFocusedEntity() instanceof LivingEntity && ((LivingEntity)camera.getFocusedEntity()).isDead()) {
                float f = Math.min((float)((LivingEntity)camera.getFocusedEntity()).deathTime + tickDelta, 20.0F);
                d /= (1.0F - 500.0F / (f + 500.0F)) * 2.0F + 1.0F;
            }

            CameraSubmersionType cameraSubmersionType = camera.getSubmersionType();
            if (cameraSubmersionType == CameraSubmersionType.LAVA || cameraSubmersionType == CameraSubmersionType.WATER) {
                d *= MathHelper.lerp(this.client.options.getFovEffectScale().getValue(), 1.0, 0.8571428656578064);
            }

            cir.setReturnValue(Zoom.changeFovBasedOnZoom(d));
        }
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
