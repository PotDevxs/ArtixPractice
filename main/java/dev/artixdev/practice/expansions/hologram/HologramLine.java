package dev.artixdev.practice.expansions.hologram;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * Represents a single line of a hologram (text or item).
 */
public class HologramLine {

    private String text;
    private ItemStack item;
    private int slot;
    private Player player;
    private boolean valid = true;

    public HologramLine(ItemStack item) {
        this.item = item;
    }

    public HologramLine(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Refresh this line on the display (stub; override in implementations).
     */
    public void update() {
        // Stub: NMS/API-specific refresh
    }

    /**
     * Remove this line from the display.
     */
    public void remove() {
        this.valid = false;
    }

    /**
     * Whether this line is still valid (not removed).
     */
    public boolean isValid() {
        return valid;
    }
}
