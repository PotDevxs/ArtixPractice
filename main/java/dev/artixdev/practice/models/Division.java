package dev.artixdev.practice.models;

import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.command.util.CC;
import dev.artixdev.libs.com.google.gson.annotations.SerializedName;

/**
 * Division
 * Model for player division/rank
 * This is an alias for Rank to maintain compatibility
 */
public class Division {
    
    @SerializedName("name")
    private final String name;
    @SerializedName("displayName")
    private final String displayName;
    @SerializedName("priority")
    private final int priority;
    @SerializedName("color")
    private final ChatColor color;
    @SerializedName("durability")
    private final int durability;
    @SerializedName("icon")
    private final ItemStack icon;
    @SerializedName("eloNeeded")
    private final int eloNeeded;

    public Division(String name, String displayName, ChatColor color, ItemStack icon, int eloNeeded, int priority, int durability) {
        this.name = name;
        this.displayName = displayName;
        this.color = color;
        this.icon = icon;
        this.eloNeeded = eloNeeded;
        this.priority = priority;
        this.durability = durability;
    }
    
    /**
     * Create Division from Rank
     * @param rank the rank
     */
    public Division(Rank rank) {
        this.name = rank.getName();
        this.displayName = rank.getDisplayName();
        this.color = rank.getColor();
        this.icon = rank.getIcon();
        this.eloNeeded = rank.getEloNeeded();
        this.priority = rank.getPriority();
        this.durability = rank.getDurability();
    }

    public int getDurability() {
        return this.durability;
    }

    public ItemStack getIcon() {
        return this.icon;
    }

    public boolean hasHigherPriority(Division other) {
        return this.priority >= other.getPriority();
    }

    public boolean hasLowerPriority(Division other) {
        return this.priority <= other.getPriority();
    }

    public String getName() {
        return this.name;
    }
    
    public String getDisplayName() {
        return this.displayName;
    }

    public ChatColor getColor() {
        return this.color;
    }

    public int getEloNeeded() {
        return this.eloNeeded;
    }

    public int getPriority() {
        return this.priority;
    }

    public String getFormattedName() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.color);
        builder.append(this.displayName);
        return CC.translate(builder.toString());
    }
}
