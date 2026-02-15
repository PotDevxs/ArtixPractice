package dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer;

public interface ByteBufAllocationOperator {
   Object wrappedBuffer(byte[] var1);

   Object copiedBuffer(byte[] var1);

   Object buffer();

   Object directBuffer();

   Object compositeBuffer();
}
