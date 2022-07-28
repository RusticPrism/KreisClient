package de.rusticprism.kreisclient.mixin.commands;

import com.mojang.authlib.GameProfile;
import de.rusticprism.kreisclient.KreisClient;
import de.rusticprism.kreisclient.utils.Prefix;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.encryption.PlayerPublicKey;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class ChatMixin extends AbstractClientPlayerEntity {

    public ChatMixin(ClientWorld world, GameProfile profile, @Nullable PlayerPublicKey publicKey) {
        super(world, profile, publicKey);
    }

    @Inject(method = "sendChatMessage(Ljava/lang/String;Lnet/minecraft/text/Text;)V", at = @At("HEAD"), cancellable = true)
    private void onChat(String message, Text preview, CallbackInfo ci) {
        String[] args1 = message.split(" ");
        String[] args = new String[args1.length - 1];
        System.arraycopy(args1, 1, args, 0, args1.length - 1);
        if(message.startsWith(Prefix.getCommandPrefix())) {
            KreisClient.cmdMan.perform(message.split(" ")[0],args);
            ci.cancel();
        }
    }
}
