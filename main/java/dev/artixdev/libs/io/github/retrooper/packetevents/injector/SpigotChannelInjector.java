package dev.artixdev.libs.io.github.retrooper.packetevents.injector;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelPipeline;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.bukkit.entity.Player;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.UserLoginEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.injector.ChannelInjector;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.ConnectionState;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.reflection.ReflectionObject;
import dev.artixdev.libs.io.github.retrooper.packetevents.injector.connection.ServerChannelHandler;
import dev.artixdev.libs.io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import dev.artixdev.libs.io.github.retrooper.packetevents.injector.handlers.PacketEventsDecoder;
import dev.artixdev.libs.io.github.retrooper.packetevents.injector.handlers.PacketEventsEncoder;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.InjectedList;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.SpigotReflectionUtil;

public class SpigotChannelInjector implements ChannelInjector {
   public final Set<Channel> injectedConnectionChannels = new HashSet();
   public List<Object> networkManagers;
   private int connectionChannelsListIndex = -1;
   public boolean inboundAheadProtocolTranslation = false;
   public boolean outboundAheadProtocolTranslation = false;

   public void updatePlayer(User user, Object player) {
      PacketEvents.getAPI().getEventManager().callEvent(new UserLoginEvent(user, player));
      Object channel = user.getChannel();
      if (channel == null) {
         channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
      }

      this.setPlayer(channel, player);
   }

   public boolean isServerBound() {
      Object serverConnection = SpigotReflectionUtil.getMinecraftServerConnectionInstance();
      if (serverConnection != null) {
         ReflectionObject reflectServerConnection = new ReflectionObject(serverConnection);

         for(int i = 0; i < 2; ++i) {
            List<?> list = reflectServerConnection.readList(i);
            Iterator var5 = list.iterator();

            while(var5.hasNext()) {
               Object value = var5.next();
               if (value instanceof ChannelFuture) {
                  this.connectionChannelsListIndex = i;
                  return true;
               }
            }
         }
      }

      return false;
   }

   public void inject() {
      Object serverConnection = SpigotReflectionUtil.getMinecraftServerConnectionInstance();
      if (serverConnection != null) {
         ReflectionObject reflectServerConnection = new ReflectionObject(serverConnection);
         List<ChannelFuture> connectionChannelFutures = reflectServerConnection.readList(this.connectionChannelsListIndex);
         InjectedList<ChannelFuture> wrappedList = new InjectedList<ChannelFuture>(connectionChannelFutures, (future) -> {
            Channel channel = future.channel();
            this.injectServerChannel(channel);
            this.injectedConnectionChannels.add(channel);
         });
         reflectServerConnection.writeList(this.connectionChannelsListIndex, wrappedList);
         if (this.networkManagers == null) {
            this.networkManagers = SpigotReflectionUtil.getNetworkManagers();
         }

         synchronized(this.networkManagers) {
            if (!this.networkManagers.isEmpty()) {
               PacketEvents.getAPI().getLogManager().debug("Late bind not enabled, injecting into existing channel");
            }

            Iterator var6 = this.networkManagers.iterator();

            while(var6.hasNext()) {
               Object networkManager = var6.next();
               ReflectionObject networkManagerWrapper = new ReflectionObject(networkManager);
               Channel channel = (Channel)networkManagerWrapper.readObject(0, Channel.class);
               if (channel != null) {
                  try {
                     ServerConnectionInitializer.initChannel(channel, ConnectionState.PLAY);
                  } catch (Exception e) {
                     System.out.println("Spigot injector failed to inject into an existing channel.");
                     e.printStackTrace();
                  }
               }
            }
         }
      }

   }

   public void uninject() {
      Iterator var1 = this.injectedConnectionChannels.iterator();

      while(var1.hasNext()) {
         Channel connectionChannel = (Channel)var1.next();
         this.uninjectServerChannel(connectionChannel);
      }

      this.injectedConnectionChannels.clear();
      Object serverConnection = SpigotReflectionUtil.getMinecraftServerConnectionInstance();
      if (serverConnection != null) {
         ReflectionObject reflectServerConnection = new ReflectionObject(serverConnection);
         List<ChannelFuture> connectionChannelFutures = reflectServerConnection.readList(this.connectionChannelsListIndex);
         if (connectionChannelFutures instanceof InjectedList) {
            reflectServerConnection.writeList(this.connectionChannelsListIndex, ((InjectedList)connectionChannelFutures).originalList());
         }
      }

   }

   private void injectServerChannel(Channel serverChannel) {
      ChannelPipeline pipeline = serverChannel.pipeline();
      ChannelHandler connectionHandler = pipeline.get(PacketEvents.CONNECTION_HANDLER_NAME);
      if (connectionHandler != null) {
         pipeline.remove(PacketEvents.CONNECTION_HANDLER_NAME);
      }

      if (pipeline.get("SpigotNettyServerChannelHandler#0") != null) {
         pipeline.addAfter("SpigotNettyServerChannelHandler#0", PacketEvents.CONNECTION_HANDLER_NAME, new ServerChannelHandler());
      } else if (pipeline.get("floodgate-init") != null) {
         pipeline.addAfter("floodgate-init", PacketEvents.CONNECTION_HANDLER_NAME, new ServerChannelHandler());
      } else if (pipeline.get("MinecraftPipeline#0") != null) {
         pipeline.addAfter("MinecraftPipeline#0", PacketEvents.CONNECTION_HANDLER_NAME, new ServerChannelHandler());
      } else {
         pipeline.addFirst(PacketEvents.CONNECTION_HANDLER_NAME, new ServerChannelHandler());
      }

      if (this.networkManagers == null) {
         this.networkManagers = SpigotReflectionUtil.getNetworkManagers();
      }

      synchronized(this.networkManagers) {
         Iterator var5 = this.networkManagers.iterator();

         while(var5.hasNext()) {
            Object networkManager = var5.next();
            ReflectionObject networkManagerWrapper = new ReflectionObject(networkManager);
            Channel channel = (Channel)networkManagerWrapper.readObject(0, Channel.class);
            if (channel != null && channel.isOpen() && channel.localAddress().equals(serverChannel.localAddress())) {
               channel.close();
            }
         }

      }
   }

   private void uninjectServerChannel(Channel serverChannel) {
      if (serverChannel.pipeline().get(PacketEvents.CONNECTION_HANDLER_NAME) != null) {
         serverChannel.pipeline().remove(PacketEvents.CONNECTION_HANDLER_NAME);
      } else {
         PacketEvents.getAPI().getLogManager().warn("Failed to uninject server channel, handler not found");
      }

   }

   public void updateUser(Object channel, User user) {
      PacketEventsEncoder encoder = this.getEncoder((Channel)channel);
      if (encoder != null) {
         encoder.user = user;
      }

      PacketEventsDecoder decoder = this.getDecoder((Channel)channel);
      if (decoder != null) {
         decoder.user = user;
      }

   }

   public void setPlayer(Object channel, Object player) {
      PacketEventsEncoder encoder = this.getEncoder((Channel)channel);
      if (encoder != null) {
         encoder.player = (Player)player;
      }

      PacketEventsDecoder decoder = this.getDecoder((Channel)channel);
      if (decoder != null) {
         decoder.player = (Player)player;
         decoder.user.getProfile().setName(((Player)player).getName());
         decoder.user.getProfile().setUUID(((Player)player).getUniqueId());
      }

   }

   private PacketEventsEncoder getEncoder(Channel channel) {
      return (PacketEventsEncoder)channel.pipeline().get(PacketEvents.ENCODER_NAME);
   }

   private PacketEventsDecoder getDecoder(Channel channel) {
      return (PacketEventsDecoder)channel.pipeline().get(PacketEvents.DECODER_NAME);
   }

   public boolean isProxy() {
      return false;
   }
}
