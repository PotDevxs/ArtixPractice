package dev.artixdev.practice.managers;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Predicate;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.events.CooldownEndEvent;
import dev.artixdev.practice.events.CooldownStartEvent;

/**
 * Cooldown Manager
 * Manages cooldowns for players
 */
public abstract class CooldownManager extends BaseCooldownManager {
   
   protected final Map<UUID, CooldownData> playerCooldowns = new ConcurrentHashMap<>();
   
   /**
    * Constructor
    * @param name the cooldown name
    * @param defaultDuration the default duration
    */
   public CooldownManager(String name, long defaultDuration) {
      super(name, defaultDuration);
   }
   
   /**
    * Get player cooldowns
    * @return player cooldowns map
    */
   public Map<UUID, CooldownData> getPlayerCooldowns() {
      return playerCooldowns;
   }
   
   /**
    * Add cooldown for player
    * @param player the player
    * @param uuid the player UUID
    */
   protected void addCooldown(Player player, UUID uuid) {
      CooldownData cooldownData = new CooldownData(uuid, System.currentTimeMillis() + getDefaultDuration());
      playerCooldowns.put(uuid, cooldownData);
      
      // Fire cooldown start event
      CooldownStartEvent event = new CooldownStartEvent(player, this);
      Bukkit.getPluginManager().callEvent(event);
   }
   
   /**
    * Get cooldown duration for player
    * @param player the player
    * @return cooldown duration in milliseconds
    */
   public long getCooldownDuration(Player player) {
      CooldownData cooldownData = playerCooldowns.get(player.getUniqueId());
      if (cooldownData == null) {
         return 0;
      }
      
      long remaining = cooldownData.getEndTime() - System.currentTimeMillis();
      return Math.max(0, remaining);
   }
   
   /**
    * Remove cooldown for player
    * @param player the player
    * @param uuid the player UUID
    * @return removed cooldown data
    */
   public CooldownData removeCooldown(Player player, UUID uuid) {
      CooldownData cooldownData = playerCooldowns.remove(uuid);
      
      if (cooldownData != null) {
         cooldownData.end();
         
         // Fire cooldown end event
         CooldownEndEvent event = new CooldownEndEvent(player, this);
         Bukkit.getPluginManager().callEvent(event);
      }
      
      return cooldownData;
   }
   
   /**
    * Set cooldown for player
    * @param player the player
    * @param uuid the player UUID
    * @param duration the duration in milliseconds
    * @param bypassPermission the bypass permission
    */
   public void setCooldown(Player player, UUID uuid, long duration, boolean bypassPermission) {
      CooldownData cooldownData = playerCooldowns.get(uuid);
      
      if (cooldownData != null) {
         boolean wasActive = cooldownData.isActive();
         
         if (wasActive != bypassPermission) {
            // Fire cooldown change event
            CooldownStartEvent event = new CooldownStartEvent(player, this);
            Bukkit.getPluginManager().callEvent(event);
            
            if (!event.isCancelled()) {
               cooldownData.setActive(bypassPermission);
            }
         }
      }
   }
   
   /**
    * Set cooldown for player with predicate
    * @param player the player
    * @param uuid the player UUID
    * @param duration the duration in milliseconds
    * @param bypassPermission the bypass permission
    * @param predicate the predicate
    */
   public void setCooldown(Player player, UUID uuid, long duration, boolean bypassPermission, Predicate<Long> predicate) {
      if (predicate != null && !predicate.test(duration)) {
         return;
      }
      
      setCooldown(player, uuid, duration, bypassPermission);
   }
   
   /**
    * Remove cooldown for player (no player object)
    * @param uuid the player UUID
    */
   public void removeCooldown(UUID uuid) {
      removeCooldown(null, uuid);
   }
   
   /**
    * Set cooldown for player (no player object)
    * @param uuid the player UUID
    * @param duration the duration in milliseconds
    * @param bypassPermission the bypass permission
    */
   public void setCooldown(UUID uuid, long duration, boolean bypassPermission) {
      setCooldown(null, uuid, duration, bypassPermission);
   }
   
   /**
    * Check if player has cooldown
    * @param player the player
    * @return true if has cooldown
    */
   public boolean hasCooldown(Player player) {
      return hasCooldown(player.getUniqueId());
   }
   
   /**
    * Check if player has cooldown
    * @param uuid the player UUID
    * @return true if has cooldown
    */
   public boolean hasCooldown(UUID uuid) {
      CooldownData cooldownData = playerCooldowns.get(uuid);
      return cooldownData != null && cooldownData.isActive() && cooldownData.getEndTime() > System.currentTimeMillis();
   }
   
   /**
    * Get cooldown data for player
    * @param uuid the player UUID
    * @return cooldown data or null
    */
   public CooldownData getCooldownData(UUID uuid) {
      return playerCooldowns.get(uuid);
   }
   
   /**
    * Get cooldown data for player
    * @param player the player
    * @return cooldown data or null
    */
   public CooldownData getCooldownData(Player player) {
      return getCooldownData(player.getUniqueId());
   }
   
   /**
    * Clear all cooldowns
    */
   public void clearAllCooldowns() {
      playerCooldowns.clear();
   }
   
   /**
    * Get cooldown count
    * @return cooldown count
    */
   public int getCooldownCount() {
      return playerCooldowns.size();
   }
   
   /**
    * Check if cooldown is active
    * @param uuid the player UUID
    * @return true if active
    */
   public boolean isCooldownActive(UUID uuid) {
      CooldownData cooldownData = playerCooldowns.get(uuid);
      return cooldownData != null && cooldownData.isActive();
   }
   
   /**
    * Check if cooldown is active
    * @param player the player
    * @return true if active
    */
   public boolean isCooldownActive(Player player) {
      return isCooldownActive(player.getUniqueId());
   }
   
   /**
    * Cooldown Data Class
    */
   public static class CooldownData {
      private final UUID playerUUID;
      private final long endTime;
      private boolean active;
      
      public CooldownData(UUID playerUUID, long endTime) {
         this.playerUUID = playerUUID;
         this.endTime = endTime;
         this.active = true;
      }
      
      public UUID getPlayerUUID() {
         return playerUUID;
      }
      
      public long getEndTime() {
         return endTime;
      }
      
      public boolean isActive() {
         return active;
      }
      
      public void setActive(boolean active) {
         this.active = active;
      }
      
      public void end() {
         this.active = false;
      }
      
      public long getRemainingTime() {
         return Math.max(0, endTime - System.currentTimeMillis());
      }
      
      public boolean isExpired() {
         return System.currentTimeMillis() >= endTime;
      }
   }
}
