package dev.artixdev.practice.nametag;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.nametag.NameTagHandler;
import dev.artixdev.api.practice.spigot.SpigotHandler;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.SettingsConfig;
import dev.artixdev.practice.utils.ChatUtils;

/**
 * NameTag Manager
 * Manages player name tags and visibility
 */
public class NameTagManager {
   
   private static final Logger logger = LogManager.getLogger(NameTagManager.class);
   private static final String[] DEFAULT_PREFIXES = {
      "&a[VIP]",
      "&b[MVP]",
      "&6[ELITE]",
      "&c[MASTER]",
      "&d[LEGEND]",
      "&e[CHAMPION]",
      "&f[PRO]",
      "&9[EXPERT]",
      "&5[ADMIN]",
      "&c[OWNER]"
   };
   
   private List<String> nameTagPrefixes;
   protected final Main plugin;
   protected String defaultPrefix;
   protected int maxNameTagLength = 16;
   protected final SettingsConfig settingsConfig;
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public NameTagManager(Main plugin) {
      this.plugin = plugin;
      this.settingsConfig = plugin.getSettingsConfig();
      this.nameTagPrefixes = new ArrayList<>(Arrays.asList(DEFAULT_PREFIXES));
      this.defaultPrefix = "&7";
   }
   
   /**
    * Initialize name tag manager
    */
   public void initialize() {
      logger.info("Initializing NameTag Manager...");
      
      // Load configuration
      loadConfiguration();
      
      // Register name tag handler
      registerNameTagHandler();
      
      logger.info("NameTag Manager initialized successfully!");
   }
   
   /**
    * Load configuration
    */
   private void loadConfiguration() {
      // Load name tag settings from config
      if (settingsConfig != null) {
         this.maxNameTagLength = settingsConfig.getMaxNameTagLength();
         this.defaultPrefix = settingsConfig.getDefaultNameTagPrefix();
         
         List<String> configPrefixes = settingsConfig.getNameTagPrefixes();
         if (configPrefixes != null && !configPrefixes.isEmpty()) {
            this.nameTagPrefixes = new ArrayList<>(configPrefixes);
         }
      }
   }
   
   /**
    * Register name tag handler
    */
   private void registerNameTagHandler() {
      try {
         SpigotHandler spigotHandler = SpigotHandler.getInstance();
         if (spigotHandler != null) {
            NameTagHandler nameTagHandler = spigotHandler.getNameTagHandler();
            if (nameTagHandler != null) {
               nameTagHandler.setNameTagManager(this);
               logger.info("NameTag Handler registered successfully!");
            } else {
               logger.warn("NameTag Handler not available!");
            }
         } else {
            logger.warn("SpigotHandler not available!");
         }
      } catch (Exception e) {
         logger.error("Failed to register NameTag Handler", e);
      }
   }
   
   /**
    * Update name tags for all players
    */
   public void updateNameTags() {
      try {
         SpigotHandler spigotHandler = SpigotHandler.getInstance();
         if (spigotHandler == null) {
            return;
         }
         
         NameTagHandler nameTagHandler = spigotHandler.getNameTagHandler();
         if (nameTagHandler == null) {
            return;
         }
         
         // Update name tags for all online players
         for (Player player : Bukkit.getOnlinePlayers()) {
            updatePlayerNameTag(player);
         }
         
      } catch (Exception e) {
         logger.error("Failed to update name tags", e);
      }
   }
   
   /**
    * Update name tag for specific player
    * @param player the player
    */
   public void updatePlayerNameTag(Player player) {
      if (player == null) {
         return;
      }
      
      try {
         // Get player profile
         dev.artixdev.practice.models.PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
         if (profile == null) {
            return;
         }
         
         // Generate name tag
         String nameTag = generateNameTag(player, profile);
         
         // Apply name tag
         applyNameTag(player, nameTag);
         
      } catch (Exception e) {
         logger.error("Failed to update name tag for player: " + player.getName(), e);
      }
   }
   
   /**
    * Generate name tag for player
    * @param player the player
    * @param profile the player profile
    * @return name tag
    */
   private String generateNameTag(Player player, dev.artixdev.practice.models.PlayerProfile profile) {
      StringBuilder nameTag = new StringBuilder();
      
      // Add prefix based on player level or rank
      String prefix = getPlayerPrefix(profile);
      if (prefix != null && !prefix.isEmpty()) {
         nameTag.append(ChatUtils.colorize(prefix));
      }
      
      // Add player name
      nameTag.append(player.getName());
      
      // Add suffix based on player state
      String suffix = getPlayerSuffix(profile);
      if (suffix != null && !suffix.isEmpty()) {
         nameTag.append(ChatUtils.colorize(suffix));
      }
      
      // Ensure name tag doesn't exceed max length
      String result = nameTag.toString();
      if (result.length() > maxNameTagLength) {
         result = result.substring(0, maxNameTagLength);
      }
      
      return result;
   }
   
   /**
    * Get player prefix
    * @param profile the player profile
    * @return prefix
    */
   private String getPlayerPrefix(dev.artixdev.practice.models.PlayerProfile profile) {
      if (profile == null) {
         return defaultPrefix;
      }
      
      // Determine prefix based on player level
      int level = profile.getLevel();
      if (level >= 100) {
         return "&c[OWNER]";
      } else if (level >= 80) {
         return "&5[ADMIN]";
      } else if (level >= 60) {
         return "&9[EXPERT]";
      } else if (level >= 40) {
         return "&f[PRO]";
      } else if (level >= 20) {
         return "&e[CHAMPION]";
      } else if (level >= 10) {
         return "&d[LEGEND]";
      } else if (level >= 5) {
         return "&c[MASTER]";
      } else if (level >= 3) {
         return "&6[ELITE]";
      } else if (level >= 2) {
         return "&b[MVP]";
      } else if (level >= 1) {
         return "&a[VIP]";
      }
      
      return defaultPrefix;
   }
   
   /**
    * Get player suffix
    * @param profile the player profile
    * @return suffix
    */
   private String getPlayerSuffix(dev.artixdev.practice.models.PlayerProfile profile) {
      if (profile == null) {
         return "";
      }
      
      // Determine suffix based on player state
      switch (profile.getState()) {
         case FIGHTING:
            return " &c[FIGHTING]";
         case SPECTATING:
            return " &7[SPECTATING]";
         case QUEUE:
            return " &e[QUEUE]";
         case LOBBY:
            return " &a[LOBBY]";
         case FREE:
            return " &f[FREE]";
         default:
            return "";
      }
   }
   
   /**
    * Apply name tag to player
    * @param player the player
    * @param nameTag the name tag
    */
   private void applyNameTag(Player player, String nameTag) {
      try {
         // Set player display name
         player.setDisplayName(nameTag);
         
         // Set player list name
         player.setPlayerListName(nameTag);
         
         // Update name tag for other players
         updateNameTagForOthers(player, nameTag);
         
      } catch (Exception e) {
         logger.error("Failed to apply name tag to player: " + player.getName(), e);
      }
   }
   
   /**
    * Update name tag for other players
    * @param player the player
    * @param nameTag the name tag
    */
   private void updateNameTagForOthers(Player player, String nameTag) {
      try {
         SpigotHandler spigotHandler = SpigotHandler.getInstance();
         if (spigotHandler == null) {
            return;
         }
         
         NameTagHandler nameTagHandler = spigotHandler.getNameTagHandler();
         if (nameTagHandler == null) {
            return;
         }
         
         // Update name tag for all other players
         for (Player other : Bukkit.getOnlinePlayers()) {
            if (!other.equals(player)) {
               nameTagHandler.updateNameTag(player, other, nameTag);
            }
         }
         
      } catch (Exception e) {
         logger.error("Failed to update name tag for others", e);
      }
   }
   
   /**
    * Get name tag prefixes
    * @return list of prefixes
    */
   public List<String> getNameTagPrefixes() {
      return new ArrayList<>(nameTagPrefixes);
   }
   
   /**
    * Set name tag prefixes
    * @param prefixes the prefixes
    */
   public void setNameTagPrefixes(List<String> prefixes) {
      this.nameTagPrefixes = new ArrayList<>(prefixes);
   }
   
   /**
    * Get default prefix
    * @return default prefix
    */
   public String getDefaultPrefix() {
      return defaultPrefix;
   }
   
   /**
    * Set default prefix
    * @param defaultPrefix the default prefix
    */
   public void setDefaultPrefix(String defaultPrefix) {
      this.defaultPrefix = defaultPrefix;
   }
   
   /**
    * Get max name tag length
    * @return max length
    */
   public int getMaxNameTagLength() {
      return maxNameTagLength;
   }
   
   /**
    * Set max name tag length
    * @param maxNameTagLength the max length
    */
   public void setMaxNameTagLength(int maxNameTagLength) {
      this.maxNameTagLength = maxNameTagLength;
   }
   
   /**
    * Shutdown name tag manager
    */
   public void shutdown() {
      logger.info("Shutting down NameTag Manager...");
      
      // Clear name tags
      clearAllNameTags();
      
      logger.info("NameTag Manager shutdown complete!");
   }
   
   /**
    * Clear all name tags
    */
   private void clearAllNameTags() {
      try {
         for (Player player : Bukkit.getOnlinePlayers()) {
            player.setDisplayName(player.getName());
            player.setPlayerListName(player.getName());
         }
      } catch (Exception e) {
         logger.error("Failed to clear name tags", e);
      }
   }
}
