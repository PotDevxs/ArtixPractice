package dev.artixdev.libs.com.github.retrooper.packetevents.netty.channel;

import java.net.SocketAddress;
import java.util.Arrays;
import java.util.List;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;

public class ChannelHelper {
   public static SocketAddress remoteAddress(Object channel) {
      return PacketEvents.getAPI().getNettyManager().getChannelOperator().remoteAddress(channel);
   }

   public static SocketAddress localAddress(Object channel) {
      return PacketEvents.getAPI().getNettyManager().getChannelOperator().localAddress(channel);
   }

   public static boolean isOpen(Object channel) {
      return PacketEvents.getAPI().getNettyManager().getChannelOperator().isOpen(channel);
   }

   public static Object close(Object channel) {
      return PacketEvents.getAPI().getNettyManager().getChannelOperator().close(channel);
   }

   public static Object write(Object channel, Object buffer) {
      return PacketEvents.getAPI().getNettyManager().getChannelOperator().write(channel, buffer);
   }

   public static Object flush(Object channel) {
      return PacketEvents.getAPI().getNettyManager().getChannelOperator().flush(channel);
   }

   public static Object writeAndFlush(Object channel, Object buffer) {
      return PacketEvents.getAPI().getNettyManager().getChannelOperator().writeAndFlush(channel, buffer);
   }

   public static Object fireChannelRead(Object channel, Object buffer) {
      return PacketEvents.getAPI().getNettyManager().getChannelOperator().fireChannelRead(channel, buffer);
   }

   public static Object writeInContext(Object channel, String ctx, Object buffer) {
      return PacketEvents.getAPI().getNettyManager().getChannelOperator().writeInContext(channel, ctx, buffer);
   }

   public static Object flushInContext(Object channel, String ctx) {
      return PacketEvents.getAPI().getNettyManager().getChannelOperator().flushInContext(channel, ctx);
   }

   public static Object writeAndFlushInContext(Object channel, String ctx, Object buffer) {
      return PacketEvents.getAPI().getNettyManager().getChannelOperator().writeAndFlushInContext(channel, ctx, buffer);
   }

   public static Object fireChannelReadInContext(Object channel, String ctx, Object buffer) {
      return PacketEvents.getAPI().getNettyManager().getChannelOperator().fireChannelReadInContext(channel, ctx, buffer);
   }

   public static List<String> pipelineHandlerNames(Object channel) {
      return PacketEvents.getAPI().getNettyManager().getChannelOperator().pipelineHandlerNames(channel);
   }

   public static String pipelineHandlerNamesAsString(Object channel) {
      return Arrays.toString(pipelineHandlerNames(channel).toArray(new String[0]));
   }

   public static Object getPipeline(Object channel) {
      return PacketEvents.getAPI().getNettyManager().getChannelOperator().getPipeline(channel);
   }

   public static Object getPipelineHandler(Object channel, String name) {
      return PacketEvents.getAPI().getNettyManager().getChannelOperator().getPipelineHandler(channel, name);
   }

   public static Object getPipelineContext(Object channel, String handlerName) {
      return PacketEvents.getAPI().getNettyManager().getChannelOperator().getPipelineContext(channel, handlerName);
   }

   public static Object pooledByteBuf(Object channel) {
      return PacketEvents.getAPI().getNettyManager().getChannelOperator().pooledByteBuf(channel);
   }

   public static void runInEventLoop(Object channel, Runnable runnable) {
      PacketEvents.getAPI().getNettyManager().getChannelOperator().runInEventLoop(channel, runnable);
   }
}
