package de.rusticprism.kreisclient.mixin.zoom;

import de.rusticprism.kreisclient.utils.Zoom;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Shader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SynchronousResourceReloader;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GameRenderer.class)
public abstract class ZoomMixin implements AutoCloseable, SynchronousResourceReloader {


    @Redirect(at = @At(value = "FIELD",target = "Lnet/minecraft/client/option/GameOptions;fov:D",opcode = Opcodes.GETFIELD,ordinal = 0),method = "getFov")
    public double onZoom(GameOptions instance) {
        Zoom.manageSmoothCamera();
        return Zoom.changeFovBasedOnZoom(instance.fov);
    }

    @Shadow
    @Override
    public void reload(ResourceManager manager) {

    }
    @Shadow
    @Override
    public void close() throws Exception {

    }
}
