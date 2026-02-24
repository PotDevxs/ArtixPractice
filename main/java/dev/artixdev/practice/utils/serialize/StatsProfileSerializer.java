package dev.artixdev.practice.utils.serialize;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.Map.Entry;
import dev.artixdev.libs.com.google.gson.JsonDeserializationContext;
import dev.artixdev.libs.com.google.gson.JsonDeserializer;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonObject;
import dev.artixdev.libs.com.google.gson.JsonParseException;
import dev.artixdev.libs.com.google.gson.JsonSerializationContext;
import dev.artixdev.libs.com.google.gson.JsonSerializer;
import dev.artixdev.libs.com.google.gson.reflect.TypeToken;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.models.StatsProfile;
import dev.artixdev.practice.models.KitStats;
import dev.artixdev.practice.models.MatchHistory;
import dev.artixdev.practice.models.Division;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.utils.PlayerUtils;

public class StatsProfileSerializer implements JsonDeserializer<StatsProfile>, JsonSerializer<StatsProfile> {
    private static final Type MATCH_HISTORY_TYPE = (new TypeToken<List<MatchHistory>>() {
    }).getType();

    public StatsProfile deserialize(JsonElement src, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return fromJson(src);
    }

    public JsonElement serialize(StatsProfile statsProfile, Type type, JsonSerializationContext jsonSerializationContext) {
        return toJson(statsProfile);
    }

    public static JsonObject toJson(StatsProfile statsProfile) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_id", statsProfile.getUuid().toString());
        jsonObject.addProperty("name", statsProfile.getName() == null ? 
            PlayerUtils.getName(statsProfile.getUuid()) : statsProfile.getName());
        
        Division division = Main.getInstance().getRankManager().getDivision(statsProfile.getGlobalElo());
        if (division != null) {
            jsonObject.addProperty("division", Main.getInstance().getGson().toJson(division));
        }

        jsonObject.addProperty("globalElo", statsProfile.getGlobalElo());
        jsonObject.addProperty("kills", statsProfile.getKills());
        jsonObject.addProperty("wins", statsProfile.getWins());
        jsonObject.addProperty("winstreak", statsProfile.getWinstreak());
        jsonObject.addProperty("highestWinstreak", statsProfile.getHighestWinstreak());
        jsonObject.addProperty("unrankedMatchHistory", 
            Main.getInstance().getGson().toJson(statsProfile.getUnrankedMatchHistory(), MATCH_HISTORY_TYPE));
        jsonObject.addProperty("rankedMatchHistory", 
            Main.getInstance().getGson().toJson(statsProfile.getRankedMatchHistory(), MATCH_HISTORY_TYPE));
        
        JsonObject kitStats = new JsonObject();
        Iterator<Entry<String, KitStats>> iterator = statsProfile.getKitStats().entrySet().iterator();

        while(iterator.hasNext()) {
            Entry<String, KitStats> entry = iterator.next();
            JsonObject kitCache = new JsonObject();
            if (!entry.getKey().equals("global")) {
                KitStats cache = entry.getValue();
                kitCache.addProperty("elo", cache.getElo());
                kitCache.addProperty("wins", cache.getWins());
                kitCache.addProperty("losses", cache.getLosses());
                kitCache.addProperty("kills", cache.getKills());
                kitCache.addProperty("deaths", cache.getDeaths());
                kitCache.addProperty("winstreak", cache.getWinstreak());
                kitCache.addProperty("rankedWins", cache.getRankedWins());
                kitCache.addProperty("rankedLosses", cache.getRankedLosses());
                kitCache.addProperty("rankedWinstreak", cache.getRankedWinstreak());
                kitCache.addProperty("highestWinstreak", cache.getHighestWinstreak());
                kitCache.addProperty("rankedHighestWinstreak", cache.getRankedHighestWinstreak());
                kitStats.add(entry.getKey().toLowerCase(), kitCache);
            }
        }

        jsonObject.add("kitStats", kitStats);
        return jsonObject;
    }

    public static StatsProfile fromJson(JsonElement src) {
        if (src != null && src.isJsonObject()) {
            JsonObject json = src.getAsJsonObject();
            UUID uuid = UUID.fromString(json.get("_id").getAsString());
            StatsProfile statsProfile = new StatsProfile(uuid);
            
            if (json.has("name") && !json.get("name").isJsonNull()) {
                statsProfile.setName(json.get("name").getAsString());
            }

            int globalElo = json.get("globalElo").getAsInt();
            int kills = json.get("kills").getAsInt();
            int wins = json.get("wins").getAsInt();
            int winstreak = json.get("winstreak").getAsInt();
            int highestWinstreak = json.get("highestWinstreak").getAsInt();
            
            statsProfile.setGlobalElo(globalElo);
            statsProfile.setKills(kills);
            statsProfile.setWins(wins);
            statsProfile.setWinstreak(winstreak);
            statsProfile.setHighestWinstreak(highestWinstreak);
            
            List<MatchHistory> unrankedMatchHistory;
            if (json.has("unrankedMatchHistory")) {
                unrankedMatchHistory = Main.getInstance().getGson().fromJson(
                    json.get("unrankedMatchHistory").getAsString(), MATCH_HISTORY_TYPE);
                statsProfile.getUnrankedMatchHistory().addAll(unrankedMatchHistory);
            }

            if (json.has("rankedMatchHistory")) {
                unrankedMatchHistory = Main.getInstance().getGson().fromJson(
                    json.get("rankedMatchHistory").getAsString(), MATCH_HISTORY_TYPE);
                statsProfile.getRankedMatchHistory().addAll(unrankedMatchHistory);
            }

            JsonObject kitStats = json.getAsJsonObject("kitStats");
            if (kitStats == null) {
                return statsProfile;
            } else {
                Iterator<Entry<String, JsonElement>> iterator = kitStats.entrySet().iterator();

                while(iterator.hasNext()) {
                    Entry<String, JsonElement> cacheEntry = iterator.next();
                    if (cacheEntry != null) {
                        String kitName = cacheEntry.getKey();
                        JsonObject kitCache = cacheEntry.getValue().getAsJsonObject();
                        if (kitName != null && kitCache != null) {
                            KitStats statsCache = new KitStats();
                            statsCache.setElo(kitCache.get("elo").getAsInt());
                            statsCache.setWins(kitCache.get("wins").getAsInt());
                            statsCache.setLosses(kitCache.get("losses").getAsInt());
                            statsCache.setKills(kitCache.get("kills").getAsInt());
                            statsCache.setDeaths(kitCache.get("deaths").getAsInt());
                            statsCache.setWinstreak(kitCache.get("winstreak").getAsInt());
                            statsCache.setRankedWins(kitCache.get("rankedWins").getAsInt());
                            statsCache.setRankedLosses(kitCache.get("rankedLosses").getAsInt());
                            statsCache.setRankedWinstreak(kitCache.get("rankedWinstreak").getAsInt());
                            
                            if (kitCache.has("highestWinstreak")) {
                                statsCache.setHighestWinstreak(kitCache.get("highestWinstreak").getAsInt());
                            }

                            statsCache.setRankedHighestWinstreak(kitCache.get("rankedHighestWinstreak").getAsInt());
                            statsProfile.getKitStats().put(kitName.toLowerCase(), statsCache);
                        }
                    }
                }

                return statsProfile;
            }
        } else {
            return null;
        }
    }
}
