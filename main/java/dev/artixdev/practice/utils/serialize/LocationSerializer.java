package dev.artixdev.practice.utils.serialize;

import java.lang.reflect.Type;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import dev.artixdev.libs.com.google.gson.JsonDeserializationContext;
import dev.artixdev.libs.com.google.gson.JsonDeserializer;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonObject;
import dev.artixdev.libs.com.google.gson.JsonParseException;
import dev.artixdev.libs.com.google.gson.JsonSerializationContext;
import dev.artixdev.libs.com.google.gson.JsonSerializer;
import dev.artixdev.practice.utils.LocationUtils;

public class LocationSerializer implements JsonDeserializer<Location>, JsonSerializer<Location> {
    
    public JsonElement serialize(Location src, Type typeOfSrc, JsonSerializationContext context) {
        return toJson(src);
    }

    public Location deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return fromJson(json);
    }

    public static JsonObject toJson(Location location) {
        if (location == null) {
            return null;
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("location", LocationUtils.serializeLocation(location));
            return jsonObject;
        }
    }

    public static Location fromJson(JsonElement jsonElement) {
        if (jsonElement != null && jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            if (jsonObject.has("location")) {
                return LocationUtils.deserializeLocation(jsonObject.get("location").getAsString());
            } else {
                World world = Bukkit.getWorld(jsonObject.get("world").getAsString());
                double x = jsonObject.get("x").getAsDouble();
                double y = jsonObject.get("y").getAsDouble();
                double z = jsonObject.get("z").getAsDouble();
                float yaw = jsonObject.get("yaw").getAsFloat();
                float pitch = jsonObject.get("pitch").getAsFloat();
                return new Location(world, x, y, z, yaw, pitch);
            }
        } else {
            return null;
        }
    }
}
