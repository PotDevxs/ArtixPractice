package dev.artixdev.practice.managers;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.enums.KitType;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.models.Kit;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * Kit Manager
 * Handles all kit-related operations
 */
public class KitManager {
    
    private final Main plugin;
    private final Map<KitType, List<ItemStack>> kitItems;
    private final Map<KitType, List<ItemStack>> kitArmor;
    
    public KitManager(Main plugin) {
        this.plugin = plugin;
        this.kitItems = new HashMap<>();
        this.kitArmor = new HashMap<>();
    }
    
    /**
     * Initialize kit manager
     */
    public void initialize() {
        plugin.getLogger().info("Initializing KitManager...");
        
        // Load default kits
        loadDefaultKits();
        
        plugin.getLogger().info("KitManager initialized successfully!");
    }
    
    /**
     * Load default kits
     */
    private void loadDefaultKits() {
        // Diamond Kit
        List<ItemStack> diamondItems = new ArrayList<>();
        diamondItems.add(new ItemStack(Material.DIAMOND_SWORD));
        diamondItems.add(new ItemStack(Material.GOLDEN_APPLE, 8));
        diamondItems.add(new ItemStack(Material.ENDER_PEARL, 16));
        kitItems.put(KitType.DIAMOND, diamondItems);
        
        List<ItemStack> diamondArmor = new ArrayList<>();
        diamondArmor.add(new ItemStack(Material.DIAMOND_HELMET));
        diamondArmor.add(new ItemStack(Material.DIAMOND_CHESTPLATE));
        diamondArmor.add(new ItemStack(Material.DIAMOND_LEGGINGS));
        diamondArmor.add(new ItemStack(Material.DIAMOND_BOOTS));
        kitArmor.put(KitType.DIAMOND, diamondArmor);
        
        // Iron Kit
        List<ItemStack> ironItems = new ArrayList<>();
        ironItems.add(new ItemStack(Material.IRON_SWORD));
        ironItems.add(new ItemStack(Material.GOLDEN_APPLE, 4));
        ironItems.add(new ItemStack(Material.ENDER_PEARL, 8));
        kitItems.put(KitType.IRON, ironItems);
        
        List<ItemStack> ironArmor = new ArrayList<>();
        ironArmor.add(new ItemStack(Material.IRON_HELMET));
        ironArmor.add(new ItemStack(Material.IRON_CHESTPLATE));
        ironArmor.add(new ItemStack(Material.IRON_LEGGINGS));
        ironArmor.add(new ItemStack(Material.IRON_BOOTS));
        kitArmor.put(KitType.IRON, ironArmor);
        
        // Gold Kit
        List<ItemStack> goldItems = new ArrayList<>();
        goldItems.add(XMaterial.GOLDEN_SWORD.parseItem());
        goldItems.add(new ItemStack(XMaterial.GOLDEN_APPLE.parseMaterial(), 2));
        goldItems.add(new ItemStack(XMaterial.ENDER_PEARL.parseMaterial(), 4));
        kitItems.put(KitType.GOLD, goldItems);
        
        List<ItemStack> goldArmor = new ArrayList<>();
        goldArmor.add(XMaterial.GOLDEN_HELMET.parseItem());
        goldArmor.add(XMaterial.GOLDEN_CHESTPLATE.parseItem());
        goldArmor.add(XMaterial.GOLDEN_LEGGINGS.parseItem());
        goldArmor.add(XMaterial.GOLDEN_BOOTS.parseItem());
        kitArmor.put(KitType.GOLD, goldArmor);
        
        // Chain Kit (using STONE as closest match)
        List<ItemStack> chainItems = new ArrayList<>();
        chainItems.add(new ItemStack(Material.STONE_SWORD));
        chainItems.add(new ItemStack(Material.GOLDEN_APPLE, 1));
        chainItems.add(new ItemStack(Material.ENDER_PEARL, 2));
        kitItems.put(KitType.STONE, chainItems);
        
        List<ItemStack> chainArmor = new ArrayList<>();
        chainArmor.add(new ItemStack(Material.CHAINMAIL_HELMET));
        chainArmor.add(new ItemStack(Material.CHAINMAIL_CHESTPLATE));
        chainArmor.add(new ItemStack(Material.CHAINMAIL_LEGGINGS));
        chainArmor.add(new ItemStack(Material.CHAINMAIL_BOOTS));
        kitArmor.put(KitType.STONE, chainArmor);
        
        // Leather Kit (using WOOD as closest match)
        List<ItemStack> leatherItems = new ArrayList<>();
        leatherItems.add(XMaterial.WOODEN_SWORD.parseItem());
        leatherItems.add(new ItemStack(XMaterial.GOLDEN_APPLE.parseMaterial(), 1));
        kitItems.put(KitType.WOOD, leatherItems);
        
        List<ItemStack> leatherArmor = new ArrayList<>();
        leatherArmor.add(new ItemStack(Material.LEATHER_HELMET));
        leatherArmor.add(new ItemStack(Material.LEATHER_CHESTPLATE));
        leatherArmor.add(new ItemStack(Material.LEATHER_LEGGINGS));
        leatherArmor.add(new ItemStack(Material.LEATHER_BOOTS));
        kitArmor.put(KitType.WOOD, leatherArmor);
        
        // Netherite Kit (using CUSTOM as there's no equivalent)
        List<ItemStack> netheriteItems = new ArrayList<>();
        netheriteItems.add(XMaterial.NETHERITE_SWORD.parseItem());
        netheriteItems.add(new ItemStack(XMaterial.GOLDEN_APPLE.parseMaterial(), 16));
        netheriteItems.add(new ItemStack(XMaterial.ENDER_PEARL.parseMaterial(), 32));
        netheriteItems.add(new ItemStack(XMaterial.ENCHANTED_GOLDEN_APPLE.parseMaterial(), 4));
        kitItems.put(KitType.CUSTOM, netheriteItems);
        
        List<ItemStack> netheriteArmor = new ArrayList<>();
        netheriteArmor.add(XMaterial.NETHERITE_HELMET.parseItem());
        netheriteArmor.add(XMaterial.NETHERITE_CHESTPLATE.parseItem());
        netheriteArmor.add(XMaterial.NETHERITE_LEGGINGS.parseItem());
        netheriteArmor.add(XMaterial.NETHERITE_BOOTS.parseItem());
        kitArmor.put(KitType.CUSTOM, netheriteArmor);
    }
    
    /**
     * Give kit to player
     */
    public void giveKit(Player player, KitType kitType) {
        // Clear inventory
        player.getInventory().clear();
        
        // Give items
        List<ItemStack> items = kitItems.get(kitType);
        if (items != null) {
            for (ItemStack item : items) {
                player.getInventory().addItem(item.clone());
            }
        }
        
        // Give armor
        List<ItemStack> armor = kitArmor.get(kitType);
        if (armor != null) {
            PlayerInventory inventory = player.getInventory();
            if (armor.size() >= 4) {
                inventory.setHelmet(armor.get(0).clone());
                inventory.setChestplate(armor.get(1).clone());
                inventory.setLeggings(armor.get(2).clone());
                inventory.setBoots(armor.get(3).clone());
            }
        }
        
        // Update inventory
        player.updateInventory();
        
        // Send message
        player.sendMessage(ChatUtils.colorize("&aYou have been given the " + kitType.getDisplayName() + " kit!"));
    }
    
    /**
     * Get kit items
     */
    public List<ItemStack> getKitItems(KitType kitType) {
        return kitItems.getOrDefault(kitType, new ArrayList<>());
    }
    
    /**
     * Get kit armor
     */
    public List<ItemStack> getKitArmor(KitType kitType) {
        return kitArmor.getOrDefault(kitType, new ArrayList<>());
    }
    
    /**
     * Check if kit exists
     */
    public boolean hasKit(KitType kitType) {
        return kitItems.containsKey(kitType) || kitArmor.containsKey(kitType);
    }
    
    /**
     * Get all available kits
     */
    public List<KitType> getAvailableKits() {
        return new ArrayList<>(kitItems.keySet());
    }
    
    /**
     * Get kits (alias for getAvailableKits)
     */
    public List<KitType> getKits() {
        return getAvailableKits();
    }

    /**
     * Find a kit by its name or display name.
     * This provides a minimal adapter for command providers that expect a {@link Kit}.
     *
     * @param name kit identifier or display name (case-insensitive)
     * @return a lightweight Kit instance, or null if no matching KitType exists
     */
    public Kit getKitByName(String name) {
        if (name == null) {
            return null;
        }

        KitType matchedType = null;
        for (KitType type : KitType.values()) {
            if (type.name().equalsIgnoreCase(name) ||
                type.getDisplayName().equalsIgnoreCase(name)) {
                matchedType = type;
                break;
            }
        }

        if (matchedType == null) {
            return null;
        }

        Kit kit = new Kit(matchedType.name());
        kit.setDisplayName(matchedType.getDisplayName());
        return kit;
    }
    
    /**
     * Get kit display name
     */
    public String getKitDisplayName(KitType kitType) {
        return kitType.getDisplayName();
    }
    
    /**
     * Get kit description
     */
    public String getKitDescription(KitType kitType) {
        switch (kitType) {
            case DIAMOND:
                return "High-tier kit with diamond gear and golden apples";
            case IRON:
                return "Mid-tier kit with iron gear and some golden apples";
            case GOLD:
                return "Low-tier kit with gold gear and minimal supplies";
            case STONE:
                return "Basic kit with chainmail gear and basic supplies";
            case WOOD:
                return "Starter kit with leather gear and minimal supplies";
            case CUSTOM:
                return "Custom kit with player-defined items";
            default:
                return "Unknown kit type";
        }
    }
    
    /**
     * Get kit power level
     */
    public int getKitPowerLevel(KitType kitType) {
        switch (kitType) {
            case WOOD: return 1;
            case STONE: return 2;
            case GOLD: return 3;
            case IRON: return 4;
            case DIAMOND: return 5;
            case CUSTOM: return 6; // Netherite kit uses CUSTOM type
            default: return 0;
        }
    }
    
    /**
     * Check if kit is balanced
     */
    public boolean isKitBalanced(KitType kitType) {
        // Check if kit has reasonable power level
        int powerLevel = getKitPowerLevel(kitType);
        return powerLevel > 0 && powerLevel <= 6;
    }
    
    /**
     * Get kit statistics
     */
    public Map<String, Object> getKitStatistics() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("total_kits", kitItems.size());
        stats.put("available_kits", getAvailableKits().size());

        int totalItems = 0;
        for (List<ItemStack> items : kitItems.values()) {
            totalItems += items.size();
        }
        stats.put("total_items", totalItems);

        int totalArmor = 0;
        for (List<ItemStack> armor : kitArmor.values()) {
            totalArmor += armor.size();
        }
        stats.put("total_armor", totalArmor);

        return stats;
    }
}
