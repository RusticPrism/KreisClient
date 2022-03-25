package de.rusticprism.kreisclient.mixin.perspective;

import de.rusticprism.kreisclient.KreisClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
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
                    target = "net/minecraft/client/tutorial/TutorialManager.onUpdateMouse(DD)V"
            ),
            locals = LocalCapture.CAPTURE_FAILEXCEPTION
    )
    private void perspectiveUpdatePitchYaw(CallbackInfo info,double adjustedSens, double x, double y,int invert) {
        if (KreisClient.INSTANCE.perspectiveEnabled) {
            KreisClient.INSTANCE.cameraYaw += x / 8.0F;
            KreisClient.INSTANCE.cameraPitch += (y * invert * -1) / 8.0F;
            KreisClient.LOGGER.info(y + " " +  KreisClient.INSTANCE.cameraPitch);

            if (Math.abs(KreisClient.INSTANCE.cameraPitch) > 90.0F) {
                KreisClient.INSTANCE.cameraPitch = KreisClient.INSTANCE.cameraPitch < 0.0F ? 90.0F : -90.0F;
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
}
