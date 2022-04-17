package de.rusticprism.kreisclient.mixin.commands;

import de.rusticprism.kreisclient.KreisClient;
import de.rusticprism.kreisclient.utils.Prefix;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ChatMixin {

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChat(String message, CallbackInfo ci) {
        String[] args1 = message.split(" ");
        String[] args = new String[args1.length - 1];
        for(int i = 1; i < args1.length; i++) {
            args[i -1] = args1[i];
        }
        if(message.startsWith(Prefix.getCommandPrefix())) {
            KreisClient.cmdMan.perform(message.split(" ")[0],args);
            ci.cancel();
        }
    }
}
