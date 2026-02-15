package dev.artixdev.practice.arena.serialize;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import dev.artixdev.practice.models.Arena;
import dev.artixdev.practice.utils.LocationUtils;
import dev.artixdev.practice.utils.cuboid.Cuboid;

/**
 * Arena Serializer
 * Handles JSON serialization and deserialization of Arena objects
 */
public class ArenaSerializer implements JsonDeserializer<Arena>, JsonSerializer<Arena> {
   
   @Override
   public Arena deserialize(JsonElement src, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
      return fromJson(src);
   }

   @Override
   public JsonElement serialize(Arena arena, Type type, JsonSerializationContext jsonSerializationContext) {
      return toJson(arena);
   }

   /**
    * Convert Arena to JSON
    * @param arena the arena to serialize
    * @return JSON object
    */
   public static JsonObject toJson(Arena arena) {
      if (arena == null) {
         return null;
      }
      
      JsonObject object = new JsonObject();
      object.addProperty("id", arena.getName());
      object.addProperty("name", arena.getName());
      object.addProperty("kitType", arena.getType().name());
      object.addProperty("enabled", arena.isEnabled());
      
      if (arena.getSpawn1() != null) {
         object.addProperty("spawn1", locationToString(arena.getSpawn1()));
      }
      
      if (arena.getSpawn2() != null) {
         object.addProperty("spawn2", locationToString(arena.getSpawn2()));
      }
      
      if (arena.getBounds() != null) {
         object.addProperty("min", locationToString(arena.getBounds().getLowerCorner()));
         object.addProperty("max", locationToString(arena.getBounds().getUpperCorner()));
      }
      
      return object;
   }

   /**
    * Convert JSON to Arena
    * @param src JSON element
    * @return Arena object
    */
   public static Arena fromJson(JsonElement src) {
      if (src != null && src.isJsonObject()) {
         JsonObject json = src.getAsJsonObject();
         
         String idString = json.get("id").getAsString();
         String name = json.get("name").getAsString();
         String kitTypeString = json.get("kitType").getAsString();
         boolean enabled = json.get("enabled").getAsBoolean();
         
         Arena arena = new Arena(name, dev.artixdev.practice.enums.ArenaType.valueOf(kitTypeString), 0);
         arena.setEnabled(enabled);
         
         if (json.has("spawn1")) {
            arena.setSpawn1(stringToLocation(json.get("spawn1").getAsString()));
         }
         
         if (json.has("spawn2")) {
            arena.setSpawn2(stringToLocation(json.get("spawn2").getAsString()));
         }
         
         if (json.has("min") && json.has("max")) {
            Location min = stringToLocation(json.get("min").getAsString());
            Location max = stringToLocation(json.get("max").getAsString());
            if (min != null && max != null) {
               arena.setBounds(new dev.artixdev.practice.utils.cuboid.Cuboid(min, max));
            }
         }
         
         return arena;
      } else {
         return null;
      }
   }
   
   /**
    * Convert Location to String
    * @param location the location
    * @return string representation
    */
   private static String locationToString(Location location) {
      if (location == null) {
         return null;
      }
      return location.getWorld().getName() + ":" + 
             location.getX() + ":" + 
             location.getY() + ":" + 
             location.getZ() + ":" + 
             location.getYaw() + ":" + 
             location.getPitch();
   }
   
   /**
    * Convert String to Location
    * @param locationString the location string
    * @return Location object
    */
   private static Location stringToLocation(String locationString) {
      if (locationString == null || locationString.isEmpty()) {
         return null;
      }
      
      String[] parts = locationString.split(":");
      if (parts.length != 6) {
         return null;
      }
      
      try {
         org.bukkit.World world = org.bukkit.Bukkit.getWorld(parts[0]);
         if (world == null) {
            return null;
         }
         
         double x = Double.parseDouble(parts[1]);
         double y = Double.parseDouble(parts[2]);
         double z = Double.parseDouble(parts[3]);
         float yaw = Float.parseFloat(parts[4]);
         float pitch = Float.parseFloat(parts[5]);
         
         return new Location(world, x, y, z, yaw, pitch);
      } catch (Exception e) {
         return null;
      }
   }
}
