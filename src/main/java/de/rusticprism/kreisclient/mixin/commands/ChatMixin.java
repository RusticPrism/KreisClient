package de.rusticprism.kreisclient.mixin.commands;

import de.rusticprism.kreisclient.KreisClient;
import de.rusticprism.kreisclient.utils.Prefix;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.message.ChatMessageSigner;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ChatMixin {

    @Inject(method = "sendChatMessagePacket", at = @At("HEAD"), cancellable = true)
    private void onChat(ChatMessageSigner signer, String message, Text preview, CallbackInfo ci) {
        String[] args1 = message.split(" ");
        String[] args = new String[args1.length - 1];
        System.arraycopy(args1, 1, args, 0, args1.length - 1);
        if(message.startsWith(Prefix.getCommandPrefix())) {
            KreisClient.cmdMan.perform(message.split(" ")[0],args);
            ci.cancel();
        }
    }
}
