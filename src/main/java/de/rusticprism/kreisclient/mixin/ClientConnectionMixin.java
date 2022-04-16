package de.rusticprism.kreisclient.mixin;

import de.rusticprism.kreisclient.commands.ServerCommand;
import de.rusticprism.kreisclient.utils.PacketEvent;
import de.rusticprism.kreisclient.utils.TickUtil;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.PacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientConnection.class)
public class ClientConnectionMixin {
    @Inject(method = "handlePacket", at = @At("HEAD"))
    private static <T extends PacketListener> void onHandlePacket(Packet<T> packet, PacketListener listener, CallbackInfo info) {
        ServerCommand.onReadPacket(PacketEvent.Receive.get(packet));
        TickUtil.INSTANCE.onReceivePacket(PacketEvent.Receive.get(packet));
    }
}
