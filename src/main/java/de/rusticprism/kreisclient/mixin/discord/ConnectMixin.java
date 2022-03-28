package de.rusticprism.kreisclient.mixin.discord;

import de.rusticprism.kreisclient.discord.Discord;
import de.rusticprism.kreisclient.modapi.KreisClientMod;
import de.rusticprism.kreisclient.modapi.Mod;
import de.rusticprism.kreisclient.modapi.ModRegistery;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ConnectScreen.class)
public class ConnectMixin {
    @Inject(at = @At(value= "INVOKE",target = "Ljava/lang/Thread;start()V"),method = "connect(Lnet/minecraft/client/MinecraftClient;Lnet/minecraft/client/network/ServerAddress;)V")
    public void onConnect(MinecraftClient client, ServerAddress address, CallbackInfo ci) {
        Discord.update("Playing MultiPlayer","Server: "+ address.getAddress());
        ModRegistery.mods.get("blockcounter").init(MinecraftClient.getInstance());
    }
}
