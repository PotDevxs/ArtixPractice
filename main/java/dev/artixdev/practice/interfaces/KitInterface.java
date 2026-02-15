package dev.artixdev.practice.interfaces;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface KitInterface {
    
    int MAX_KITS = 50;
    boolean DEFAULT_ENABLED = false;

    /**
     * Apply this kit to the specified player
     * @param player the player to apply the kit to
     */
    void apply(Player player);

    /**
     * Get the display item for this kit
     * @return the ItemStack representing this kit
     */
    ItemStack getDisplayItem();
}
