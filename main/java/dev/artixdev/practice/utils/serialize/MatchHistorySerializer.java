package dev.artixdev.practice.utils.serialize;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.UUID;
import dev.artixdev.libs.com.google.gson.JsonDeserializationContext;
import dev.artixdev.libs.com.google.gson.JsonDeserializer;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonObject;
import dev.artixdev.libs.com.google.gson.JsonParseException;
import dev.artixdev.libs.com.google.gson.JsonSerializationContext;
import dev.artixdev.libs.com.google.gson.JsonSerializer;
import dev.artixdev.practice.Practice;
import dev.artixdev.practice.models.MatchHistory;
import dev.artixdev.practice.models.PlayerSnapshot;

public class MatchHistorySerializer implements JsonDeserializer<MatchHistory>, JsonSerializer<MatchHistory> {
    
    public MatchHistory deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return jsonElement != null && jsonElement.isJsonObject() ? deserialize(jsonElement.getAsJsonObject()) : null;
    }

    public JsonElement serialize(MatchHistory profileHistory, Type type, JsonSerializationContext jsonSerializationContext) {
        return profileHistory == null ? null : serialize(profileHistory);
    }

    public static MatchHistory deserialize(JsonObject object) {
        UUID uuid = UUID.fromString(object.get("_id").getAsString());
        String matchType = object.get("type").getAsString();
        long time;
        
        if (object.has("date")) {
            Date date = DateSerializer.deserialize(object.get("date").getAsString());
            if (date == null) {
                date = new Date();
            }
            time = date.getTime();
        } else {
            time = object.get("time").getAsLong();
        }

        PlayerSnapshot winnerSnapshot = null;
        if (object.has("winnerSnapshot") && !object.get("winnerSnapshot").isJsonNull()) {
            winnerSnapshot = Practice.getPlugin().getGson().fromJson(
                object.get("winnerSnapshot").getAsString(), PlayerSnapshot.class);
        }

        PlayerSnapshot loserSnapshot = null;
        if (object.has("looserSnapshot") && !object.get("looserSnapshot").isJsonNull()) {
            loserSnapshot = Practice.getPlugin().getGson().fromJson(
                object.get("looserSnapshot").getAsString(), PlayerSnapshot.class);
        }

        String kit = object.get("kit").getAsString();
        String arena = object.get("arena").getAsString();
        boolean ranked = object.get("ranked").getAsBoolean();
        
        return new MatchHistory(uuid, matchType, time, winnerSnapshot, loserSnapshot, kit, arena, ranked);
    }

    public static JsonObject serialize(MatchHistory matchHistory) {
        JsonObject object = new JsonObject();
        object.addProperty("_id", matchHistory.getUuid().toString());
        object.addProperty("type", matchHistory.getMatchType());
        object.addProperty("time", matchHistory.getTime());
        object.addProperty("kit", matchHistory.getKit());
        object.addProperty("arena", matchHistory.getArena());
        object.addProperty("ranked", matchHistory.isRanked());
        
        if (matchHistory.getWinnerSnapshot() != null) {
            object.addProperty("winnerSnapshot", Practice.getPlugin().getGson().toJson(matchHistory.getWinnerSnapshot()));
        }
        
        if (matchHistory.getLoserSnapshot() != null) {
            object.addProperty("looserSnapshot", Practice.getPlugin().getGson().toJson(matchHistory.getLoserSnapshot()));
        }
        
        return object;
    }
}
