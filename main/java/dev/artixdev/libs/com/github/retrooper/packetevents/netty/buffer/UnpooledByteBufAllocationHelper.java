package dev.artixdev.libs.com.github.retrooper.packetevents.netty.buffer;

import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;

public class UnpooledByteBufAllocationHelper {
   public static Object wrappedBuffer(byte[] bytes) {
      return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().wrappedBuffer(bytes);
   }

   public static Object copiedBuffer(byte[] bytes) {
      return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().copiedBuffer(bytes);
   }

   public static Object buffer() {
      return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().buffer();
   }

   public static Object directBuffer() {
      return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().directBuffer();
   }

   public static Object compositeBuffer() {
      return PacketEvents.getAPI().getNettyManager().getByteBufAllocationOperator().compositeBuffer();
   }
}
