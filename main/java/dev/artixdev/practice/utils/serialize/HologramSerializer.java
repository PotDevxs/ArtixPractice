package dev.artixdev.practice.utils.serialize;

import java.lang.reflect.Type;
import java.util.UUID;
import org.bukkit.Location;
import dev.artixdev.libs.com.google.gson.JsonDeserializationContext;
import dev.artixdev.libs.com.google.gson.JsonDeserializer;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonObject;
import dev.artixdev.libs.com.google.gson.JsonParseException;
import dev.artixdev.libs.com.google.gson.JsonSerializationContext;
import dev.artixdev.libs.com.google.gson.JsonSerializer;
import dev.artixdev.libs.com.google.gson.reflect.TypeToken;
import dev.artixdev.practice.Practice;
import dev.artixdev.practice.models.HologramData;
import dev.artixdev.practice.enums.HologramType;
import dev.artixdev.practice.enums.LeaderboardType;
import dev.artixdev.practice.models.Kit;
import dev.artixdev.practice.holograms.AbstractHologram;
import dev.artixdev.practice.holograms.LeaderboardHologram;
import dev.artixdev.practice.holograms.KitHologram;

public class HologramSerializer implements JsonDeserializer<AbstractHologram>, JsonSerializer<AbstractHologram> {
    
    public AbstractHologram deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return fromJson(jsonElement);
    }

    public JsonElement serialize(AbstractHologram tickableHologram, Type type, JsonSerializationContext jsonSerializationContext) {
        return toJson(tickableHologram);
    }

    public static AbstractHologram fromJson(JsonElement src) {
        if (src != null && src.isJsonObject()) {
            JsonObject jsonObject = src.getAsJsonObject();

            HologramType hologramType;
            LeaderboardType leaderboardType;
            try {
                hologramType = HologramType.valueOf(jsonObject.get("type").getAsString());
                leaderboardType = LeaderboardType.valueOf(jsonObject.get("leaderboardType").getAsString());
            } catch (Exception e) {
                return null;
            }

            AbstractHologram hologram;
            switch(hologramType) {
            case GLOBAL_LEADERBOARD:
                hologram = new LeaderboardHologram(leaderboardType);
                break;
            case KIT_LEADERBOARD:
                hologram = new KitHologram(leaderboardType);
                break;
            default:
                Kit kit = Practice.getPlugin().getKitManager().getKitByName(jsonObject.get("kit").getAsString());
                if (kit == null) {
                    return null;
                }

                hologram = new KitHologram(kit, leaderboardType);
            }

            UUID uuid = UUID.fromString(jsonObject.get("_id").getAsString());
            HologramData data = new HologramData(uuid);
            data.setName(jsonObject.get("name").getAsString());
            
            Type locationAdapter = (new TypeToken<Location>() {
            }).getType();
            Location location = Practice.getPlugin().getGson().fromJson(jsonObject.get("location").getAsString(), locationAdapter);
            data.setLocation(location);
            
            hologram.setData(data);
            return hologram;
        } else {
            return null;
        }
    }

    public static JsonObject toJson(AbstractHologram src) {
        HologramData data = src.getData();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_id", data.getUniqueId().toString());
        jsonObject.addProperty("name", data.getName());
        jsonObject.addProperty("location", Practice.getPlugin().getGson().toJson(data.getLocation()));
        jsonObject.addProperty("type", src.getHologramType().name());
        jsonObject.addProperty("leaderboardType", src.getLeaderboardType().name());
        jsonObject.addProperty("updateIndex", src.getUpdateIndex());
        
        if (src.getHologramType() == HologramType.KIT_LEADERBOARD) {
            jsonObject.addProperty("kit", ((KitHologram)src).getKit().getName());
        }

        return jsonObject;
    }
}
