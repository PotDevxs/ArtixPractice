package dev.artixdev.practice.utils.other;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.tablist.skin.CachedSkin;
import dev.artixdev.api.practice.tablist.util.Skin;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEventsAPI;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.player.PlayerManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.GameMode;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.UserProfile;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerInfo;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonObject;
import dev.artixdev.libs.com.google.gson.JsonParser;

/**
 * SkinUtils
 * 
 * Utility class for player skin-related operations.
 * 
 * @author RefineDev
 * @since 1.0
 */
public final class SkinUtils {
    
    private static final String ASHCON_API_URL = "https://api.ashcon.app/mojang/v2/user/%s";
    public static final CachedSkin DEFAULT_SKIN = new CachedSkin(
        "Default",
        Skin.DEFAULT_SKIN.getValue(),
        Skin.DEFAULT_SKIN.getSignature()
    );
    
    /**
     * Fetch a player's skin from the Mojang API.
     * 
     * @param playerName the player's name
     * @return the cached skin, or null if not found
     */
    public static CachedSkin fetchSkin(String playerName) {
        if (playerName == null || playerName.isEmpty()) {
            return DEFAULT_SKIN;
        }
        
        try {
            URL url = new URL(String.format(ASHCON_API_URL, playerName));
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                return null;
            }
            
            BufferedReader reader = new BufferedReader(
                new InputStreamReader(connection.getInputStream())
            );
            
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            
            JsonElement jsonElement = JsonParser.parseString(response.toString());
            if (!jsonElement.isJsonObject()) {
                return null;
            }
            
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            JsonObject textures = jsonObject.get("textures").getAsJsonObject();
            JsonObject raw = textures.get("raw").getAsJsonObject();
            
            String value = raw.get("value").getAsString();
            String signature = raw.get("signature").getAsString();
            
            return new CachedSkin(playerName, value, signature);
            
        } catch (IOException | NullPointerException e) {
            return null;
        }
    }
    
    /**
     * Update player latency using PacketEvents.
     * This method's original implementation was not fully decompiled.
     * 
     * @param playerData the player data object
     */
    public static void updatePlayerLatency(Object playerData) {
        // Original implementation was not fully decompiled
        // This would update player latency using PacketEvents API
        try {
            PacketEventsAPI api = PacketEvents.getAPI();
            PlayerManager playerManager = api.getPlayerManager();
            
            // Implementation would create and send packet
            // WrapperPlayServerPlayerInfo packet = new WrapperPlayServerPlayerInfo(...);
            // playerManager.sendPacket(player, packet);
        } catch (Exception e) {
            // Ignore
        }
    }
    
    /**
     * Check if a location is within certain bounds.
     * This method's original implementation was not decompiled.
     * 
     * @param location the location to check
     * @param param1 first parameter
     * @param param2 second parameter
     * @return true if location is within bounds
     */
    public static boolean isLocationInBounds(Object location, int param1, int param2) {
        // Original implementation was not decompiled
        return false;
    }
    
    /**
     * Private constructor to prevent instantiation.
     * This is a utility class and should not be instantiated.
     * 
     * @throws UnsupportedOperationException always thrown when attempting to instantiate
     */
    private SkinUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
