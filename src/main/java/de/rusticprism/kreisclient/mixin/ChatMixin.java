package de.rusticprism.kreisclient.mixin;

import de.rusticprism.kreisclient.KreisClient;
import net.minecraft.client.gui.hud.ChatHudListener;
import net.minecraft.network.MessageType;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ChatHudListener.class)
public abstract class ChatMixin {
    @Inject(method = "onChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChat(MessageType type, Text message, UUID sender, CallbackInfo ci) {
        String[] args1 = message.getString().split(" ");
        String[] args = new String[args1.length - 2];
        for(int i = 2; i < args1.length; i++) {
            args[i -2] = args1[i];
        }
        if(args1[1].startsWith("+")) {
            KreisClient.cmdMan.perform(args1[1],args);
            ci.cancel();
        }
    }
}
