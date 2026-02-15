package dev.artixdev.libs.io.github.retrooper.packetevents.injector.handlers;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageEncoder;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;
import org.bukkit.entity.Player;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketSendEvent;
import dev.artixdev.libs.com.github.retrooper.packetevents.exception.CancelPacketException;
import dev.artixdev.libs.com.github.retrooper.packetevents.exception.InvalidDisconnectPacketSend;
import dev.artixdev.libs.com.github.retrooper.packetevents.exception.PacketProcessException;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer.ByteBufHelper;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.ConnectionState;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.ExceptionUtil;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.PacketEventsImplHelper;
import dev.artixdev.libs.io.github.retrooper.packetevents.injector.connection.ServerConnectionInitializer;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.viaversion.CustomPipelineUtil;

public class PacketEventsEncoder extends MessageToMessageEncoder<ByteBuf> {
   public User user;
   public Player player;
   private boolean handledCompression;
   private ChannelPromise promise;
   public static final Object COMPRESSION_ENABLED_EVENT = paperCompressionEnabledEvent();

   public PacketEventsEncoder(User user) {
      this.handledCompression = COMPRESSION_ENABLED_EVENT != null;
      this.user = user;
   }

   public PacketEventsEncoder(ChannelHandler encoder) {
      this.handledCompression = COMPRESSION_ENABLED_EVENT != null;
      this.user = ((PacketEventsEncoder)encoder).user;
      this.player = ((PacketEventsEncoder)encoder).player;
      this.handledCompression = ((PacketEventsEncoder)encoder).handledCompression;
      this.promise = ((PacketEventsEncoder)encoder).promise;
   }

   protected void encode(ChannelHandlerContext ctx, ByteBuf byteBuf, List<Object> list) throws Exception {
      boolean needsRecompression = !this.handledCompression && this.handleCompression(ctx, byteBuf);
      this.handleClientBoundPacket(ctx.channel(), this.user, this.player, byteBuf, this.promise);
      if (needsRecompression) {
         this.compress(ctx, byteBuf);
      }

      if (!ByteBufHelper.isReadable(byteBuf)) {
         throw CancelPacketException.INSTANCE;
      } else {
         list.add(byteBuf.retain());
      }
   }

   private PacketSendEvent handleClientBoundPacket(Channel channel, User user, Object player, ByteBuf buffer, ChannelPromise promise) throws Exception {
      PacketSendEvent packetSendEvent = PacketEventsImplHelper.handleClientBoundPacket(channel, user, player, buffer, true);
      if (packetSendEvent.hasTasksAfterSend()) {
         promise.addListener((p) -> {
            Iterator var2 = packetSendEvent.getTasksAfterSend().iterator();

            while(var2.hasNext()) {
               Runnable task = (Runnable)var2.next();
               task.run();
            }

         });
      }

      return packetSendEvent;
   }

   public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
      ChannelPromise oldPromise = this.promise != null && !this.promise.isSuccess() ? this.promise : null;
      promise.addListener((p) -> {
         this.promise = oldPromise;
      });
      this.promise = promise;
      super.write(ctx, msg, promise);
   }

   public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
      if (!ExceptionUtil.isException(cause, CancelPacketException.class)) {
         if (!ExceptionUtil.isException(cause, InvalidDisconnectPacketSend.class)) {
            boolean didWeCauseThis = ExceptionUtil.isException(cause, PacketProcessException.class);
            if (didWeCauseThis && this.user != null && this.user.getEncoderState() != ConnectionState.HANDSHAKING) {
               cause.printStackTrace();
            } else {
               super.exceptionCaught(ctx, cause);
            }
         }
      }
   }

   private static Object paperCompressionEnabledEvent() {
      try {
         Class<?> eventClass = Class.forName("io.papermc.paper.network.ConnectionEvent");
         return eventClass.getDeclaredField("COMPRESSION_THRESHOLD_SET").get((Object)null);
      } catch (ReflectiveOperationException ignored) {
         return null;
      }
   }

   private void compress(ChannelHandlerContext ctx, ByteBuf input) throws InvocationTargetException {
      ChannelHandler compressor = ctx.pipeline().get("compress");
      ByteBuf temp = ctx.alloc().buffer();

      try {
         if (compressor != null) {
            CustomPipelineUtil.callEncode(compressor, ctx, input, temp);
         }
      } finally {
         input.clear().writeBytes(temp);
         temp.release();
      }

   }

   private void decompress(ChannelHandlerContext ctx, ByteBuf input, ByteBuf output) throws InvocationTargetException {
      ChannelHandler decompressor = ctx.pipeline().get("decompress");
      if (decompressor != null) {
         ByteBuf temp = (ByteBuf)CustomPipelineUtil.callDecode(decompressor, ctx, input).get(0);

         try {
            output.clear().writeBytes(temp);
         } finally {
            temp.release();
         }
      }

   }

   private boolean handleCompression(ChannelHandlerContext ctx, ByteBuf buffer) throws InvocationTargetException {
      if (this.handledCompression) {
         return false;
      } else {
         int compressIndex = ctx.pipeline().names().indexOf("compress");
         if (compressIndex == -1) {
            return false;
         } else {
            this.handledCompression = true;
            int peEncoderIndex = ctx.pipeline().names().indexOf(PacketEvents.ENCODER_NAME);
            if (peEncoderIndex == -1) {
               return false;
            } else if (compressIndex > peEncoderIndex) {
               this.decompress(ctx, buffer, buffer);
               PacketEventsDecoder decoder = (PacketEventsDecoder)ctx.pipeline().get(PacketEvents.DECODER_NAME);
               ServerConnectionInitializer.relocateHandlers(ctx.channel(), decoder, this.user);
               return true;
            } else {
               return false;
            }
         }
      }
   }
}
