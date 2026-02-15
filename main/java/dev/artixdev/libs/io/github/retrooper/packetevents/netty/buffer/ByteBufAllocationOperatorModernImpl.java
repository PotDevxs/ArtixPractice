package dev.artixdev.libs.io.github.retrooper.packetevents.netty.buffer;

import io.netty.buffer.Unpooled;
import dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer.ByteBufAllocationOperator;

public class ByteBufAllocationOperatorModernImpl implements ByteBufAllocationOperator {
   public Object wrappedBuffer(byte[] bytes) {
      return Unpooled.wrappedBuffer(bytes);
   }

   public Object copiedBuffer(byte[] bytes) {
      return Unpooled.copiedBuffer(bytes);
   }

   public Object buffer() {
      return Unpooled.buffer();
   }

   public Object directBuffer() {
      return Unpooled.directBuffer();
   }

   public Object compositeBuffer() {
      return Unpooled.compositeBuffer();
   }
}
