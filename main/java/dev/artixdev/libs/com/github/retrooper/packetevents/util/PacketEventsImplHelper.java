package dev.artixdev.libs.com.github.retrooper.packetevents.util;

import java.util.Iterator;
import java.util.UUID;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketReceiveEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.UserDisconnectEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.protocol.ProtocolManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class PacketEventsImplHelper {
   public static PacketSendEvent handleClientBoundPacket(Object channel, User user, Object player, Object buffer, boolean autoProtocolTranslation) throws Exception {
      if (!ByteBufHelper.isReadable(buffer)) {
         return null;
      } else {
         int preProcessIndex = ByteBufHelper.readerIndex(buffer);
         PacketSendEvent packetSendEvent = EventCreationUtil.createSendEvent(channel, user, player, buffer, autoProtocolTranslation);
         int processIndex = ByteBufHelper.readerIndex(buffer);
         PacketEvents.getAPI().getEventManager().callEvent(packetSendEvent, () -> {
            ByteBufHelper.readerIndex(buffer, processIndex);
         });
         if (!packetSendEvent.isCancelled()) {
            if (packetSendEvent.getLastUsedWrapper() != null) {
               ByteBufHelper.clear(buffer);
               packetSendEvent.getLastUsedWrapper().writeVarInt(packetSendEvent.getPacketId());
               packetSendEvent.getLastUsedWrapper().write();
            } else {
               ByteBufHelper.readerIndex(buffer, preProcessIndex);
            }
         } else {
            ByteBufHelper.clear(buffer);
         }

         if (packetSendEvent.hasPostTasks()) {
            Iterator var8 = packetSendEvent.getPostTasks().iterator();

            while(var8.hasNext()) {
               Runnable task = (Runnable)var8.next();
               task.run();
            }
         }

         return packetSendEvent;
      }
   }

   public static PacketReceiveEvent handleServerBoundPacket(Object channel, User user, Object player, Object buffer, boolean autoProtocolTranslation) throws Exception {
      if (!ByteBufHelper.isReadable(buffer)) {
         return null;
      } else {
         int preProcessIndex = ByteBufHelper.readerIndex(buffer);
         PacketReceiveEvent packetReceiveEvent = EventCreationUtil.createReceiveEvent(channel, user, player, buffer, autoProtocolTranslation);
         int processIndex = ByteBufHelper.readerIndex(buffer);
         PacketEvents.getAPI().getEventManager().callEvent(packetReceiveEvent, () -> {
            ByteBufHelper.readerIndex(buffer, processIndex);
         });
         if (!packetReceiveEvent.isCancelled()) {
            if (packetReceiveEvent.getLastUsedWrapper() != null) {
               ByteBufHelper.clear(buffer);
               packetReceiveEvent.getLastUsedWrapper().writeVarInt(packetReceiveEvent.getPacketId());
               packetReceiveEvent.getLastUsedWrapper().write();
            } else {
               ByteBufHelper.readerIndex(buffer, preProcessIndex);
            }
         } else {
            ByteBufHelper.clear(buffer);
         }

         if (packetReceiveEvent.hasPostTasks()) {
            Iterator var8 = packetReceiveEvent.getPostTasks().iterator();

            while(var8.hasNext()) {
               Runnable task = (Runnable)var8.next();
               task.run();
            }
         }

         return packetReceiveEvent;
      }
   }

   public static void handleDisconnection(Object channel, @Nullable UUID uuid) {
      synchronized(channel) {
         User user = PacketEvents.getAPI().getProtocolManager().getUser(channel);
         if (user != null) {
            UserDisconnectEvent disconnectEvent = new UserDisconnectEvent(user);
            PacketEvents.getAPI().getEventManager().callEvent(disconnectEvent);
            PacketEvents.getAPI().getProtocolManager().removeUser(user.getChannel());
         }

         if (uuid == null) {
            ProtocolManager.CHANNELS.entrySet().removeIf((pair) -> {
               return pair.getValue() == channel;
            });
         } else {
            ProtocolManager.CHANNELS.remove(uuid);
         }

      }
   }
}
