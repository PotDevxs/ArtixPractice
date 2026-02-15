package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.packettype.config.clientbound;

public enum ClientboundConfigPacketType_1_20_2 {
   PLUGIN_MESSAGE,
   DISCONNECT,
   CONFIGURATION_END,
   KEEP_ALIVE,
   PING,
   REGISTRY_DATA,
   RESOURCE_PACK_SEND,
   UPDATE_ENABLED_FEATURES,
   UPDATE_TAGS;

   // $FF: synthetic method
   private static ClientboundConfigPacketType_1_20_2[] $values() {
      return new ClientboundConfigPacketType_1_20_2[]{PLUGIN_MESSAGE, DISCONNECT, CONFIGURATION_END, KEEP_ALIVE, PING, REGISTRY_DATA, RESOURCE_PACK_SEND, UPDATE_ENABLED_FEATURES, UPDATE_TAGS};
   }
}
