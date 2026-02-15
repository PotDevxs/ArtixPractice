package dev.artixdev.practice.utils.serialize;

import java.lang.reflect.Type;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.libs.com.google.gson.JsonDeserializationContext;
import dev.artixdev.libs.com.google.gson.JsonDeserializer;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonObject;
import dev.artixdev.libs.com.google.gson.JsonParseException;
import dev.artixdev.libs.com.google.gson.JsonParser;
import dev.artixdev.libs.com.google.gson.JsonPrimitive;
import dev.artixdev.libs.com.google.gson.JsonSerializationContext;
import dev.artixdev.libs.com.google.gson.JsonSerializer;
import dev.artixdev.practice.utils.ItemUtils;

public class ItemStackSerializer implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack> {
    
    public JsonElement serialize(ItemStack item, Type type, JsonSerializationContext context) {
        return serialize(item);
    }

    public ItemStack deserialize(JsonElement element, Type type, JsonDeserializationContext context) throws JsonParseException {
        return deserialize(element);
    }

    public static JsonElement serialize(ItemStack item) {
        if (item == null) {
            item = new ItemStack(Material.AIR);
        }

        String serialized = ItemUtils.serializeItemStack(item);
        if (serialized == null) {
            return new JsonPrimitive("");
        }
        
        // Try to parse as JSON object, fallback to primitive string
        try {
            return JsonParser.parseString(serialized);
        } catch (Exception e) {
            return new JsonPrimitive(serialized);
        }
    }

    public static ItemStack deserialize(JsonElement object) {
        if (!(object instanceof JsonObject)) {
            return new ItemStack(Material.AIR);
        } else {
            JsonObject element = (JsonObject)object;
            return ItemUtils.deserializeItemStack(element);
        }
    }
}
