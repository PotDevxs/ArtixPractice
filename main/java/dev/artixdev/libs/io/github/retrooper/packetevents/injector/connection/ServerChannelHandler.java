package dev.artixdev.libs.io.github.retrooper.packetevents.injector.connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.Version;
import java.util.Map;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.PEVersion;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.SpigotReflectionUtil;

public class ServerChannelHandler extends ChannelInboundHandlerAdapter {
   public static boolean CHECKED_NETTY_VERSION;
   public static PEVersion NETTY_VERSION;
   public static final PEVersion MODERN_NETTY_VERSION = new PEVersion(new int[]{4, 1, 24});

   private static PEVersion resolveNettyVersion() {
      Map<String, Version> nettyArtifacts = Version.identify();
      Version version = (Version)nettyArtifacts.getOrDefault("netty-common", (Version)nettyArtifacts.get("netty-all"));
      if (version != null) {
         String stringVersion = version.artifactVersion();
         stringVersion = stringVersion.replaceAll("[^\\d.]", "");
         if (stringVersion.endsWith(".")) {
            stringVersion = stringVersion.substring(0, stringVersion.length() - 1);
         }

         return new PEVersion(stringVersion);
      } else {
         return null;
      }
   }

   public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      Channel channel = (Channel)msg;
      if (NETTY_VERSION == null && !CHECKED_NETTY_VERSION) {
         NETTY_VERSION = resolveNettyVersion();
         CHECKED_NETTY_VERSION = true;
      }

      if ((NETTY_VERSION == null || !NETTY_VERSION.isNewerThan(MODERN_NETTY_VERSION)) && !SpigotReflectionUtil.V_1_12_OR_HIGHER) {
         channel.pipeline().addFirst(PacketEvents.SERVER_CHANNEL_HANDLER_NAME, new PreChannelInitializer_v1_8());
      } else {
         channel.pipeline().addLast(PacketEvents.SERVER_CHANNEL_HANDLER_NAME, new PreChannelInitializer_v1_12());
      }

      super.channelRead(ctx, msg);
   }
}
