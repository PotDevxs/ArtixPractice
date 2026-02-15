package dev.artixdev.api.practice.menu.util;

import org.bukkit.ChatColor;

public class CC {
   public static final String MENU_BAR;

   public static String translate(String in) {
      return ChatColor.translateAlternateColorCodes('&', in);
   }

   static {
      MENU_BAR = ChatColor.GRAY + ChatColor.STRIKETHROUGH.toString() + "------------------------";
   }
}
