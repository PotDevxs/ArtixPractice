package dev.artixdev.practice.utils.other;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * PotionUtils
 * 
 * Utility class for potion-related operations.
 * 
 * @author RefineDev
 * @since 1.0
 */
public final class PotionUtils {
    
    /**
     * Add a custom potion effect to an item stack.
     * 
     * @param itemStack the item stack to modify
     * @param effectType the potion effect type
     * @param duration the duration in ticks
     * @param amplifier the amplifier level
     * @return the modified item stack
     */
    public static ItemStack addCustomEffect(ItemStack itemStack, PotionEffectType effectType, int duration, int amplifier) {
        if (itemStack == null || effectType == null) {
            return itemStack;
        }
        
        if (!itemStack.hasItemMeta()) {
            return itemStack;
        }
        
        ItemMeta meta = itemStack.getItemMeta();
        if (!(meta instanceof PotionMeta)) {
            return itemStack;
        }
        
        PotionMeta potionMeta = (PotionMeta) meta;
        PotionEffect effect = new PotionEffect(effectType, duration, amplifier);
        potionMeta.addCustomEffect(effect, true);
        itemStack.setItemMeta(potionMeta);
        
        return itemStack;
    }
    
    /**
     * Get the display name of a potion effect type.
     * 
     * @param effectType the potion effect type
     * @return the display name
     */
    public static String getEffectDisplayName(PotionEffectType effectType) {
        if (effectType == null) {
            return "Unknown";
        }
        return effectType.getName();
    }
    
    /**
     * Private constructor to prevent instantiation.
     * This is a utility class and should not be instantiated.
     * 
     * @throws UnsupportedOperationException always thrown when attempting to instantiate
     */
    private PotionUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }
}
