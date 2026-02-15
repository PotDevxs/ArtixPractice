package dev.artixdev.practice.utils;

import com.google.common.base.Strings;
import org.bukkit.ChatColor;

public class ProgressBar {
    
    public static final int DEFAULT_LENGTH = 40;

    public static String createProgressBar(int current, int max, int length, char character, ChatColor filledColor, ChatColor emptyColor) {
        if (max <= 0) return "";
        
        float percentage = (float) current / max;
        int filledLength = (int) (percentage * length);
        
        String filled = Strings.repeat(String.valueOf(character), filledLength);
        String empty = Strings.repeat(String.valueOf(character), length - filledLength);
        
        return filledColor + filled + emptyColor + empty;
    }

    public static String createProgressBar(int current, int max) {
        return createProgressBar(current, max, DEFAULT_LENGTH, '▎', ChatColor.GREEN, ChatColor.GRAY);
    }

    public static String createHealthBar(int current, int max) {
        return createProgressBar(current, max, 20, '|', ChatColor.RED, ChatColor.GRAY);
    }

    public static String createExperienceBar(int current, int max) {
        return createProgressBar(current, max, 20, '|', ChatColor.GREEN, ChatColor.GRAY);
    }
}
