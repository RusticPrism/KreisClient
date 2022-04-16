package de.rusticprism.kreisclient.mixin;

import de.rusticprism.kreisclient.KreisClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.NarratorMode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(method = "updateWindowTitle",at = @At("HEAD"),cancellable = true)
    public void setWindowTitle(CallbackInfo ci) {
        MinecraftClient.getInstance().getWindow().setTitle("KreisClient " + KreisClient.version + "  Minecraft: 1.18.2");
        MinecraftClient.getInstance().options.narrator = NarratorMode.OFF;
        ci.cancel();
    }
}
