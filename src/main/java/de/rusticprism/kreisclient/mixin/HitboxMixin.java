package de.rusticprism.kreisclient.mixin;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.entity.Entity;
import net.minecraft.resource.SynchronousResourceReloader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EntityRenderDispatcher.class)
public abstract class HitboxMixin implements SynchronousResourceReloader {

    @Redirect(method = "render",at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;doesRenderOnFire()Z"))
    public boolean render(Entity instance) {
        return true;
    }
}
