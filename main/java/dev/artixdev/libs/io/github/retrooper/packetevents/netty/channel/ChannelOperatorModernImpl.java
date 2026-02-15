package dev.artixdev.libs.io.github.retrooper.packetevents.netty.channel;

import io.netty.channel.Channel;
import java.net.SocketAddress;
import java.util.List;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.channel.ChannelOperator;

public class ChannelOperatorModernImpl implements ChannelOperator {
   public SocketAddress remoteAddress(Object channel) {
      return ((Channel)channel).remoteAddress();
   }

   public SocketAddress localAddress(Object channel) {
      return ((Channel)channel).localAddress();
   }

   public boolean isOpen(Object channel) {
      return ((Channel)channel).isOpen();
   }

   public Object close(Object channel) {
      return ((Channel)channel).close();
   }

   public Object write(Object channel, Object buffer) {
      return ((Channel)channel).write(buffer);
   }

   public Object flush(Object channel) {
      return ((Channel)channel).flush();
   }

   public Object writeAndFlush(Object channel, Object buffer) {
      return ((Channel)channel).writeAndFlush(buffer);
   }

   public Object fireChannelRead(Object channel, Object buffer) {
      return ((Channel)channel).pipeline().fireChannelRead(buffer);
   }

   public Object writeInContext(Object channel, String ctx, Object buffer) {
      return ((Channel)channel).pipeline().context(ctx).write(buffer);
   }

   public Object flushInContext(Object channel, String ctx) {
      return ((Channel)channel).pipeline().context(ctx).flush();
   }

   public Object writeAndFlushInContext(Object channel, String ctx, Object buffer) {
      return ((Channel)channel).pipeline().context(ctx).writeAndFlush(buffer);
   }

   public Object getPipeline(Object channel) {
      return ((Channel)channel).pipeline();
   }

   public Object fireChannelReadInContext(Object channel, String ctx, Object buffer) {
      return ((Channel)channel).pipeline().context(ctx).fireChannelRead(buffer);
   }

   public List<String> pipelineHandlerNames(Object channel) {
      return ((Channel)channel).pipeline().names();
   }

   public Object getPipelineHandler(Object channel, String name) {
      return ((Channel)channel).pipeline().get(name);
   }

   public Object getPipelineContext(Object channel, String name) {
      return ((Channel)channel).pipeline().context(name);
   }

   public void runInEventLoop(Object channel, Runnable runnable) {
      ((Channel)channel).eventLoop().execute(runnable);
   }

   public Object pooledByteBuf(Object o) {
      return ((Channel)o).alloc().buffer();
   }
}
