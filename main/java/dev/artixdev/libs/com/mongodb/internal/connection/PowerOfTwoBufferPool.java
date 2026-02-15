package dev.artixdev.libs.com.mongodb.internal.connection;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import dev.artixdev.libs.com.mongodb.connection.BufferProvider;
import dev.artixdev.libs.com.mongodb.internal.thread.DaemonThreadFactory;
import dev.artixdev.libs.org.bson.ByteBuf;
import dev.artixdev.libs.org.bson.ByteBufNIO;

public class PowerOfTwoBufferPool implements BufferProvider {
   public static final PowerOfTwoBufferPool DEFAULT = (new PowerOfTwoBufferPool()).enablePruning();
   private final Map<Integer, PowerOfTwoBufferPool.BufferPool> powerOfTwoToPoolMap;
   private final long maxIdleTimeNanos;
   private final ScheduledExecutorService pruner;

   PowerOfTwoBufferPool() {
      this(24);
   }

   PowerOfTwoBufferPool(int highestPowerOfTwo) {
      this(highestPowerOfTwo, 1L, TimeUnit.MINUTES);
   }

   PowerOfTwoBufferPool(int highestPowerOfTwo, long maxIdleTime, TimeUnit timeUnit) {
      this.powerOfTwoToPoolMap = new HashMap();
      int powerOfTwo = 1;

      for(int i = 0; i <= highestPowerOfTwo; ++i) {
         this.powerOfTwoToPoolMap.put(i, new PowerOfTwoBufferPool.BufferPool(powerOfTwo));
         powerOfTwo <<= 1;
      }

      this.maxIdleTimeNanos = timeUnit.toNanos(maxIdleTime);
      this.pruner = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory("BufferPoolPruner"));
   }

   PowerOfTwoBufferPool enablePruning() {
      this.pruner.scheduleAtFixedRate(this::prune, this.maxIdleTimeNanos, this.maxIdleTimeNanos / 2L, TimeUnit.NANOSECONDS);
      return this;
   }

   void disablePruning() {
      this.pruner.shutdownNow();
   }

   public ByteBuf getBuffer(int size) {
      return new PowerOfTwoBufferPool.PooledByteBufNIO(this.getByteBuffer(size));
   }

   public ByteBuffer getByteBuffer(int size) {
      PowerOfTwoBufferPool.BufferPool pool = (PowerOfTwoBufferPool.BufferPool)this.powerOfTwoToPoolMap.get(log2(roundUpToNextHighestPowerOfTwo(size)));
      ByteBuffer byteBuffer = pool == null ? this.createNew(size) : pool.get().getBuffer();
      byteBuffer.clear();
      byteBuffer.limit(size);
      return byteBuffer;
   }

   private ByteBuffer createNew(int size) {
      ByteBuffer buf = ByteBuffer.allocate(size);
      buf.order(ByteOrder.LITTLE_ENDIAN);
      return buf;
   }

   public void release(ByteBuffer buffer) {
      PowerOfTwoBufferPool.BufferPool pool = (PowerOfTwoBufferPool.BufferPool)this.powerOfTwoToPoolMap.get(log2(roundUpToNextHighestPowerOfTwo(buffer.capacity())));
      if (pool != null) {
         pool.release(new PowerOfTwoBufferPool.IdleTrackingByteBuffer(buffer));
      }

   }

   private void prune() {
      this.powerOfTwoToPoolMap.values().forEach(PowerOfTwoBufferPool.BufferPool::prune);
   }

   static int log2(int powerOfTwo) {
      return 31 - Integer.numberOfLeadingZeros(powerOfTwo);
   }

   static int roundUpToNextHighestPowerOfTwo(int size) {
      int v = size - 1;
      v |= v >> 1;
      v |= v >> 2;
      v |= v >> 4;
      v |= v >> 8;
      v |= v >> 16;
      ++v;
      return v;
   }

   private final class BufferPool {
      private final int bufferSize;
      private final ConcurrentLinkedDeque<PowerOfTwoBufferPool.IdleTrackingByteBuffer> available = new ConcurrentLinkedDeque();

      BufferPool(int bufferSize) {
         this.bufferSize = bufferSize;
      }

      PowerOfTwoBufferPool.IdleTrackingByteBuffer get() {
         PowerOfTwoBufferPool.IdleTrackingByteBuffer buffer = (PowerOfTwoBufferPool.IdleTrackingByteBuffer)this.available.pollLast();
         return buffer != null ? buffer : new PowerOfTwoBufferPool.IdleTrackingByteBuffer(PowerOfTwoBufferPool.this.createNew(this.bufferSize));
      }

      void release(PowerOfTwoBufferPool.IdleTrackingByteBuffer t) {
         this.available.addLast(t);
      }

      void prune() {
         long now = System.nanoTime();
         this.available.removeIf((cur) -> {
            return now - cur.getLastUsedNanos() >= PowerOfTwoBufferPool.this.maxIdleTimeNanos;
         });
      }
   }

   private class PooledByteBufNIO extends ByteBufNIO {
      PooledByteBufNIO(ByteBuffer buf) {
         super(buf);
      }

      public void release() {
         ByteBuffer wrapped = this.asNIO();
         super.release();
         if (this.getReferenceCount() == 0) {
            PowerOfTwoBufferPool.this.release(wrapped);
         }

      }
   }

   private static final class IdleTrackingByteBuffer {
      private final long lastUsedNanos;
      private final ByteBuffer buffer;

      private IdleTrackingByteBuffer(ByteBuffer buffer) {
         this.lastUsedNanos = System.nanoTime();
         this.buffer = buffer;
      }

      public long getLastUsedNanos() {
         return this.lastUsedNanos;
      }

      public ByteBuffer getBuffer() {
         return this.buffer;
      }

      // $FF: synthetic method
      IdleTrackingByteBuffer(ByteBuffer x0, Object x1) {
         this(x0);
      }
   }
}
