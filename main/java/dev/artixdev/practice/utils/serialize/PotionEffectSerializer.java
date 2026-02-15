package dev.artixdev.practice.utils.serialize;

import java.lang.reflect.Type;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import dev.artixdev.libs.com.google.gson.JsonDeserializationContext;
import dev.artixdev.libs.com.google.gson.JsonDeserializer;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonObject;
import dev.artixdev.libs.com.google.gson.JsonParseException;
import dev.artixdev.libs.com.google.gson.JsonSerializationContext;
import dev.artixdev.libs.com.google.gson.JsonSerializer;

public class PotionEffectSerializer implements JsonDeserializer<PotionEffect>, JsonSerializer<PotionEffect> {
    
    public JsonElement serialize(PotionEffect src, Type typeOfSrc, JsonSerializationContext context) {
        return toJson(src);
    }

    public PotionEffect deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return fromJson(json);
    }

    public static JsonObject toJson(PotionEffect potionEffect) {
        if (potionEffect == null) {
            return null;
        } else {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("id", potionEffect.getType().getName());
            jsonObject.addProperty("duration", potionEffect.getDuration());
            jsonObject.addProperty("amplifier", potionEffect.getAmplifier());
            jsonObject.addProperty("ambient", potionEffect.isAmbient());
            return jsonObject;
        }
    }

    public static PotionEffect fromJson(JsonElement jsonElement) {
        if (jsonElement != null && jsonElement.isJsonObject()) {
            JsonObject jsonObject = jsonElement.getAsJsonObject();

            PotionEffectType effectType;
            try {
                effectType = PotionEffectType.getById(jsonObject.get("id").getAsInt());
            } catch (Exception e) {
                effectType = PotionEffectType.getByName(jsonObject.get("id").getAsString());
            }

            int duration = jsonObject.get("duration").getAsInt();
            int amplifier = jsonObject.get("amplifier").getAsInt();
            boolean ambient = jsonObject.get("ambient").getAsBoolean();
            return new PotionEffect(effectType, duration, amplifier, ambient);
        } else {
            return null;
        }
    }
}
