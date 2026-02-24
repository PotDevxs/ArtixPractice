package dev.artixdev.practice.utils.other;

import org.bukkit.inventory.ItemStack;

/**
 * ItemStackUtils
 * 
 * Utility class for ItemStack-related operations.
 * 
 * @author RefineDev
 * @since 1.0
 */
public final class ItemStackUtils {
    
    /**
     * Get a default item stack representation.
     * This method's original implementation was not decompiled.
     * 
     * @return a default item stack, or null
     */
    public static Object getDefaultItemStack() {
        return new ItemStack(org.bukkit.Material.AIR);
    }
    
    /**
     * Private constructor to prevent instantiation.
     * This is a utility class and should not be instantiated.
     * 
     * @throws UnsupportedOperationException always thrown when attempting to instantiate
     */
    private ItemStackUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
