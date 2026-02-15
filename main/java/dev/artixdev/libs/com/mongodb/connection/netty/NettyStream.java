package dev.artixdev.libs.com.mongodb.connection.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.CompositeByteBuf;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.timeout.ReadTimeoutException;
import java.io.IOException;
import java.net.SocketAddress;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import dev.artixdev.libs.com.mongodb.MongoClientException;
import dev.artixdev.libs.com.mongodb.MongoException;
import dev.artixdev.libs.com.mongodb.MongoInternalException;
import dev.artixdev.libs.com.mongodb.MongoSocketException;
import dev.artixdev.libs.com.mongodb.MongoSocketOpenException;
import dev.artixdev.libs.com.mongodb.MongoSocketReadTimeoutException;
import dev.artixdev.libs.com.mongodb.ServerAddress;
import dev.artixdev.libs.com.mongodb.annotations.ThreadSafe;
import dev.artixdev.libs.com.mongodb.assertions.Assertions;
import dev.artixdev.libs.com.mongodb.connection.AsyncCompletionHandler;
import dev.artixdev.libs.com.mongodb.connection.SocketSettings;
import dev.artixdev.libs.com.mongodb.connection.SslSettings;
import dev.artixdev.libs.com.mongodb.connection.Stream;
import dev.artixdev.libs.com.mongodb.internal.Locks;
import dev.artixdev.libs.com.mongodb.internal.connection.SslHelper;
import dev.artixdev.libs.com.mongodb.internal.connection.netty.NettyByteBuf;
import dev.artixdev.libs.com.mongodb.internal.thread.InterruptionUtil;
import dev.artixdev.libs.com.mongodb.lang.Nullable;

final class NettyStream implements Stream {
   private static final byte NO_SCHEDULE_TIME = 0;
   private final ServerAddress address;
   private final SocketSettings settings;
   private final SslSettings sslSettings;
   private final EventLoopGroup workerGroup;
   private final Class<? extends SocketChannel> socketChannelClass;
   private final ByteBufAllocator allocator;
   @Nullable
   private final SslContext sslContext;
   private boolean isClosed;
   private volatile Channel channel;
   private final LinkedList<ByteBuf> pendingInboundBuffers = new LinkedList();
   private final Lock lock = new ReentrantLock();
   private NettyStream.PendingReader pendingReader;
   private Throwable pendingException;
   @Nullable
   private NettyStream.ReadTimeoutTask readTimeoutTask;
   private long readTimeoutMillis = 0L;

   NettyStream(ServerAddress address, SocketSettings settings, SslSettings sslSettings, EventLoopGroup workerGroup, Class<? extends SocketChannel> socketChannelClass, ByteBufAllocator allocator, @Nullable SslContext sslContext) {
      this.address = address;
      this.settings = settings;
      this.sslSettings = sslSettings;
      this.workerGroup = workerGroup;
      this.socketChannelClass = socketChannelClass;
      this.allocator = allocator;
      this.sslContext = sslContext;
   }

   public dev.artixdev.libs.org.bson.ByteBuf getBuffer(int size) {
      return new NettyByteBuf(this.allocator.buffer(size, size));
   }

   public void open() throws IOException {
      NettyStream.FutureAsyncCompletionHandler<Void> handler = new NettyStream.FutureAsyncCompletionHandler();
      this.openAsync(handler);
      handler.get();
   }

   public void openAsync(AsyncCompletionHandler<Void> handler) {
      LinkedList socketAddressQueue;
      try {
         socketAddressQueue = new LinkedList(this.address.getSocketAddresses());
      } catch (Throwable e) {
         handler.failed(e);
         return;
      }

      this.initializeChannel(handler, socketAddressQueue);
   }

   private void initializeChannel(AsyncCompletionHandler<Void> handler, Queue<SocketAddress> socketAddressQueue) {
      if (socketAddressQueue.isEmpty()) {
         handler.failed(new MongoSocketException("Exception opening socket", this.getAddress()));
      } else {
         SocketAddress nextAddress = (SocketAddress)socketAddressQueue.poll();
         Bootstrap bootstrap = new Bootstrap();
         bootstrap.group(this.workerGroup);
         bootstrap.channel(this.socketChannelClass);
         bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, this.settings.getConnectTimeout(TimeUnit.MILLISECONDS));
         bootstrap.option(ChannelOption.TCP_NODELAY, true);
         bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
         if (this.settings.getReceiveBufferSize() > 0) {
            bootstrap.option(ChannelOption.SO_RCVBUF, this.settings.getReceiveBufferSize());
         }

         if (this.settings.getSendBufferSize() > 0) {
            bootstrap.option(ChannelOption.SO_SNDBUF, this.settings.getSendBufferSize());
         }

         bootstrap.option(ChannelOption.ALLOCATOR, this.allocator);
         bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            public void initChannel(SocketChannel ch) {
               ChannelPipeline pipeline = ch.pipeline();
               if (NettyStream.this.sslSettings.isEnabled()) {
                  NettyStream.this.addSslHandler(ch);
               }

               int readTimeout = NettyStream.this.settings.getReadTimeout(TimeUnit.MILLISECONDS);
               if (readTimeout > 0) {
                  NettyStream.this.readTimeoutMillis = (long)readTimeout;
                  pipeline.addLast(new ChannelHandler[]{new ChannelInboundHandlerAdapter()});
                  NettyStream.this.readTimeoutTask = new NettyStream.ReadTimeoutTask(pipeline.lastContext());
               }

               pipeline.addLast(new ChannelHandler[]{NettyStream.this.new InboundBufferHandler()});
            }
         });
         ChannelFuture channelFuture = bootstrap.connect(nextAddress);
         channelFuture.addListener(new NettyStream.OpenChannelFutureListener(socketAddressQueue, channelFuture, handler));
      }

   }

   public void write(List<dev.artixdev.libs.org.bson.ByteBuf> buffers) throws IOException {
      NettyStream.FutureAsyncCompletionHandler<Void> future = new NettyStream.FutureAsyncCompletionHandler();
      this.writeAsync(buffers, future);
      future.get();
   }

   public dev.artixdev.libs.org.bson.ByteBuf read(int numBytes) throws IOException {
      return this.read(numBytes, 0);
   }

   public boolean supportsAdditionalTimeout() {
      return true;
   }

   public dev.artixdev.libs.org.bson.ByteBuf read(int numBytes, int additionalTimeoutMillis) throws IOException {
      Assertions.isTrueArgument("additionalTimeoutMillis must not be negative", additionalTimeoutMillis >= 0);
      NettyStream.FutureAsyncCompletionHandler<dev.artixdev.libs.org.bson.ByteBuf> future = new NettyStream.FutureAsyncCompletionHandler();
      this.readAsync(numBytes, future, combinedTimeout(this.readTimeoutMillis, additionalTimeoutMillis));
      return (dev.artixdev.libs.org.bson.ByteBuf)future.get();
   }

   public void writeAsync(List<dev.artixdev.libs.org.bson.ByteBuf> buffers, AsyncCompletionHandler<Void> handler) {
      CompositeByteBuf composite = PooledByteBufAllocator.DEFAULT.compositeBuffer();
      Iterator var4 = buffers.iterator();

      while(var4.hasNext()) {
         dev.artixdev.libs.org.bson.ByteBuf cur = (dev.artixdev.libs.org.bson.ByteBuf)var4.next();
         composite.addComponent(true, ((NettyByteBuf)cur).asByteBuf().retain());
      }

      this.channel.writeAndFlush(composite).addListener((future) -> {
         if (!future.isSuccess()) {
            handler.failed(future.cause());
         } else {
            handler.completed(null);
         }

      });
   }

   public void readAsync(int numBytes, AsyncCompletionHandler<dev.artixdev.libs.org.bson.ByteBuf> handler) {
      this.readAsync(numBytes, handler, this.readTimeoutMillis);
   }

   private void readAsync(int numBytes, AsyncCompletionHandler<dev.artixdev.libs.org.bson.ByteBuf> handler, long readTimeoutMillis) {
      dev.artixdev.libs.org.bson.ByteBuf buffer = null;
      Throwable exceptionResult = null;
      this.lock.lock();

      try {
         exceptionResult = this.pendingException;
         if (exceptionResult == null) {
            if (!this.hasBytesAvailable(numBytes)) {
               if (this.pendingReader == null) {
                  this.pendingReader = new NettyStream.PendingReader(numBytes, handler, scheduleReadTimeout(this.readTimeoutTask, readTimeoutMillis));
               }
            } else {
               CompositeByteBuf composite = this.allocator.compositeBuffer(this.pendingInboundBuffers.size());
               int bytesNeeded = numBytes;
               Iterator iter = this.pendingInboundBuffers.iterator();

               while(iter.hasNext()) {
                  ByteBuf next = (ByteBuf)iter.next();
                  int bytesNeededFromCurrentBuffer = Math.min(next.readableBytes(), bytesNeeded);
                  if (bytesNeededFromCurrentBuffer == next.readableBytes()) {
                     composite.addComponent(next);
                     iter.remove();
                  } else {
                     next.retain();
                     composite.addComponent(next.readSlice(bytesNeededFromCurrentBuffer));
                  }

                  composite.writerIndex(composite.writerIndex() + bytesNeededFromCurrentBuffer);
                  bytesNeeded -= bytesNeededFromCurrentBuffer;
                  if (bytesNeeded == 0) {
                     break;
                  }
               }

               buffer = (new NettyByteBuf(composite)).flip();
            }
         }

         if ((exceptionResult != null || buffer != null) && this.pendingReader != null) {
            cancel(this.pendingReader.timeout);
            this.pendingReader = null;
         }
      } finally {
         this.lock.unlock();
      }

      if (exceptionResult != null) {
         handler.failed(exceptionResult);
      }

      if (buffer != null) {
         handler.completed(buffer);
      }

   }

   private boolean hasBytesAvailable(int numBytes) {
      int bytesAvailable = 0;
      Iterator var3 = this.pendingInboundBuffers.iterator();

      do {
         if (!var3.hasNext()) {
            return false;
         }

         ByteBuf cur = (ByteBuf)var3.next();
         bytesAvailable += cur.readableBytes();
      } while(bytesAvailable < numBytes);

      return true;
   }

   private void handleReadResponse(@Nullable ByteBuf buffer, @Nullable Throwable t) {
      NettyStream.PendingReader localPendingReader = (NettyStream.PendingReader)Locks.withLock(this.lock, () -> {
         if (buffer != null) {
            this.pendingInboundBuffers.add(buffer.retain());
         } else {
            this.pendingException = t;
         }

         return this.pendingReader;
      });
      if (localPendingReader != null) {
         this.readAsync(localPendingReader.numBytes, localPendingReader.handler, 0L);
      }

   }

   public ServerAddress getAddress() {
      return this.address;
   }

   public void close() {
      Locks.withLock(this.lock, () -> {
         this.isClosed = true;
         if (this.channel != null) {
            this.channel.close();
            this.channel = null;
         }

         Iterator iterator = this.pendingInboundBuffers.iterator();

         while(iterator.hasNext()) {
            ByteBuf nextByteBuf = (ByteBuf)iterator.next();
            iterator.remove();
            nextByteBuf.release();
         }

      });
   }

   public boolean isClosed() {
      return this.isClosed;
   }

   public SocketSettings getSettings() {
      return this.settings;
   }

   public SslSettings getSslSettings() {
      return this.sslSettings;
   }

   public EventLoopGroup getWorkerGroup() {
      return this.workerGroup;
   }

   public Class<? extends SocketChannel> getSocketChannelClass() {
      return this.socketChannelClass;
   }

   public ByteBufAllocator getAllocator() {
      return this.allocator;
   }

   private void addSslHandler(SocketChannel channel) {
      SSLEngine engine;
      if (this.sslContext == null) {
         SSLContext sslContext;
         try {
            sslContext = (SSLContext)Optional.ofNullable(this.sslSettings.getContext()).orElse(SSLContext.getDefault());
         } catch (NoSuchAlgorithmException e) {
            throw new MongoClientException("Unable to create standard SSLContext", e);
         }

         engine = sslContext.createSSLEngine(this.address.getHost(), this.address.getPort());
      } else {
         engine = this.sslContext.newEngine(channel.alloc(), this.address.getHost(), this.address.getPort());
      }

      engine.setUseClientMode(true);
      SSLParameters sslParameters = engine.getSSLParameters();
      SslHelper.enableSni(this.address.getHost(), sslParameters);
      if (!this.sslSettings.isInvalidHostNameAllowed()) {
         SslHelper.enableHostNameVerification(sslParameters);
      }

      engine.setSSLParameters(sslParameters);
      channel.pipeline().addFirst("ssl", new SslHandler(engine, false));
   }

   private static void cancel(@Nullable Future<?> f) {
      if (f != null) {
         f.cancel(false);
      }

   }

   private static long combinedTimeout(long timeout, int additionalTimeout) {
      return timeout == 0L ? 0L : Math.addExact(timeout, (long)additionalTimeout);
   }

   @Nullable
   private static ScheduledFuture<?> scheduleReadTimeout(@Nullable NettyStream.ReadTimeoutTask readTimeoutTask, long timeoutMillis) {
      return timeoutMillis == 0L ? null : ((NettyStream.ReadTimeoutTask)Assertions.assertNotNull(readTimeoutTask)).schedule(timeoutMillis);
   }

   @ThreadSafe
   private static final class ReadTimeoutTask implements Runnable {
      private final ChannelHandlerContext ctx;

      private ReadTimeoutTask(ChannelHandlerContext timeoutChannelHandlerContext) {
         this.ctx = timeoutChannelHandlerContext;
      }

      public void run() {
         try {
            if (this.ctx.channel().isOpen()) {
               this.ctx.fireExceptionCaught(ReadTimeoutException.INSTANCE);
               this.ctx.close();
            }
         } catch (Throwable e) {
            this.ctx.fireExceptionCaught(e);
         }

      }

      private ScheduledFuture<?> schedule(long timeoutMillis) {
         return this.ctx.executor().schedule(this, timeoutMillis, TimeUnit.MILLISECONDS);
      }

      // $FF: synthetic method
      ReadTimeoutTask(ChannelHandlerContext x0, Object x1) {
         this(x0);
      }
   }

   private static final class FutureAsyncCompletionHandler<T> implements AsyncCompletionHandler<T> {
      private final CountDownLatch latch = new CountDownLatch(1);
      private volatile T t;
      private volatile Throwable throwable;

      FutureAsyncCompletionHandler() {
      }

      public void completed(@Nullable T t) {
         this.t = t;
         this.latch.countDown();
      }

      public void failed(Throwable t) {
         this.throwable = t;
         this.latch.countDown();
      }

      public T get() throws IOException {
         try {
            this.latch.await();
            if (this.throwable != null) {
               if (this.throwable instanceof IOException) {
                  throw (IOException)this.throwable;
               } else if (this.throwable instanceof MongoException) {
                  throw (MongoException)this.throwable;
               } else {
                  throw new MongoInternalException("Exception thrown from Netty Stream", this.throwable);
               }
            } else {
               return this.t;
            }
         } catch (InterruptedException e) {
            throw InterruptionUtil.interruptAndCreateMongoInterruptedException("Interrupted", e);
         }
      }
   }

   private class OpenChannelFutureListener implements ChannelFutureListener {
      private final Queue<SocketAddress> socketAddressQueue;
      private final ChannelFuture channelFuture;
      private final AsyncCompletionHandler<Void> handler;

      OpenChannelFutureListener(Queue<SocketAddress> socketAddressQueue, ChannelFuture channelFuture, AsyncCompletionHandler<Void> handler) {
         this.socketAddressQueue = socketAddressQueue;
         this.channelFuture = channelFuture;
         this.handler = handler;
      }

      public void operationComplete(ChannelFuture future) {
         Locks.withLock(NettyStream.this.lock, () -> {
            if (future.isSuccess()) {
               if (NettyStream.this.isClosed) {
                  this.channelFuture.channel().close();
               } else {
                  NettyStream.this.channel = this.channelFuture.channel();
                  NettyStream.this.channel.closeFuture().addListener((future1) -> {
                     NettyStream.this.handleReadResponse((ByteBuf)null, new IOException("The connection to the server was closed"));
                  });
               }

               this.handler.completed(null);
            } else if (NettyStream.this.isClosed) {
               this.handler.completed(null);
            } else if (this.socketAddressQueue.isEmpty()) {
               this.handler.failed(new MongoSocketOpenException("Exception opening socket", NettyStream.this.getAddress(), future.cause()));
            } else {
               NettyStream.this.initializeChannel(this.handler, this.socketAddressQueue);
            }

         });
      }
   }

   private static final class PendingReader {
      private final int numBytes;
      private final AsyncCompletionHandler<dev.artixdev.libs.org.bson.ByteBuf> handler;
      @Nullable
      private final ScheduledFuture<?> timeout;

      private PendingReader(int numBytes, AsyncCompletionHandler<dev.artixdev.libs.org.bson.ByteBuf> handler, @Nullable ScheduledFuture<?> timeout) {
         this.numBytes = numBytes;
         this.handler = handler;
         this.timeout = timeout;
      }

      // $FF: synthetic method
      PendingReader(int x0, AsyncCompletionHandler x1, ScheduledFuture x2, Object x3) {
         this(x0, x1, x2);
      }
   }

   private class InboundBufferHandler extends SimpleChannelInboundHandler<ByteBuf> {
      private InboundBufferHandler() {
      }

      protected void channelRead0(ChannelHandlerContext ctx, ByteBuf buffer) {
         NettyStream.this.handleReadResponse(buffer, (Throwable)null);
      }

      public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) {
         if (t instanceof ReadTimeoutException) {
            NettyStream.this.handleReadResponse((ByteBuf)null, new MongoSocketReadTimeoutException("Timeout while receiving message", NettyStream.this.address, t));
         } else {
            NettyStream.this.handleReadResponse((ByteBuf)null, t);
         }

         ctx.close();
      }

      // $FF: synthetic method
      InboundBufferHandler(Object x1) {
         this();
      }
   }
}
