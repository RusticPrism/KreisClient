package de.rusticprism.kreisclient.mixin.discord;

import de.rusticprism.kreisclient.discord.Discord;
import de.rusticprism.kreisclient.modapi.ModRegistery;
import net.minecraft.client.MinecraftClient;
import net.minecraft.server.integrated.IntegratedServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IntegratedServer.class)
public class SinglePlayerMixin {
    @Inject(method = "setupServer",at = @At("HEAD"))
    public void onStart(CallbackInfoReturnable<Boolean> cir) {
        Discord.update("Playing","SinglePlayer");
    }
}
