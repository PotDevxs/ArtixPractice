package dev.artixdev.libs.com.github.retrooper.packetevents.manager;

import java.net.InetSocketAddress;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketListenerAbstract;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketListenerPriority;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.ConnectionState;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTCompound;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.nbt.NBTList;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.PacketType;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.UserProfile;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.configuration.server.WrapperConfigServerRegistryData;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.handshaking.client.WrapperHandshakingClientHandshake;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.login.server.WrapperLoginServerLoginSuccess;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerJoinGame;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerRespawn;

public class InternalPacketListener extends PacketListenerAbstract {
   public InternalPacketListener() {
      this(PacketListenerPriority.LOWEST);
   }

   public InternalPacketListener(PacketListenerPriority priority) {
      super(priority);
   }

   public void onPacketSend(PacketSendEvent event) {
      User user = event.getUser();
      if (event.getPacketType() == PacketType.Login.Server.LOGIN_SUCCESS) {
         Object channel = event.getChannel();
         WrapperLoginServerLoginSuccess loginSuccess = new WrapperLoginServerLoginSuccess(event);
         UserProfile profile = loginSuccess.getUserProfile();
         user.getProfile().setUUID(profile.getUUID());
         user.getProfile().setName(profile.getName());
         user.getProfile().setTextureProperties(profile.getTextureProperties());
         synchronized(channel) {
            ProtocolManager.CHANNELS.put(profile.getUUID(), channel);
         }

         label74: {
            PacketEvents.getAPI().getLogManager().debug("Mapped player UUID with their channel.");
            boolean proxy = PacketEvents.getAPI().getInjector().isProxy();
            if (proxy) {
               if (event.getUser().getClientVersion().isNewerThanOrEquals(ClientVersion.V_1_20_2)) {
                  break label74;
               }
            } else if (event.getServerVersion().isNewerThanOrEquals(ServerVersion.V_1_20_2)) {
               break label74;
            }

            user.setConnectionState(ConnectionState.PLAY);
            return;
         }

         user.setEncoderState(ConnectionState.CONFIGURATION);
      } else {
         NBTCompound dimension;
         NBTList list;
         if (event.getPacketType() == PacketType.Configuration.Server.REGISTRY_DATA) {
            WrapperConfigServerRegistryData registryData = new WrapperConfigServerRegistryData(event);
            dimension = registryData.getRegistryData();
            if (dimension != null) {
               list = dimension.getCompoundTagOrNull("minecraft:dimension_type").getCompoundListTagOrNull("value");
               user.setWorldNBT(list);
            }
         } else {
            if (event.getPacketType() == PacketType.Play.Server.JOIN_GAME) {
               WrapperPlayServerJoinGame joinGame = new WrapperPlayServerJoinGame(event);
               user.setEntityId(joinGame.getEntityId());
               user.setDimension(joinGame.getDimension());
               if (event.getServerVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
                  return;
               }

               dimension = joinGame.getDimensionCodec();
               if (dimension != null) {
                  list = dimension.getCompoundTagOrNull("minecraft:dimension_type").getCompoundListTagOrNull("value");
                  user.setWorldNBT(list);
               }

               dimension = user.getWorldNBT(joinGame.getDimension().getDimensionName());
               if (dimension != null) {
                  NBTCompound worldNBT = dimension.getCompoundTagOrNull("element");
                  user.setMinWorldHeight(worldNBT.getNumberTagOrNull("min_y").getAsInt());
                  user.setTotalWorldHeight(worldNBT.getNumberTagOrNull("height").getAsInt());
               }
            } else if (event.getPacketType() == PacketType.Play.Server.RESPAWN) {
               WrapperPlayServerRespawn respawn = new WrapperPlayServerRespawn(event);
               user.setDimension(respawn.getDimension());
               if (event.getServerVersion().isOlderThanOrEquals(ServerVersion.V_1_16_5)) {
                  return;
               }

               dimension = user.getWorldNBT(respawn.getDimension().getDimensionName());
               if (dimension != null) {
                  dimension = dimension.getCompoundTagOrNull("element");
                  user.setMinWorldHeight(dimension.getNumberTagOrNull("min_y").getAsInt());
                  user.setTotalWorldHeight(dimension.getNumberTagOrNull("height").getAsInt());
               }
            } else if (event.getPacketType() == PacketType.Play.Server.CONFIGURATION_START) {
               user.setEncoderState(ConnectionState.CONFIGURATION);
            } else if (event.getPacketType() == PacketType.Configuration.Server.CONFIGURATION_END) {
               user.setEncoderState(ConnectionState.PLAY);
            }
         }
      }

   }

   public void onPacketReceive(PacketReceiveEvent event) {
      User user = event.getUser();
      if (event.getPacketType() == PacketType.Handshaking.Client.HANDSHAKE) {
         Object channel = event.getChannel();
         InetSocketAddress address = event.getSocketAddress();
         WrapperHandshakingClientHandshake handshake = new WrapperHandshakingClientHandshake(event);
         ConnectionState nextState = handshake.getNextConnectionState();
         ClientVersion clientVersion = handshake.getClientVersion();
         user.setClientVersion(clientVersion);
         PacketEvents.getAPI().getLogManager().debug("Processed " + address.getHostString() + ":" + address.getPort() + "'s client version. Client Version: " + clientVersion.getReleaseName());
         user.setConnectionState(nextState);
      } else if (event.getPacketType() == PacketType.Login.Client.LOGIN_SUCCESS_ACK) {
         user.setDecoderState(ConnectionState.CONFIGURATION);
      } else if (event.getPacketType() == PacketType.Play.Client.CONFIGURATION_ACK) {
         user.setDecoderState(ConnectionState.CONFIGURATION);
      } else if (event.getPacketType() == PacketType.Configuration.Client.CONFIGURATION_END_ACK) {
         user.setDecoderState(ConnectionState.PLAY);
      }

   }
}
