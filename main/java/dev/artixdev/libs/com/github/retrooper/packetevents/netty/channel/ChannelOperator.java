package dev.artixdev.libs.com.github.retrooper.packetevents.netty.channel;

import java.net.SocketAddress;
import java.util.List;

public interface ChannelOperator {
   SocketAddress remoteAddress(Object var1);

   SocketAddress localAddress(Object var1);

   boolean isOpen(Object var1);

   Object close(Object var1);

   Object write(Object var1, Object var2);

   Object flush(Object var1);

   Object writeAndFlush(Object var1, Object var2);

   Object fireChannelRead(Object var1, Object var2);

   Object writeInContext(Object var1, String var2, Object var3);

   Object flushInContext(Object var1, String var2);

   Object writeAndFlushInContext(Object var1, String var2, Object var3);

   Object fireChannelReadInContext(Object var1, String var2, Object var3);

   List<String> pipelineHandlerNames(Object var1);

   Object getPipelineHandler(Object var1, String var2);

   Object getPipelineContext(Object var1, String var2);

   Object getPipeline(Object var1);

   void runInEventLoop(Object var1, Runnable var2);

   Object pooledByteBuf(Object var1);
}
