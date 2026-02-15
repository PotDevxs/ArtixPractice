package dev.artixdev.libs.com.mongodb.connection.netty;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.ReferenceCountedOpenSslClientContext;
import io.netty.handler.ssl.SslContext;
import java.util.Objects;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.NettyTransportSettings;
import dev.artixdev.libs.com.mongodb.connection.SocketSettings;
import dev.artixdev.libs.com.mongodb.connection.SslSettings;
import dev.artixdev.libs.com.mongodb.connection.StreamFactory;
import dev.artixdev.libs.com.mongodb.connection.StreamFactoryFactory;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

/** @deprecated */
@Deprecated
public final class NettyStreamFactoryFactory implements StreamFactoryFactory {
   private final EventLoopGroup eventLoopGroup;
   private final Class<? extends SocketChannel> socketChannelClass;
   private final ByteBufAllocator allocator;
   @Nullable
   private final SslContext sslContext;

   public static NettyStreamFactoryFactory.Builder builder() {
      return new NettyStreamFactoryFactory.Builder();
   }

   EventLoopGroup getEventLoopGroup() {
      return this.eventLoopGroup;
   }

   Class<? extends SocketChannel> getSocketChannelClass() {
      return this.socketChannelClass;
   }

   ByteBufAllocator getAllocator() {
      return this.allocator;
   }

   @Nullable
   SslContext getSslContext() {
      return this.sslContext;
   }

   public StreamFactory create(SocketSettings socketSettings, SslSettings sslSettings) {
      return new NettyStreamFactory(socketSettings, sslSettings, this.eventLoopGroup, this.socketChannelClass, this.allocator, this.sslContext);
   }

   public boolean equals(Object o) {
      if (this == o) {
         return true;
      } else if (o != null && this.getClass() == o.getClass()) {
         NettyStreamFactoryFactory that = (NettyStreamFactoryFactory)o;
         return Objects.equals(this.eventLoopGroup, that.eventLoopGroup) && Objects.equals(this.socketChannelClass, that.socketChannelClass) && Objects.equals(this.allocator, that.allocator) && Objects.equals(this.sslContext, that.sslContext);
      } else {
         return false;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.eventLoopGroup, this.socketChannelClass, this.allocator, this.sslContext});
   }

   public String toString() {
      return "NettyStreamFactoryFactory{eventLoopGroup=" + this.eventLoopGroup + ", socketChannelClass=" + this.socketChannelClass + ", allocator=" + this.allocator + ", sslContext=" + this.sslContext + '}';
   }

   private NettyStreamFactoryFactory(NettyStreamFactoryFactory.Builder builder) {
      this.allocator = builder.allocator == null ? ByteBufAllocator.DEFAULT : builder.allocator;
      this.socketChannelClass = builder.socketChannelClass == null ? NioSocketChannel.class : builder.socketChannelClass;
      this.eventLoopGroup = (EventLoopGroup)(builder.eventLoopGroup == null ? new NioEventLoopGroup() : builder.eventLoopGroup);
      this.sslContext = builder.sslContext;
   }

   // $FF: synthetic method
   NettyStreamFactoryFactory(NettyStreamFactoryFactory.Builder x0, Object x1) {
      this(x0);
   }

   public static final class Builder {
      private ByteBufAllocator allocator;
      private Class<? extends SocketChannel> socketChannelClass;
      private EventLoopGroup eventLoopGroup;
      @Nullable
      private SslContext sslContext;

      private Builder() {
      }

      public NettyStreamFactoryFactory.Builder applySettings(NettyTransportSettings settings) {
         this.allocator = settings.getAllocator();
         this.eventLoopGroup = settings.getEventLoopGroup();
         this.sslContext = settings.getSslContext();
         this.socketChannelClass = settings.getSocketChannelClass();
         return this;
      }

      public NettyStreamFactoryFactory.Builder allocator(ByteBufAllocator allocator) {
         this.allocator = (ByteBufAllocator)Assertions.notNull("allocator", allocator);
         return this;
      }

      public NettyStreamFactoryFactory.Builder socketChannelClass(Class<? extends SocketChannel> socketChannelClass) {
         this.socketChannelClass = (Class)Assertions.notNull("socketChannelClass", socketChannelClass);
         return this;
      }

      public NettyStreamFactoryFactory.Builder eventLoopGroup(EventLoopGroup eventLoopGroup) {
         this.eventLoopGroup = (EventLoopGroup)Assertions.notNull("eventLoopGroup", eventLoopGroup);
         return this;
      }

      public NettyStreamFactoryFactory.Builder sslContext(SslContext sslContext) {
         this.sslContext = (SslContext)Assertions.notNull("sslContext", sslContext);
         Assertions.isTrueArgument("sslContext must be client-side", sslContext.isClient());
         Assertions.isTrueArgument("sslContext must use either SslProvider.JDK or SslProvider.OPENSSL TLS/SSL protocol provider", !(sslContext instanceof ReferenceCountedOpenSslClientContext));
         return this;
      }

      public NettyStreamFactoryFactory build() {
         return new NettyStreamFactoryFactory(this);
      }

      // $FF: synthetic method
      Builder(Object x0) {
         this();
      }
   }
}
