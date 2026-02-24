package dev.artixdev.practice.nametag;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.nametag.adapter.NameTagAdapter;
import dev.artixdev.api.practice.nametag.setup.NameTagTeam;
import dev.artixdev.practice.configs.SettingsConfig;
import dev.artixdev.practice.Main;

/**
 * Event NameTag Adapter
 * Adapter for event-related nametags
 */
public class EventNameTagAdapter extends NameTagAdapter {
   
   private final Main plugin;
   
   /**
    * Constructor
    */
   public EventNameTagAdapter() {
      this.plugin = Main.getInstance();
   }
   
   /**
    * Create nametag for event participants
    * @param viewer the viewer
    * @param target the target
    * @return nametag team
    */
   public NameTagTeam createEventNameTag(Player viewer, Player target) {
      if (viewer == null || target == null) {
         return createNameTag("", "");
      }
      
      // Check if PlaceholderAPI is available
      boolean hasPlaceholderAPI = plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
      
      // Check if target is in an event
      if (isPlayerInEvent(target)) {
         String prefix = hasPlaceholderAPI ? 
            setPlaceholders(target, SettingsConfig.EVENT_PREFIX) : 
            SettingsConfig.EVENT_PREFIX;
            
         String suffix = hasPlaceholderAPI ? 
            setPlaceholders(target, SettingsConfig.EVENT_SUFFIX) : 
            SettingsConfig.EVENT_SUFFIX;
            
         return createNameTag(prefix, suffix);
      }
      
      return createNameTag("", "");
   }
   
   /**
    * Check if player is in an event
    * @param player the player
    * @return true if in event
    */
   private boolean isPlayerInEvent(Player player) {
      return plugin.getPlayerManager().getPlayerProfile(player.getUniqueId()) != null;
   }
   
   @Override
   public NameTagTeam fetchNameTag(Player viewer, Player target) {
      return createEventNameTag(viewer, target);
   }
   
   /**
    * Create nametag with prefix and suffix
    * @param prefix the prefix
    * @param suffix the suffix
    * @return nametag team
    */
   public NameTagTeam createNameTag(String prefix, String suffix) {
      return super.createNameTag(prefix, suffix);
   }
   
   /**
    * Set placeholders using PlaceholderAPI if available
    * @param player the player
    * @param text the text with placeholders
    * @return text with placeholders replaced
    */
   private String setPlaceholders(Player player, String text) {
      if (player == null || text == null) {
         return text;
      }
      
      try {
         // Use reflection to access PlaceholderAPI
         Class<?> placeholderAPIClass = Class.forName("me.clip.placeholderapi.PlaceholderAPI");
         java.lang.reflect.Method setPlaceholdersMethod = placeholderAPIClass.getMethod("setPlaceholders", Player.class, String.class);
         return (String) setPlaceholdersMethod.invoke(null, player, text);
      } catch (Exception e) {
         // PlaceholderAPI not available or method not found
         // Return original text
         return text;
      }
   }
}
