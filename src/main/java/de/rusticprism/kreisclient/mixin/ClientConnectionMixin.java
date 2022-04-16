package de.rusticprism.kreisclient.mixin;

import de.rusticprism.kreisclient.KreisClient;
import de.rusticprism.kreisclient.utils.KreisClientCommand;
import de.rusticprism.kreisclient.utils.PacketEvent;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true)
    private static <T extends PacketListener> void onHandlePacket(Packet<T> packet, PacketListener listener, CallbackInfo info) {
        if (KreisClient.EVENTBUS.post(PacketEvent.Receive.get(packet)).isCancelled()) info.cancel();
    }


    @Inject(at = @At("HEAD"), method = "send(Lnet/minecraft/network/Packet;)V", cancellable = true)
    private void onSendPacketHead(Packet<?> packet, CallbackInfo info) {
        if (KreisClient.EVENTBUS.post(PacketEvent.Send.get(packet)).isCancelled()) info.cancel();
    }

    @Inject(method = "send(Lnet/minecraft/network/Packet;)V", at = @At("TAIL"))
    private void onSendPacketTail(Packet<?> packet, CallbackInfo info) {
        KreisClient.EVENTBUS.post(PacketEvent.Sent.get(packet));
    }
}
