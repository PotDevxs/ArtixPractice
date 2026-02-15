package dev.artixdev.libs.com.mongodb.connection;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.ReferenceCountedOpenSslClientContext;
import io.netty.handler.ssl.SslContext;
import dev.artixdev.libs.com.mongodb.annotations.Immutable;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

@Immutable
public final class NettyTransportSettings extends TransportSettings {
   private final EventLoopGroup eventLoopGroup;
   private final Class<? extends SocketChannel> socketChannelClass;
   private final ByteBufAllocator allocator;
   private final SslContext sslContext;

   static NettyTransportSettings.Builder builder() {
      return new NettyTransportSettings.Builder();
   }

   @Nullable
   public EventLoopGroup getEventLoopGroup() {
      return this.eventLoopGroup;
   }

   @Nullable
   public Class<? extends SocketChannel> getSocketChannelClass() {
      return this.socketChannelClass;
   }

   @Nullable
   public ByteBufAllocator getAllocator() {
      return this.allocator;
   }

   @Nullable
   public SslContext getSslContext() {
      return this.sslContext;
   }

   public String toString() {
      return "NettyTransportSettings{eventLoopGroup=" + this.eventLoopGroup + ", socketChannelClass=" + this.socketChannelClass + ", allocator=" + this.allocator + ", sslContext=" + this.sslContext + '}';
   }

   private NettyTransportSettings(NettyTransportSettings.Builder builder) {
      this.allocator = builder.allocator;
      this.socketChannelClass = builder.socketChannelClass;
      this.eventLoopGroup = builder.eventLoopGroup;
      this.sslContext = builder.sslContext;
   }

   // $FF: synthetic method
   NettyTransportSettings(NettyTransportSettings.Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder {
      private ByteBufAllocator allocator;
      private Class<? extends SocketChannel> socketChannelClass;
      private EventLoopGroup eventLoopGroup;
      private SslContext sslContext;

      private Builder() {
      }

      public NettyTransportSettings.Builder allocator(ByteBufAllocator allocator) {
         this.allocator = (ByteBufAllocator)Assertions.notNull("allocator", allocator);
         return this;
      }

      public NettyTransportSettings.Builder socketChannelClass(Class<? extends SocketChannel> socketChannelClass) {
         this.socketChannelClass = (Class)Assertions.notNull("socketChannelClass", socketChannelClass);
         return this;
      }

      public NettyTransportSettings.Builder eventLoopGroup(EventLoopGroup eventLoopGroup) {
         this.eventLoopGroup = (EventLoopGroup)Assertions.notNull("eventLoopGroup", eventLoopGroup);
         return this;
      }

      public NettyTransportSettings.Builder sslContext(SslContext sslContext) {
         this.sslContext = (SslContext)Assertions.notNull("sslContext", sslContext);
         Assertions.isTrueArgument("sslContext must be client-side", sslContext.isClient());
         Assertions.isTrueArgument("sslContext must use either SslProvider.JDK or SslProvider.OPENSSL TLS/SSL protocol provider", !(sslContext instanceof ReferenceCountedOpenSslClientContext));
         return this;
      }

      public NettyTransportSettings build() {
         return new NettyTransportSettings(this);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
