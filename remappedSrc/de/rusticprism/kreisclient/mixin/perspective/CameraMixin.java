package de.rusticprism.kreisclient.mixin.perspective;

import de.rusticprism.kreisclient.KreisClient;
import net.minecraft.client.render.Camera;
import net.minecraft.entity.Entity;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(Camera.class)
public class CameraMixin {
    @Shadow
    private float pitch;
    @Shadow
    private float yaw;

    @Inject(method = "update", at = @At(value = "INVOKE", target = "net/minecraft/client/render/Camera.moveBy(DDD)V", ordinal = 0))
    private void perspectiveUpdatePitchYaw(BlockView area, Entity focusedEntity, boolean thirdPerson, boolean inverseView, float tickDelta, CallbackInfo info) {
        if (KreisClient.INSTANCE.perspectiveEnabled) {
            this.pitch = KreisClient.INSTANCE.cameraPitch;
            this.yaw = KreisClient.INSTANCE.cameraYaw;
        }
    }

    @ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "net/minecraft/client/render/Camera.setRotation(FF)V", ordinal = 0))
    private void perspectiveFixRotation(Args args) {
        if (KreisClient.INSTANCE.perspectiveEnabled) {
            args.set(0, KreisClient.INSTANCE.cameraYaw);
            args.set(1, KreisClient.INSTANCE.cameraPitch);
        }
    }
}
