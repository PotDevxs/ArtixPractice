package dev.artixdev.practice.enums;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.practice.utils.ItemBuilder;

public enum KitType {
    
    // Basic Kits
    DIAMOND("Diamond", new ItemBuilder(Material.DIAMOND_SWORD).build()),
    IRON("Iron", new ItemBuilder(Material.IRON_SWORD).build()),
    GOLD("Gold", new ItemBuilder(Material.GOLD_SWORD).build()),
    STONE("Stone", new ItemBuilder(Material.STONE_SWORD).build()),
    WOOD("Wood", new ItemBuilder(Material.WOOD_SWORD).build()),
    
    // Special Kits
    ARCHER("Archer", new ItemBuilder(Material.BOW).build()),
    AXE("Axe", new ItemBuilder(Material.DIAMOND_AXE).build()),
    ROD("Rod", new ItemBuilder(Material.FISHING_ROD).build()),
    SOUP("Soup", new ItemBuilder(Material.MUSHROOM_SOUP).build()),
    POTION("Potion", new ItemBuilder(Material.POTION).build()),
    
    // Practice Kits
    SUMO("Sumo", new ItemBuilder(Material.SLIME_BLOCK).build()),
    BRIDGES("Bridges", new ItemBuilder(Material.SANDSTONE).build()),
    SPLIT("Split", new ItemBuilder(Material.IRON_FENCE).build()),
    COMBO("Combo", new ItemBuilder(Material.DIAMOND_SWORD).build()),
    HCTEAMS("HCTeams", new ItemBuilder(Material.DIAMOND_CHESTPLATE).build()),
    
    // Custom Kits
    CUSTOM("Custom", new ItemBuilder(Material.NAME_TAG).build()),
    BUILD("Build", new ItemBuilder(Material.BRICK).build()),
    PARKOUR("Parkour", new ItemBuilder(Material.LEATHER_BOOTS).build()),
    ELYTRA("Elytra", new ItemBuilder(Material.FEATHER).build()),
    FFA("FFA", new ItemBuilder(Material.DIAMOND_SWORD).build()),
    
    // Event Kits
    EVENT("Event", new ItemBuilder(Material.NETHER_STAR).build()),
    TOURNAMENT("Tournament", new ItemBuilder(Material.GOLD_INGOT).build()),
    RANKED("Ranked", new ItemBuilder(Material.EMERALD).build()),
    UNRANKED("Unranked", new ItemBuilder(Material.IRON_INGOT).build()),
    PRIVATE("Private", new ItemBuilder(Material.ENDER_PEARL).build()),
    BOXING("Boxing", new ItemBuilder(Material.STICK).build());

    private final String name;
    private final ItemStack icon;

    KitType(String name, ItemStack icon) {
        this.name = name;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }
    
    public String getDisplayName() {
        return name;
    }
    
    public String getDescription() {
        return "Kit type: " + name;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public static KitType fromName(String name) {
        for (KitType type : values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    public boolean isRanked() {
        return this == RANKED || this == TOURNAMENT;
    }

    public boolean isUnranked() {
        return this == UNRANKED || this == PRIVATE;
    }

    public boolean isSpecial() {
        return this == SUMO || this == BRIDGES || this == SPLIT || this == COMBO;
    }

    public boolean isPractice() {
        return this == DIAMOND || this == IRON || this == GOLD || this == STONE || this == WOOD;
    }

    public boolean isCustom() {
        return this == CUSTOM || this == BUILD || this == PARKOUR || this == ELYTRA || this == FFA;
    }

    public boolean isEvent() {
        return this == EVENT || this == TOURNAMENT;
    }

    @Override
    public String toString() {
        return name;
    }
}