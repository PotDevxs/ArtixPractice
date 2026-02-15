package dev.artixdev.libs.com.github.retrooper.packetevents;

import dev.artixdev.libs.org.jetbrains.annotations.ApiStatus;

public final class PacketEvents {
   private static PacketEventsAPI<?> API;
   @ApiStatus.Internal
   public static String IDENTIFIER;
   @ApiStatus.Internal
   public static String ENCODER_NAME;
   @ApiStatus.Internal
   public static String DECODER_NAME;
   @ApiStatus.Internal
   public static String CONNECTION_HANDLER_NAME;
   @ApiStatus.Internal
   public static String SERVER_CHANNEL_HANDLER_NAME;
   @ApiStatus.Internal
   public static String TIMEOUT_HANDLER_NAME;

   public static PacketEventsAPI<?> getAPI() {
      return API;
   }

   public static void setAPI(PacketEventsAPI<?> api) {
      API = api;
   }
}
