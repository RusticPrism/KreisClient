package de.rusticprism.kreisclient.mixin.perspective;

import de.rusticprism.kreisclient.KreisClient;
import de.rusticprism.kreisclient.utils.Zoom;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.checkerframework.checker.units.qual.A;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({Mouse.class})
public class MouseMixin {

    @Inject(
            method = "updateMouse",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/tutorial/TutorialManager;onUpdateMouse(DD)V"
            ),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private void perspectiveUpdatePitchYaw(CallbackInfo ci, double d, double e, double x, double y, double f, double g, double h, int i) {
        if (KreisClient.INSTANCE.perspectiveEnabled) {
            KreisClient.INSTANCE.cameraYaw += x / 8.0F;
            KreisClient.INSTANCE.cameraPitch += (y * i * -1) / 8.0F;

            if (KreisClient.INSTANCE.cameraPitch > 90.0F) {
                KreisClient.INSTANCE.cameraPitch = 90.0F;
            }
            if(KreisClient.INSTANCE.cameraPitch < -90.0F) {
                KreisClient.INSTANCE.cameraPitch = -90.0F;
            }
        }
    }

    @Inject(
            method = "updateMouse",
            at = @At(
                    value = "INVOKE",
                    target = "net/minecraft/client/network/ClientPlayerEntity.changeLookDirection(DD)V"
            ),
            cancellable = true)
    private void perspectivePreventPlayerMovement(CallbackInfo info) {
        if (KreisClient.INSTANCE.perspectiveEnabled) {
            info.cancel();
        }
    }
    @Inject(method = "onMouseScroll",at = @At("RETURN"), cancellable = true)
    private void onMouseScroll(long window, double horizontal, double vertical, CallbackInfo ci) {
            Zoom.onMouseScroll(vertical);
    }
}
