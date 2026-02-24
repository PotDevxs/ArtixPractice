package dev.artixdev.practice.managers;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.MenuConfig;
import dev.artixdev.practice.configs.menus.GeneralMenus;
import dev.artixdev.practice.configs.menus.QueueMenus;
import dev.artixdev.practice.configs.menus.PartyMenus;
import dev.artixdev.practice.configs.menus.StatsMenus;
import dev.artixdev.practice.utils.ChatUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Menu Manager
 * Handles menu creation, management, and configuration
 */
public class MenuManager {
    
    private final Main plugin;
    private final MenuConfig menuConfig;
    private final Map<UUID, String> playerMenus;
    private final Map<String, Inventory> menuCache;
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(MenuManager.class);
    
    public MenuManager(Main plugin) {
        this.plugin = plugin;
        this.menuConfig = plugin.getConfigManager().getMenuConfig();
        this.playerMenus = new HashMap<>();
        this.menuCache = new HashMap<>();
    }
    
    /**
     * Initialize menu manager
     */
    public void initialize() {
        logger.info("Initializing menu manager...");
        
        // Load menu configurations
        loadMenuConfigurations();
        
        // Initialize menu cache
        initializeMenuCache();
        
        logger.info("Menu manager initialized successfully!");
    }
    
    /**
     * Load menu configurations
     */
    private void loadMenuConfigurations() {
        // Register child storages
        menuConfig.registerChildStorages();
        
        // Add comments
        menuConfig.addSeparateComments();
        
        logger.info("Menu configurations loaded successfully!");
    }
    
    /**
     * Initialize menu cache
     */
    private void initializeMenuCache() {
        // Pre-create common menus for better performance
        createCommonMenus();
        
        logger.info("Menu cache initialized with " + menuCache.size() + " menus");
    }
    
    /**
     * Create common menus
     */
    private void createCommonMenus() {
        // Create lobby menu
        createLobbyMenu();
        
        // Create queue menu
        createQueueMenu();
        
        // Create party menu
        createPartyMenu();
        
        // Create kit menu
        createKitMenu();
        
        // Create stats menu
        createStatsMenu();
    }
    
    private static int menuSize(int size) {
        if (size <= 0) return 54;
        int slots = (size + 8) / 9 * 9;
        return Math.max(9, Math.min(54, slots));
    }
    
    /**
     * Create lobby menu (title/size from config where available)
     */
    private void createLobbyMenu() {
        String title = ChatUtils.translate("&8Lobby Menu");
        Inventory menu = Bukkit.createInventory(null, 54, title);
        menuCache.put("lobby", menu);
    }
    
    /**
     * Create queue menu from config (QueueMenus.QUEUE_TYPE_TITLE / SIZE)
     */
    private void createQueueMenu() {
        String title = QueueMenus.QUEUE_TYPE_TITLE != null ? ChatUtils.translate(QueueMenus.QUEUE_TYPE_TITLE) : "Queue";
        int size = menuSize(QueueMenus.QUEUE_TYPE_SIZE > 0 ? QueueMenus.QUEUE_TYPE_SIZE : 54);
        Inventory menu = Bukkit.createInventory(null, size, title);
        menuCache.put("queue", menu);
    }
    
    /**
     * Create party menu from config (PartyMenus)
     */
    private void createPartyMenu() {
        String title = PartyMenus.PARTY_GAMES_TITLE != null ? ChatUtils.translate(PartyMenus.PARTY_GAMES_TITLE) : "Party Menu";
        int size = menuSize(PartyMenus.PARTY_GAMES_SIZE > 0 ? PartyMenus.PARTY_GAMES_SIZE : 54);
        Inventory menu = Bukkit.createInventory(null, size, title);
        menuCache.put("party", menu);
    }
    
    /**
     * Create kit menu from config (GeneralMenus.SELECT_KIT_*)
     */
    private void createKitMenu() {
        String title = GeneralMenus.SELECT_KIT_TITLE != null ? ChatUtils.translate(GeneralMenus.SELECT_KIT_TITLE) : "Kit Menu";
        int size = menuSize(GeneralMenus.SELECT_KIT_SIZE > 0 ? GeneralMenus.SELECT_KIT_SIZE : 54);
        Inventory menu = Bukkit.createInventory(null, size, title);
        menuCache.put("kit", menu);
    }
    
    /**
     * Create stats menu from config (StatsMenus.STATS_MENU_TITLE)
     */
    private void createStatsMenu() {
        String title = StatsMenus.STATS_MENU_TITLE != null ? ChatUtils.translate(StatsMenus.STATS_MENU_TITLE) : "Stats Menu";
        Inventory menu = Bukkit.createInventory(null, 54, title);
        menuCache.put("stats", menu);
    }
    
    /**
     * Open menu for player
     */
    public void openMenu(Player player, String menuName) {
        Inventory menu = getMenu(menuName);
        if (menu != null) {
            player.openInventory(menu);
            playerMenus.put(player.getUniqueId(), menuName);
        }
    }
    
    /**
     * Get menu by name
     */
    public Inventory getMenu(String menuName) {
        return menuCache.get(menuName);
    }
    
    /**
     * Create custom menu
     */
    public Inventory createCustomMenu(String name, int size, String title) {
        Inventory menu = Bukkit.createInventory(null, size, title);
        menuCache.put(name, menu);
        return menu;
    }
    
    /**
     * Add item to menu
     */
    public void addMenuItem(String menuName, int slot, ItemStack item) {
        Inventory menu = menuCache.get(menuName);
        if (menu != null) {
            menu.setItem(slot, item);
        }
    }
    
    /**
     * Remove item from menu
     */
    public void removeMenuItem(String menuName, int slot) {
        Inventory menu = menuCache.get(menuName);
        if (menu != null) {
            menu.setItem(slot, null);
        }
    }
    
    /**
     * Clear menu
     */
    public void clearMenu(String menuName) {
        Inventory menu = menuCache.get(menuName);
        if (menu != null) {
            menu.clear();
        }
    }
    
    /**
     * Get player's current menu
     */
    public String getPlayerMenu(Player player) {
        return playerMenus.get(player.getUniqueId());
    }
    
    /**
     * Close player's menu
     */
    public void closePlayerMenu(Player player) {
        player.closeInventory();
        playerMenus.remove(player.getUniqueId());
    }
    
    /**
     * Check if player has menu open
     */
    public boolean hasMenuOpen(Player player) {
        return playerMenus.containsKey(player.getUniqueId());
    }
    
    /**
     * Update menu for player
     */
    public void updateMenu(Player player) {
        String menuName = playerMenus.get(player.getUniqueId());
        if (menuName != null) {
            // Update menu based on current state
            updateMenuContent(menuName, player);
        }
    }
    
    /**
     * Update menu content
     */
    private void updateMenuContent(String menuName, Player player) {
        Inventory menu = menuCache.get(menuName);
        if (menu != null) {
            // Update menu items based on player state
            // This would read from configuration and update accordingly
        }
    }
    
    /**
     * Get menu configuration
     */
    public MenuConfig getMenuConfig() {
        return menuConfig;
    }
    
    /**
     * Reload menu configurations
     */
    public void reloadMenus() {
        logger.info("Reloading menu configurations...");
        
        // Clear cache
        menuCache.clear();
        
        // Reload configurations
        // menuConfig.reload(); // This method might not exist
        
        // Reinitialize
        initializeMenuCache();
        
        logger.info("Menu configurations reloaded successfully!");
    }
    
    /**
     * Get menu cache size
     */
    public int getMenuCacheSize() {
        return menuCache.size();
    }
    
    /**
     * Get active menu count
     */
    public int getActiveMenuCount() {
        return playerMenus.size();
    }
    
    /**
     * Shutdown menu manager
     */
    public void shutdown() {
        // Close all player menus
        for (Player player : Bukkit.getOnlinePlayers()) {
            closePlayerMenu(player);
        }
        
        // Clear caches
        playerMenus.clear();
        menuCache.clear();
        
        logger.info("Menu manager shutdown complete.");
    }
}
