package dev.artixdev.practice.utils;

import org.bukkit.ChatColor;

/**
 * Chat Utils
 * Utility class for chat formatting
 */
public class ChatUtils {
    
    /**
     * Colorize text with Minecraft color codes
     */
    public static String colorize(String text) {
        if (text == null) {
            return "";
        }
        
        return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    /**
     * Strip color codes from text
     */
    public static String stripColor(String text) {
        if (text == null) {
            return "";
        }
        
        return ChatColor.stripColor(text);
    }
    
    /**
     * Format text with placeholders
     */
    public static String format(String text, String... placeholders) {
        if (text == null || placeholders == null) {
            return text;
        }
        
        String result = text;
        for (int i = 0; i < placeholders.length; i += 2) {
            if (i + 1 < placeholders.length) {
                result = result.replace(placeholders[i], placeholders[i + 1]);
            }
        }
        
        return result;
    }

    public static String translate(String message) {
        return colorize(message);
    }
}