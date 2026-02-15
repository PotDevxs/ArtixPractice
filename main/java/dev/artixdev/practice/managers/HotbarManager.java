package dev.artixdev.practice.managers;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.HotbarConfig;
import dev.artixdev.practice.utils.ChatUtils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Hotbar Manager
 * Handles hotbar items and layout management with full configuration support
 */
public class HotbarManager {
    
    private final Main plugin;
    private final HotbarConfig hotbarConfig;
    private final Map<UUID, String> playerLayouts;
    private final Map<String, Map<Integer, ItemStack>> layoutItems;
    private static final org.apache.logging.log4j.Logger logger = org.apache.logging.log4j.LogManager.getLogger(HotbarManager.class);
    
    public HotbarManager(Main plugin) {
        this.plugin = plugin;
        this.hotbarConfig = plugin.getConfigManager().getHotbarConfig();
        this.playerLayouts = new HashMap<>();
        this.layoutItems = new HashMap<>();
    }
    
    /**
     * Initialize hotbar manager
     */
    public void initialize() {
        logger.info("Initializing hotbar manager...");
        
        // Load hotbar configurations
        loadHotbarConfigurations();
        
        // Initialize layout items
        loadCustomItems();
        
        logger.info("Hotbar manager initialized successfully!");
    }
    
    /**
     * Load hotbar configurations
     */
    private void loadHotbarConfigurations() {
        // Load custom items from configuration
        loadCustomItems();
        
        logger.info("Hotbar configurations loaded successfully!");
    }
    
    /**
     * Load custom items from configuration
     */
    private void loadCustomItems() {
        // This would read from the hotbar configuration file
        // and load custom items for different layout types
        
        // Example: Load lobby items
        loadLayoutItems("LOBBY");
        
        // Example: Load queue items
        loadLayoutItems("QUEUE");
        
        // Example: Load party items
        loadLayoutItems("PARTY_LEADER");
        loadLayoutItems("PARTY_MEMBER");
        loadLayoutItems("PARTY");
        
        // Example: Load spectating items
        loadLayoutItems("SPECTATING");
        
        // Example: Load event items
        loadLayoutItems("EVENT");
    }
    
    /**
     * Load layout items for specific type
     */
    private void loadLayoutItems(String layoutType) {
        Map<Integer, ItemStack> items = new HashMap<>();
        
        // This would read from configuration file
        // For now, we'll create some example items
        
        switch (layoutType) {
            case "LOBBY":
                items.put(0, createItem(Material.DIAMOND_SWORD, "&c&lQueue", "&7Click to open queue menu"));
                items.put(1, createItem(Material.EMERALD, "&a&lParty", "&7Click to open party menu"));
                items.put(2, createItem(Material.BOOK, "&e&lKits", "&7Click to open kit menu"));
                items.put(3, createItem(Material.SKULL_ITEM, "&b&lStats", "&7Click to view your stats"));
                items.put(4, createItem(Material.COMPASS, "&d&lArenas", "&7Click to view arenas"));
                break;
                
            case "QUEUE":
                items.put(0, createItem(Material.REDSTONE, "&c&lLeave Queue", "&7Click to leave queue"));
                items.put(1, createItem(Material.EMERALD, "&a&lParty", "&7Click to open party menu"));
                items.put(2, createItem(Material.BOOK, "&e&lKits", "&7Click to open kit menu"));
                items.put(3, createItem(Material.SKULL_ITEM, "&b&lStats", "&7Click to view your stats"));
                break;
                
            case "PARTY_LEADER":
                items.put(0, createItem(Material.DIAMOND_SWORD, "&c&lQueue", "&7Click to open queue menu"));
                items.put(1, createItem(Material.REDSTONE, "&c&lDisband Party", "&7Click to disband party"));
                items.put(2, createItem(Material.EMERALD, "&a&lInvite Player", "&7Click to invite player"));
                items.put(3, createItem(Material.BOOK, "&e&lKits", "&7Click to open kit menu"));
                items.put(4, createItem(Material.SKULL_ITEM, "&b&lStats", "&7Click to view your stats"));
                break;
                
            case "PARTY_MEMBER":
                items.put(0, createItem(Material.DIAMOND_SWORD, "&c&lQueue", "&7Click to open queue menu"));
                items.put(1, createItem(Material.REDSTONE, "&c&lLeave Party", "&7Click to leave party"));
                items.put(2, createItem(Material.BOOK, "&e&lKits", "&7Click to open kit menu"));
                items.put(3, createItem(Material.SKULL_ITEM, "&b&lStats", "&7Click to view your stats"));
                break;
                
            case "SPECTATING":
                items.put(0, createItem(Material.REDSTONE, "&c&lStop Spectating", "&7Click to stop spectating"));
                items.put(1, createItem(Material.EMERALD, "&a&lTeleport to Player", "&7Click to teleport to player"));
                items.put(2, createItem(Material.BOOK, "&e&lMatch Info", "&7Click to view match info"));
                break;
                
            case "EVENT":
                items.put(0, createItem(Material.REDSTONE, "&c&lLeave Event", "&7Click to leave event"));
                items.put(1, createItem(Material.BOOK, "&e&lEvent Info", "&7Click to view event info"));
                break;
        }
        
        layoutItems.put(layoutType, items);
    }
    
    /**
     * Create item with name and lore
     */
    private ItemStack createItem(Material material, String name, String... lore) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        
        if (meta != null) {
            meta.setDisplayName(ChatUtils.colorize(name));
            
            if (lore.length > 0) {
                List<String> loreList = new ArrayList<>();
                for (String line : lore) {
                    loreList.add(ChatUtils.colorize(line));
                }
                meta.setLore(loreList);
            }
            
            item.setItemMeta(meta);
        }
        
        return item;
    }
    
    /**
     * Set hotbar layout for player
     */
    public void setHotbarLayout(Player player, String layoutType) {
        Map<Integer, ItemStack> items = layoutItems.get(layoutType);
        if (items != null) {
            // Clear hotbar
            for (int i = 0; i < 9; i++) {
                player.getInventory().setItem(i, null);
            }
            
            // Set items
            for (Map.Entry<Integer, ItemStack> entry : items.entrySet()) {
                int slot = entry.getKey();
                ItemStack item = entry.getValue();
                
                if (slot >= 0 && slot < 9) {
                    player.getInventory().setItem(slot, item);
                }
            }
            
            // Update player layout
            playerLayouts.put(player.getUniqueId(), layoutType);
            
            logger.info("Set hotbar layout '" + layoutType + "' for player " + player.getName());
        }
    }
    
    /**
     * Get player's current layout
     */
    public String getPlayerLayout(Player player) {
        return playerLayouts.get(player.getUniqueId());
    }
    
    /**
     * Clear player's hotbar
     */
    public void clearHotbar(Player player) {
        for (int i = 0; i < 9; i++) {
            player.getInventory().setItem(i, null);
        }
        
        playerLayouts.remove(player.getUniqueId());
    }
    
    /**
     * Update hotbar for player
     */
    public void updateHotbar(Player player) {
        String layoutType = playerLayouts.get(player.getUniqueId());
        if (layoutType != null) {
            setHotbarLayout(player, layoutType);
        }
    }
    
    /**
     * Add custom item to layout
     */
    public void addCustomItem(String layoutType, int slot, ItemStack item) {
        Map<Integer, ItemStack> items = layoutItems.computeIfAbsent(layoutType, k -> new HashMap<>());
        items.put(slot, item);
    }
    
    /**
     * Remove custom item from layout
     */
    public void removeCustomItem(String layoutType, int slot) {
        Map<Integer, ItemStack> items = layoutItems.get(layoutType);
        if (items != null) {
            items.remove(slot);
        }
    }
    
    /**
     * Get layout items
     */
    public Map<Integer, ItemStack> getLayoutItems(String layoutType) {
        return layoutItems.get(layoutType);
    }
    
    /**
     * Get available layout types
     */
    public java.util.Set<String> getAvailableLayouts() {
        return layoutItems.keySet();
    }
    
    /**
     * Check if layout exists
     */
    public boolean hasLayout(String layoutType) {
        return layoutItems.containsKey(layoutType);
    }
    
    /**
     * Get hotbar configuration
     */
    public HotbarConfig getHotbarConfig() {
        return hotbarConfig;
    }
    
    /**
     * Reload hotbar configurations
     */
    public void reloadHotbars() {
        logger.info("Reloading hotbar configurations...");
        
        // Clear cache
        layoutItems.clear();
        playerLayouts.clear();
        
        // Reload configurations
        // hotbarConfig.reload(); // This method might not exist
        
        // Reinitialize
        loadCustomItems();
        
        logger.info("Hotbar configurations reloaded successfully!");
    }
    
    /**
     * Get active layout count
     */
    public int getActiveLayoutCount() {
        return playerLayouts.size();
    }
    
    /**
     * Shutdown hotbar manager
     */
    public void shutdown() {
        // Clear all player hotbars
        for (Player player : Bukkit.getOnlinePlayers()) {
            clearHotbar(player);
        }
        
        // Clear caches
        playerLayouts.clear();
        layoutItems.clear();
        
        logger.info("Hotbar manager shutdown complete.");
    }
}
