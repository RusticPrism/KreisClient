package de.rusticprism.kreisclient.mixin;

import de.rusticprism.kreisclient.modapi.KreisClientMod;
import de.rusticprism.kreisclient.modapi.ModRegistery;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class ModsRendererMixin {

    @Inject(method = "render",at = @At(value = "INVOKE",target = "Lnet/minecraft/client/gui/hud/InGameHud;renderStatusEffectOverlay(Lnet/minecraft/client/util/math/MatrixStack;)V"))
    public void onRender(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        for (KreisClientMod mod : ModRegistery.mods) {
            if(mod.isEnabled()) {
                mod.render(matrices,1,1, 16777215);
            }
        }
    }
}
