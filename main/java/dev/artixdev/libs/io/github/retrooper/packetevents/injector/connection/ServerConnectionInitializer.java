package dev.artixdev.libs.io.github.retrooper.packetevents.injector.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import java.util.NoSuchElementException;
import java.util.UUID;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.UserConnectEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.channel.ChannelHelper;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.ConnectionState;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.UserProfile;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.FakeChannelUtil;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import dev.artixdev.libs.io.github.retrooper.packetevents.injector.handlers.PacketEventsDecoder;
import dev.artixdev.libs.io.github.retrooper.packetevents.injector.handlers.PacketEventsEncoder;

public class ServerConnectionInitializer {
   public static void initChannel(Object ch, ConnectionState connectionState) {
      Channel channel = (Channel)ch;
      if (!FakeChannelUtil.isFakeChannel(channel)) {
         User user = new User(channel, connectionState, (ClientVersion)null, new UserProfile((UUID)null, (String)null));
         if (connectionState == ConnectionState.PLAY) {
            user.setClientVersion(PacketEvents.getAPI().getServerManager().getVersion().toClientVersion());
            PacketEvents.getAPI().getLogManager().warn("Late injection detected, we missed packets so some functionality may break!");
         }

         synchronized(channel) {
            if (channel.pipeline().get("splitter") == null) {
               channel.close();
            } else {
               UserConnectEvent connectEvent = new UserConnectEvent(user);
               PacketEvents.getAPI().getEventManager().callEvent(connectEvent);
               if (connectEvent.isCancelled()) {
                  channel.unsafe().closeForcibly();
               } else {
                  relocateHandlers(channel, (PacketEventsDecoder)null, user);
                  channel.closeFuture().addListener((future) -> {
                     PacketEventsImplHelper.handleDisconnection(user.getChannel(), user.getUUID());
                  });
                  PacketEvents.getAPI().getProtocolManager().setUser(channel, user);
               }
            }
         }
      }
   }

   public static void destroyHandlers(Object ch) {
      Channel channel = (Channel)ch;
      if (channel.pipeline().get(PacketEvents.DECODER_NAME) != null) {
         channel.pipeline().remove(PacketEvents.DECODER_NAME);
      } else {
         PacketEvents.getAPI().getLogger().warning("Could not find decoder handler in channel pipeline!");
      }

      if (channel.pipeline().get(PacketEvents.ENCODER_NAME) != null) {
         channel.pipeline().remove(PacketEvents.ENCODER_NAME);
      } else {
         PacketEvents.getAPI().getLogger().warning("Could not find encoder handler in channel pipeline!");
      }

   }

   public static void relocateHandlers(Channel ctx, PacketEventsDecoder decoder, User user) {
      try {
         PacketEventsEncoder encoder;
         if (decoder != null) {
            if (decoder.hasBeenRelocated) {
               return;
            }

            decoder.hasBeenRelocated = true;
            decoder = (PacketEventsDecoder)ctx.pipeline().remove(PacketEvents.DECODER_NAME);
            ChannelHandler encoderHandler = ctx.pipeline().remove(PacketEvents.ENCODER_NAME);
            decoder = new PacketEventsDecoder(decoder);
            encoder = new PacketEventsEncoder(encoderHandler);
         } else {
            encoder = new PacketEventsEncoder(user);
            decoder = new PacketEventsDecoder(user);
         }

         ctx.pipeline().addBefore("decoder", PacketEvents.DECODER_NAME, decoder);
         ctx.pipeline().addBefore("encoder", PacketEvents.ENCODER_NAME, encoder);
      } catch (NoSuchElementException e) {
         String handlers = ChannelHelper.pipelineHandlerNamesAsString(ctx);
         throw new IllegalStateException("PacketEvents failed to add a decoder to the netty pipeline. Pipeline handlers: " + handlers, e);
      }
   }
}
