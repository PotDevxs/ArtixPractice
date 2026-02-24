package dev.artixdev.practice.managers;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.HotbarConfig;
import dev.artixdev.practice.utils.ChatUtils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.artixdev.libs.org.simpleyaml.configuration.ConfigurationSection;

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
     * Load all layout types from hotbar.yml (HOTBAR_ITEMS; CUSTOM_ITEMS merged per layout).
     */
    private void loadCustomItems() {
        ConfigurationSection hotbarItems = hotbarConfig.getConfigurationSection("HOTBAR_ITEMS");
        if (hotbarItems != null) {
            for (String layoutType : hotbarItems.getKeys(false)) {
                loadLayoutItemsFromConfig(layoutType);
            }
        }
        ConfigurationSection customItems = hotbarConfig.getConfigurationSection("CUSTOM_ITEMS");
        if (customItems != null) {
            for (String layoutType : customItems.getKeys(false)) {
                if (!layoutItems.containsKey(layoutType)) {
                    fillItemsFromSection(layoutType, "CUSTOM_ITEMS." + layoutType,
                        hotbarConfig.getConfigurationSection("CUSTOM_ITEMS." + layoutType));
                }
            }
        }
    }

    /**
     * Load one layout from HOTBAR_ITEMS.<layoutType> and merge CUSTOM_ITEMS.<layoutType> if present.
     */
    private void loadLayoutItemsFromConfig(String layoutType) {
        ConfigurationSection section = hotbarConfig.getConfigurationSection("HOTBAR_ITEMS." + layoutType);
        if (section != null) {
            fillItemsFromSection(layoutType, "HOTBAR_ITEMS." + layoutType, section);
        }
        ConfigurationSection customSection = hotbarConfig.getConfigurationSection("CUSTOM_ITEMS." + layoutType);
        if (customSection != null) {
            fillItemsFromSection(layoutType, "CUSTOM_ITEMS." + layoutType, customSection);
        }
    }

    private void fillItemsFromSection(String layoutType, String pathPrefix, ConfigurationSection section) {
        Map<Integer, ItemStack> items = layoutItems.computeIfAbsent(layoutType, k -> new HashMap<>());
        for (String itemKey : section.getKeys(false)) {
            ConfigurationSection itemSection = section.getConfigurationSection(itemKey);
            if (itemSection == null) continue;
            String itemPath = pathPrefix + "." + itemKey;
            if (hotbarConfig.contains(itemPath + ".ENABLED") && !hotbarConfig.getBoolean(itemPath + ".ENABLED")) continue;
            String name = hotbarConfig.getString(itemPath + ".NAME");
            String materialStr = hotbarConfig.getString(itemPath + ".MATERIAL");
            int slot = hotbarConfig.getInteger(itemPath + ".SLOT", 0);
            int durability = hotbarConfig.getInteger(itemPath + ".DURABILITY", 0);
            List<String> loreList = hotbarConfig.getStringList(itemPath + ".LORE");
            if (name == null) name = "&f" + itemKey;
            if (materialStr == null || materialStr.isEmpty()) continue;
            Material material = parseMaterial(materialStr);
            if (material == null || material == Material.AIR) continue;
            ItemStack stack = createItemFromConfig(material, name, durability, loreList);
            if (slot >= 0 && slot < 9) {
                items.put(slot, stack);
            }
        }
    }

    private static Material parseMaterial(String name) {
        if (name == null || name.isEmpty()) return null;
        Material m = Material.matchMaterial(name);
        if (m != null) return m;
        String legacy = name.replace("GOLDEN_", "GOLD_").replace("LEGACY_", "");
        m = Material.matchMaterial(legacy);
        if (m != null) return m;
        return Material.matchMaterial(name.toUpperCase());
    }

    private ItemStack createItemFromConfig(Material material, String displayName, int durability, List<String> lore) {
        ItemStack item = new ItemStack(material, 1, (short) durability);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatUtils.colorize(displayName));
            if (lore != null && !lore.isEmpty()) {
                List<String> colored = new ArrayList<>();
                for (String line : lore) {
                    colored.add(ChatUtils.colorize(line));
                }
                meta.setLore(colored);
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
     * Reload hotbar configurations and re-apply to all online players that have a layout.
     */
    public void reloadHotbars() {
        logger.info("Reloading hotbar configurations...");
        
        // Keep copy of who had which layout so we can re-apply after reload
        Map<UUID, String> previousLayouts = new HashMap<>(playerLayouts);
        
        // Clear item cache only; reload from config
        layoutItems.clear();
        loadCustomItems();
        
        // Re-apply hotbar to each player that had one
        for (Map.Entry<UUID, String> entry : previousLayouts.entrySet()) {
            Player p = Bukkit.getPlayer(entry.getKey());
            if (p != null && p.isOnline()) {
                setHotbarLayout(p, entry.getValue());
            }
        }
        
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
