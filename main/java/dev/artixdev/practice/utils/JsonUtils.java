package dev.artixdev.practice.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Type;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;

/**
 * JSON Utilities
 * Helper methods for JSON serialization/deserialization
 */
public class JsonUtils {
    
    private static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(UUID.class, new UUIDAdapter())
            .registerTypeAdapter(Location.class, new LocationAdapter())
            .registerTypeAdapter(ItemStack.class, new ItemStackAdapter())
            .setPrettyPrinting()
            .create();
    
    /**
     * Convert object to JSON string
     */
    public static String toJson(Object obj) {
        return gson.toJson(obj);
    }
    
    /**
     * Convert JSON string to object
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        return gson.fromJson(json, clazz);
    }
    
    /**
     * Convert JSON string to object with TypeToken
     */
    public static <T> T fromJson(String json, TypeToken<T> typeToken) {
        return gson.fromJson(json, typeToken.getType());
    }
    
    /**
     * Convert object to MongoDB Document
     */
    public static Document toDocument(Object obj) {
        String json = toJson(obj);
        return Document.parse(json);
    }
    
    /**
     * Convert MongoDB Document to object
     */
    public static <T> T fromDocument(Document doc, Class<T> clazz) {
        String json = doc.toJson();
        return fromJson(json, clazz);
    }
    
    /**
     * Convert Map to object
     */
    public static <T> T fromMap(Map<String, Object> map, Class<T> clazz) {
        String json = toJson(map);
        return fromJson(json, clazz);
    }
    
    /**
     * Convert object to Map
     */
    public static Map<String, Object> toMap(Object obj) {
        String json = toJson(obj);
        TypeToken<Map<String, Object>> typeToken = new TypeToken<Map<String, Object>>(){};
        return fromJson(json, typeToken);
    }
    
    // Custom Adapters
    private static class UUIDAdapter implements JsonSerializer<UUID>, JsonDeserializer<UUID> {
        @Override
        public JsonElement serialize(UUID src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.toString());
        }
        
        @Override
        public UUID deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return UUID.fromString(json.getAsString());
        }
    }
    
    private static class LocationAdapter implements JsonSerializer<Location>, JsonDeserializer<Location> {
        @Override
        public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
            Map<String, Object> map = new HashMap<>();
            map.put("world", src.getWorld().getName());
            map.put("x", src.getX());
            map.put("y", src.getY());
            map.put("z", src.getZ());
            map.put("yaw", src.getYaw());
            map.put("pitch", src.getPitch());
            return gson.toJsonTree(map);
        }
        
        @Override
        public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            @SuppressWarnings("unchecked")
            Map<String, Object> map = (Map<String, Object>) gson.fromJson(json, Map.class);
            
            if (map == null) {
                return null;
            }
            
            String worldName = map.get("world") != null ? map.get("world").toString() : null;
            World world = worldName != null ? Bukkit.getWorld(worldName) : null;
            
            if (world == null) {
                return null;
            }
            
            double x = map.get("x") != null ? ((Number) map.get("x")).doubleValue() : 0.0;
            double y = map.get("y") != null ? ((Number) map.get("y")).doubleValue() : 0.0;
            double z = map.get("z") != null ? ((Number) map.get("z")).doubleValue() : 0.0;
            float yaw = map.get("yaw") != null ? ((Number) map.get("yaw")).floatValue() : 0.0f;
            float pitch = map.get("pitch") != null ? ((Number) map.get("pitch")).floatValue() : 0.0f;
            
            return new Location(world, x, y, z, yaw, pitch);
        }
    }
    
    private static class ItemStackAdapter implements JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {
        @Override
        public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
            Map<String, Object> map = new HashMap<>();
            map.put("type", src.getType().name());
            map.put("amount", src.getAmount());
            map.put("durability", src.getDurability());
            if (src.hasItemMeta()) {
                map.put("meta", src.getItemMeta().serialize());
            }
            return gson.toJsonTree(map);
        }
        
        @Override
        public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            if (json == null || !json.isJsonObject()) return null;
            Map<String, Object> map = gson.fromJson(json, new TypeToken<Map<String, Object>>(){}.getType());
            if (map == null) return null;
            String typeStr = map.get("type") != null ? map.get("type").toString() : null;
            if (typeStr == null || typeStr.isEmpty()) return null;
            try {
                org.bukkit.Material material = org.bukkit.Material.valueOf(typeStr);
                if (material == null || material == org.bukkit.Material.AIR) return null;
                int amount = map.get("amount") != null ? ((Number) map.get("amount")).intValue() : 1;
                amount = Math.max(1, Math.min(amount, material.getMaxStackSize()));
                short durability = map.get("durability") != null ? ((Number) map.get("durability")).shortValue() : 0;
                ItemStack stack = new ItemStack(material, amount, durability);
                return stack;
            } catch (IllegalArgumentException e) {
                return null;
            }
        }
    }
}
