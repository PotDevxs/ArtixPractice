package dev.artixdev.practice.enums;

import java.util.Collections;
import java.util.List;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.configs.menus.GeneralMenus;
import dev.artixdev.practice.utils.other.Callback;

/**
 * Menu Type
 * Enum for different menu types
 */
public enum MenuType {
   
   // Main menus
   MAIN_MENU("Main Menu", "&bMain Menu", XMaterial.NETHER_STAR, Collections.emptyList()),
   PROFILE_MENU("Profile Menu", "&bYour Profile", XMaterial.PLAYER_HEAD, Collections.emptyList()),
   STATISTICS_MENU("Statistics Menu", "&bStatistics", XMaterial.PAPER, Collections.emptyList()),
   SETTINGS_MENU("Settings Menu", "&bSettings", XMaterial.REDSTONE, Collections.emptyList()),
   COSMETICS_MENU("Cosmetics Menu", "&bCosmetics", XMaterial.DIAMOND, Collections.emptyList()),
   
   // Match menus
   MATCH_MENU("Match Menu", "&bMatch", XMaterial.DIAMOND_SWORD, Collections.emptyList()),
   SPECTATE_MENU("Spectate Menu", "&bSpectate", XMaterial.ENDER_EYE, Collections.emptyList()),
   QUEUE_MENU("Queue Menu", "&bQueue", XMaterial.CLOCK, Collections.emptyList()),
   
   // Admin menus
   ADMIN_MENU("Admin Menu", "&cAdmin Panel", XMaterial.COMMAND_BLOCK, Collections.emptyList()),
   ARENA_MENU("Arena Menu", "&cArena Management", XMaterial.GRASS_BLOCK, Collections.emptyList()),
   KIT_MENU("Kit Menu", "&cKit Management", XMaterial.IRON_SWORD, Collections.emptyList()),
   BOT_MENU("Bot Menu", "&cBot Management", XMaterial.IRON_GOLEM_SPAWN_EGG, Collections.emptyList()),
   
   // Leaderboard menus
   LEADERBOARD_MENU("Leaderboard Menu", "&bLeaderboard", XMaterial.GOLD_INGOT, Collections.emptyList()),
   ELO_LEADERBOARD("ELO Leaderboard", "&bELO Leaderboard", XMaterial.DIAMOND, Collections.emptyList()),
   KILLS_LEADERBOARD("Kills Leaderboard", "&bKills Leaderboard", XMaterial.IRON_SWORD, Collections.emptyList()),
   WINS_LEADERBOARD("Wins Leaderboard", "&bWins Leaderboard", XMaterial.GOLDEN_APPLE, Collections.emptyList()),
   
   // Tournament menus
   TOURNAMENT_MENU("Tournament Menu", "&bTournament", XMaterial.GOLD_INGOT, Collections.emptyList()),
   TOURNAMENT_BRACKET("Tournament Bracket", "&bTournament Bracket", XMaterial.PAPER, Collections.emptyList()),
   
   // Custom menus
   CUSTOM_MENU("Custom Menu", "&bCustom Menu", XMaterial.NETHER_STAR, Collections.emptyList());
   
   private final String name;
   private final String displayName;
   private final XMaterial icon;
   private final List<String> lore;
   
   /**
    * Constructor
    * @param name the menu name
    * @param displayName the display name
    * @param icon the icon material
    * @param lore the lore
    */
   MenuType(String name, String displayName, XMaterial icon, List<String> lore) {
      this.name = name;
      this.displayName = displayName;
      this.icon = icon;
      this.lore = lore;
   }
   
   /**
    * Get menu name
    * @return menu name
    */
   public String getName() {
      return name;
   }
   
   /**
    * Get display name
    * @return display name
    */
   public String getDisplayName() {
      return displayName;
   }
   
   /**
    * Get icon
    * @return icon
    */
   public XMaterial getIcon() {
      return icon;
   }
   
   /**
    * Get lore
    * @return lore
    */
   public List<String> getLore() {
      return lore;
   }
   
   /**
    * Get icon item
    * @return icon item
    */
   public ItemStack getIconItem() {
      return icon.parseItem();
   }
   
   /**
    * Check if menu is admin only
    * @return true if admin only
    */
   public boolean isAdminOnly() {
      return this == ADMIN_MENU || 
             this == ARENA_MENU || 
             this == KIT_MENU || 
             this == BOT_MENU;
   }
   
   /**
    * Check if menu is match related
    * @return true if match related
    */
   public boolean isMatchRelated() {
      return this == MATCH_MENU || 
             this == SPECTATE_MENU || 
             this == QUEUE_MENU;
   }
   
   /**
    * Check if menu is leaderboard related
    * @return true if leaderboard related
    */
   public boolean isLeaderboardRelated() {
      return this == LEADERBOARD_MENU || 
             this == ELO_LEADERBOARD || 
             this == KILLS_LEADERBOARD || 
             this == WINS_LEADERBOARD;
   }
   
   /**
    * Check if menu is tournament related
    * @return true if tournament related
    */
   public boolean isTournamentRelated() {
      return this == TOURNAMENT_MENU || 
             this == TOURNAMENT_BRACKET;
   }
   
   /**
    * Get menu by name
    * @param name the menu name
    * @return menu type
    */
   public static MenuType getByName(String name) {
      for (MenuType type : values()) {
         if (type.getName().equalsIgnoreCase(name)) {
            return type;
         }
      }
      return null;
   }
   
   /**
    * Get menu by display name
    * @param displayName the display name
    * @return menu type
    */
   public static MenuType getByDisplayName(String displayName) {
      for (MenuType type : values()) {
         if (type.getDisplayName().equalsIgnoreCase(displayName)) {
            return type;
         }
      }
      return null;
   }
   
   /**
    * Get all admin menus
    * @return list of admin menus
    */
   public static List<MenuType> getAdminMenus() {
      return java.util.Arrays.stream(values())
         .filter(MenuType::isAdminOnly)
         .collect(java.util.stream.Collectors.toList());
   }
   
   /**
    * Get all match menus
    * @return list of match menus
    */
   public static List<MenuType> getMatchMenus() {
      return java.util.Arrays.stream(values())
         .filter(MenuType::isMatchRelated)
         .collect(java.util.stream.Collectors.toList());
   }
   
   /**
    * Get all leaderboard menus
    * @return list of leaderboard menus
    */
   public static List<MenuType> getLeaderboardMenus() {
      return java.util.Arrays.stream(values())
         .filter(MenuType::isLeaderboardRelated)
         .collect(java.util.stream.Collectors.toList());
   }
   
   /**
    * Get all tournament menus
    * @return list of tournament menus
    */
   public static List<MenuType> getTournamentMenus() {
      return java.util.Arrays.stream(values())
         .filter(MenuType::isTournamentRelated)
         .collect(java.util.stream.Collectors.toList());
   }
   
   @Override
   public String toString() {
      return displayName;
   }
}
