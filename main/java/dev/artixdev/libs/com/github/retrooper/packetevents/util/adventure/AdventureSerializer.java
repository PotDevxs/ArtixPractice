package dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure;

import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.gson.GsonComponentSerializer;
import dev.artixdev.libs.io.github.retrooper.packetevents.adventure.serializer.legacy.LegacyComponentSerializer;
import dev.artixdev.libs.net.kyori.adventure.text.Component;

public class AdventureSerializer {
   private static GsonComponentSerializer GSON;
   private static LegacyComponentSerializer LEGACY;

   public static GsonComponentSerializer getGsonSerializer() {
      if (GSON == null) {
         ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
         GSON = new GsonComponentSerializerExtended(version.isOlderThan(ServerVersion.V_1_16) && PacketEvents.getAPI().getSettings().shouldDownsampleColors(), version.isOlderThanOrEquals(ServerVersion.V_1_12_2));
      }

      return GSON;
   }

   public static LegacyComponentSerializer getLegacyGsonSerializer() {
      if (LEGACY == null) {
         LegacyComponentSerializer.Builder builder = LegacyComponentSerializer.builder();
         if (!PacketEvents.getAPI().getSettings().shouldDownsampleColors() || PacketEvents.getAPI().getServerManager().getVersion().isNewerThanOrEquals(ServerVersion.V_1_16)) {
            builder = builder.hexColors();
         }

         LEGACY = builder.build();
      }

      return LEGACY;
   }

   public static String asVanilla(Component component) {
      return getLegacyGsonSerializer().serialize(component);
   }

   public static Component fromLegacyFormat(String legacyMessage) {
      return getLegacyGsonSerializer().deserializeOrNull(legacyMessage);
   }

   public static String toLegacyFormat(Component component) {
      return (String)getLegacyGsonSerializer().serializeOrNull(component);
   }

   public static Component parseComponent(String json) {
      return getGsonSerializer().deserializeOrNull(json);
   }

   public static Component parseJsonTree(JsonElement json) {
      return getGsonSerializer().deserializeFromTree(json);
   }

   public static String toJson(Component component) {
      return (String)getGsonSerializer().serializeOrNull(component);
   }

   public static JsonElement toJsonTree(Component component) {
      return getGsonSerializer().serializeToTree(component);
   }
}
