package dev.artixdev.libs.com.mongodb.connection.netty;

import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.SocketSettings;
import dev.artixdev.libs.com.mongodb.connection.SslSettings;
import dev.artixdev.libs.com.mongodb.connection.Stream;
import dev.artixdev.libs.com.mongodb.connection.StreamFactory;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

/** @deprecated */
@Deprecated
public class NettyStreamFactory implements StreamFactory {
   private final SocketSettings settings;
   private final SslSettings sslSettings;
   private final EventLoopGroup eventLoopGroup;
   private final Class<? extends SocketChannel> socketChannelClass;
   private final ByteBufAllocator allocator;
   @Nullable
   private final SslContext sslContext;

   public NettyStreamFactory(SocketSettings settings, SslSettings sslSettings, EventLoopGroup eventLoopGroup, Class<? extends SocketChannel> socketChannelClass, ByteBufAllocator allocator, @Nullable SslContext sslContext) {
      this.settings = (SocketSettings)Assertions.notNull("settings", settings);
      this.sslSettings = (SslSettings)Assertions.notNull("sslSettings", sslSettings);
      this.eventLoopGroup = (EventLoopGroup)Assertions.notNull("eventLoopGroup", eventLoopGroup);
      this.socketChannelClass = (Class)Assertions.notNull("socketChannelClass", socketChannelClass);
      this.allocator = (ByteBufAllocator)Assertions.notNull("allocator", allocator);
      this.sslContext = sslContext;
   }

   public NettyStreamFactory(SocketSettings settings, SslSettings sslSettings, EventLoopGroup eventLoopGroup, Class<? extends SocketChannel> socketChannelClass, ByteBufAllocator allocator) {
      this(settings, sslSettings, eventLoopGroup, socketChannelClass, allocator, (SslContext)null);
   }

   public NettyStreamFactory(SocketSettings settings, SslSettings sslSettings, EventLoopGroup eventLoopGroup, ByteBufAllocator allocator) {
      this(settings, sslSettings, eventLoopGroup, NioSocketChannel.class, allocator);
   }

   public NettyStreamFactory(SocketSettings settings, SslSettings sslSettings, EventLoopGroup eventLoopGroup) {
      this(settings, sslSettings, eventLoopGroup, PooledByteBufAllocator.DEFAULT);
   }

   public NettyStreamFactory(SocketSettings settings, SslSettings sslSettings) {
      this(settings, sslSettings, new NioEventLoopGroup());
   }

   public Stream create(ServerAddress serverAddress) {
      return new NettyStream(serverAddress, this.settings, this.sslSettings, this.eventLoopGroup, this.socketChannelClass, this.allocator, this.sslContext);
   }
}
