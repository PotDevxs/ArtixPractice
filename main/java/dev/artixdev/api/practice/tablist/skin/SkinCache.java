package dev.artixdev.api.practice.tablist.skin;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.tablist.util.Skin;
import dev.artixdev.libs.com.google.gson.JsonArray;
import dev.artixdev.libs.com.google.gson.JsonElement;
import dev.artixdev.libs.com.google.gson.JsonObject;
import dev.artixdev.libs.com.google.gson.JsonParser;

public class SkinCache {
   private static final Logger log = LogManager.getLogger(SkinCache.class);
   public static final CachedSkin DEFAULT;
   private static final String ASHCON_URL = "https://api.ashcon.app/mojang/v2/user/%s";
   private static final String MOJANG_URL = "https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false";
   private final Map<String, CachedSkin> skinCache = new ConcurrentHashMap();

   public void registerCache(Player player) {
      CompletableFuture<CachedSkin> skinFuture = CompletableFuture.supplyAsync(() -> {
         try {
            WrappedGameProfile wrappedGameProfile = WrappedGameProfile.fromPlayer(player);
            WrappedSignedProperty prop = (WrappedSignedProperty)wrappedGameProfile.getProperties().get("textures").iterator().next();
            String value = prop.getValue();
            String signature = prop.getSignature();
            return new CachedSkin(player.getName(), value, signature);
         } catch (Exception e) {
            return this.fetchSkin(player, false);
         }
      });
      skinFuture.whenComplete((skin, action) -> {
         if (skin != null) {
            this.skinCache.put(player.getName(), skin);
         }

      });
   }

   public CachedSkin getSkin(Player player) {
      CachedSkin skin = (CachedSkin)this.skinCache.get(player.getName());
      if (skin == null) {
         this.registerCache(player);
         return DEFAULT;
      } else {
         return skin;
      }
   }

   public void removeCache(Player player) {
      this.skinCache.remove(player.getName());
   }

   public CachedSkin fetchSkin(Player player, boolean alternative) {
      String name = player.getName();
      UUID uuid = player.getUniqueId();

      try {
         return alternative ? this.fetchSkinName(name) : this.fetchSkinUUID(name, uuid);
      } catch (IOException | NullPointerException ignored) {
         return !alternative ? this.fetchSkin(player, true) : DEFAULT;
      }
   }

   public CachedSkin fetchSkinUUID(String name, UUID uuid) throws IOException {
      String uuidStr = uuid.toString();
      URL url = new URL(String.format("https://sessionserver.mojang.com/session/minecraft/profile/%s?unsigned=false", uuidStr));
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setRequestMethod("GET");
      if (connection.getResponseCode() != 200) {
         throw new IOException();
      } else {
         BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         Throwable var7 = null;

         JsonObject object;
         try {
            StringBuilder sb = new StringBuilder();

            String inputLine;
            while((inputLine = in.readLine()) != null) {
               sb.append(inputLine);
            }

            JsonElement element = JsonParser.parseString(sb.toString());
            if (element.isJsonObject()) {
               object = element.getAsJsonObject();
               JsonArray jsonProperties = object.get("properties").getAsJsonArray();
               JsonObject property = jsonProperties.get(0).getAsJsonObject();
               String value = property.get("value").getAsString();
               String signature = property.get("signature").getAsString();
               CachedSkin var16 = new CachedSkin(name, value, signature);
               return var16;
            }

            throw new IOException("Invalid JSON response: expected JsonObject");
         } catch (Throwable e) {
            var7 = e;
            throw e;
         } finally {
            if (in != null) {
               if (var7 != null) {
                  try {
                     in.close();
                  } catch (Throwable suppressed) {
                     var7.addSuppressed(suppressed);
                  }
               } else {
                  in.close();
               }
            }

         }
      }
   }

   public CachedSkin fetchSkinName(String name) throws IOException {
      URL url = new URL(String.format("https://api.ashcon.app/mojang/v2/user/%s", name));
      HttpURLConnection connection = (HttpURLConnection)url.openConnection();
      connection.setRequestMethod("GET");
      if (connection.getResponseCode() != 200) {
         return new CachedSkin(name, Skin.DEFAULT_SKIN.getValue(), Skin.DEFAULT_SKIN.getSignature());
      } else {
         BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
         Throwable var5 = null;

         CachedSkin var14;
         try {
            StringBuilder sb = new StringBuilder();

            String inputLine;
            while((inputLine = in.readLine()) != null) {
               sb.append(inputLine);
            }

            JsonElement element = JsonParser.parseString(sb.toString());
            JsonObject object;
            if (!element.isJsonObject()) {
               throw new IOException("Invalid JSON response: expected JsonObject");
            }

            object = element.getAsJsonObject();
            JsonObject textures = object.get("textures").getAsJsonObject();
            JsonObject raw = textures.get("raw").getAsJsonObject();
            String value = raw.get("value").getAsString();
            String signature = raw.get("signature").getAsString();
            var14 = new CachedSkin(name, value, signature);
         } catch (Throwable e) {
            var5 = e;
            throw e;
         } finally {
            if (in != null) {
               if (var5 != null) {
                  try {
                     in.close();
                  } catch (Throwable suppressed) {
                     var5.addSuppressed(suppressed);
                  }
               } else {
                  in.close();
               }
            }

         }

         return var14;
      }
   }

   static {
      DEFAULT = new CachedSkin("Default", Skin.DEFAULT_SKIN.getValue(), Skin.DEFAULT_SKIN.getSignature());
   }
}
