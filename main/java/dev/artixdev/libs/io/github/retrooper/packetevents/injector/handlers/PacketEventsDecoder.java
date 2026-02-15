package dev.artixdev.libs.io.github.retrooper.packetevents.injector.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;
import java.util.List;
import org.bukkit.entity.Player;
import dev.artixdev.libs.com.github.retrooper.packetevents.exception.PacketProcessException;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.ConnectionState;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.ExceptionUtil;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import dev.artixdev.libs.io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.SpigotReflectionUtil;

public class PacketEventsDecoder extends MessageToMessageDecoder<ByteBuf> {
   public User user;
   public Player player;
   public boolean hasBeenRelocated;

   public PacketEventsDecoder(User user) {
      this.user = user;
   }

   public PacketEventsDecoder(PacketEventsDecoder decoder) {
      this.user = decoder.user;
      this.player = decoder.player;
      this.hasBeenRelocated = decoder.hasBeenRelocated;
   }

   public void read(ChannelHandlerContext ctx, ByteBuf input, List<Object> out) throws Exception {
      PacketEventsImplHelper.handleServerBoundPacket(ctx.channel(), this.user, this.player, input, true);
      out.add(input.retain());
   }

   public void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
      if (buffer.isReadable()) {
         this.read(ctx, buffer, out);
      }

   }

   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      super.exceptionCaught(ctx, cause);
      if (ExceptionUtil.isException(cause, PacketProcessException.class) && !SpigotReflectionUtil.isMinecraftServerInstanceDebugging() && this.user != null && this.user.getDecoderState() != ConnectionState.HANDSHAKING) {
         cause.printStackTrace();
      }

   }

   public void userEventTriggered(ChannelHandlerContext ctx, Object event) throws Exception {
      if (PacketEventsEncoder.COMPRESSION_ENABLED_EVENT != null && event == PacketEventsEncoder.COMPRESSION_ENABLED_EVENT) {
         ServerConnectionInitializer.relocateHandlers(ctx.channel(), this, this.user);
         super.userEventTriggered(ctx, event);
      } else {
         super.userEventTriggered(ctx, event);
      }
   }
}
