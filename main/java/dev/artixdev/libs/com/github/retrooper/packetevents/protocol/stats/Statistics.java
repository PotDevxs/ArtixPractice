package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.stats;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.MappingHelper;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonObject;
import dev.artixdev.libs.net.kyori.adventure.text.Component;

public class Statistics {
   private static final Map<String, Statistic> STATISTIC_MAP = new HashMap();

   public static Statistic getById(String id) {
      return (Statistic)STATISTIC_MAP.get(id);
   }

   static {
      ServerVersion version = PacketEvents.getAPI().getServerManager().getVersion();
      if (version.isOlderThan(ServerVersion.V_1_12_2)) {
         JsonObject mapping = MappingHelper.getJSONObject("stats/statistics");
         if (version.isOlderThanOrEquals(ServerVersion.V_1_8_3)) {
            mapping = mapping.getAsJsonObject("V_1_8");
         } else {
            mapping = mapping.getAsJsonObject("V_1_12");
         }

         Iterator var2 = mapping.entrySet().iterator();

         while(var2.hasNext()) {
            final Entry<String, JsonElement> entry = (Entry)var2.next();
            final Component value = AdventureSerializer.parseComponent(((JsonElement)entry.getValue()).getAsString());
            Statistic statistic = new Statistic() {
               public String getId() {
                  return (String)entry.getKey();
               }

               public Component display() {
                  return value;
               }

               public boolean equals(Object obj) {
                  return obj instanceof Statistic ? ((Statistic)obj).getId().equals(this.getId()) : false;
               }
            };
            STATISTIC_MAP.put((String)entry.getKey(), statistic);
         }
      }

   }
}
