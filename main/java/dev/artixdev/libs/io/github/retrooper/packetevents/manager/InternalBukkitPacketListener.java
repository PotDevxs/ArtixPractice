package dev.artixdev.libs.io.github.retrooper.packetevents.manager;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.InternalPacketListener;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.ConnectionState;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.protocolsupport.ProtocolSupportUtil;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.viaversion.ViaVersionUtil;

public class InternalBukkitPacketListener extends InternalPacketListener {
   public void onPacketReceive(PacketReceiveEvent event) {
      User user = event.getUser();
      if (event.getPacketType() == PacketType.Handshaking.Client.HANDSHAKE) {
         InetSocketAddress address = event.getSocketAddress();
         WrapperHandshakingClientHandshake handshake = new WrapperHandshakingClientHandshake(event);
         ConnectionState nextState = handshake.getNextConnectionState();
         ClientVersion clientVersion = handshake.getClientVersion();
         PacketEvents.getAPI().getLogManager().debug("Read handshake version for " + address.getHostString() + ":" + address.getPort() + " as " + clientVersion);
         if (ViaVersionUtil.isAvailable()) {
            clientVersion = ClientVersion.getById(ViaVersionUtil.getProtocolVersion(user));
            PacketEvents.getAPI().getLogManager().debug("Read ViaVersion version for " + address.getHostString() + ":" + address.getPort() + " as " + clientVersion + " with UUID=" + user.getUUID());
         } else if (ProtocolSupportUtil.isAvailable()) {
            clientVersion = ClientVersion.getById(ProtocolSupportUtil.getProtocolVersion((SocketAddress)user.getAddress()));
            PacketEvents.getAPI().getLogManager().debug("Read ProtocolSupport version for " + address.getHostString() + ":" + address.getPort() + " as " + clientVersion);
         }

         if (clientVersion == ClientVersion.UNKNOWN) {
            PacketEvents.getAPI().getLogManager().debug("Client version for " + address.getHostString() + ":" + address.getPort() + " is unknown!");
         }

         user.setClientVersion(clientVersion);
         PacketEvents.getAPI().getLogManager().debug("Processed " + address.getHostString() + ":" + address.getPort() + "'s client version. Client Version: " + clientVersion.getReleaseName());
         user.setConnectionState(nextState);
      } else {
         super.onPacketReceive(event);
      }

   }
}
