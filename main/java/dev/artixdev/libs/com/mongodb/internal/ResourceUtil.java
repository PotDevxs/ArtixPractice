package dev.artixdev.libs.com.mongodb.internal;

import dev.artixdev.libs.org.bson.ByteBuf;

public final class ResourceUtil {
   public static void release(Iterable<? extends ByteBuf> buffers) {
      buffers.forEach((buffer) -> {
         if (buffer != null) {
            buffer.release();
         }

      });
   }

   private ResourceUtil() {
   }
}
