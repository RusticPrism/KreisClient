package de.rusticprism.kreisclient.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import de.rusticprism.kreisclient.modapi.ModRegistery;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class GameRendererMixin {

    @Inject(method = "render",at = @At("TAIL"))
    public void onRender(float tickDelta, long startTime, boolean tick, CallbackInfo ci) {
        if(MinecraftClient.getInstance().player != null) {
            ModRegistery.mods.get("blockcounter").render(RenderSystem.getModelViewStack(), 1, 1);
        }
    }
}
