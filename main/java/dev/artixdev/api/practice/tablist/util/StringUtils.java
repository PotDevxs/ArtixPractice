package dev.artixdev.api.practice.tablist.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

public final class StringUtils {
   public static final String VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
   public static final int MINOR_VERSION;
   private static final Pattern hexPattern;

   public static String[] split(String text) {
      if (text.length() <= 16) {
         return new String[]{text, ""};
      } else {
         String prefix = text.substring(0, 16);
         String suffix;
         if (prefix.charAt(15) != 167 && prefix.charAt(15) != '&') {
            if (prefix.charAt(14) != 167 && prefix.charAt(14) != '&') {
               suffix = getLastColors(prefix) + text.substring(16);
            } else {
               prefix = prefix.substring(0, 14);
               suffix = text.substring(14);
            }
         } else {
            prefix = prefix.substring(0, 15);
            suffix = text.substring(15);
         }

         return new String[]{prefix, suffix};
      }
   }

   public static ChatColor getLastColors(String input) {
      String prefixColor = org.bukkit.ChatColor.getLastColors(color(input));
      if (prefixColor.isEmpty()) {
         return null;
      } else {
         ChatColor color;
         if (MINOR_VERSION >= 16) {
            String hexColor = prefixColor.replace("§", "").replace("x", "#");
            if (hexColor.length() == 7 && hexColor.charAt(0) == '#') {
               color = null;
            } else {
               try {
                  org.bukkit.ChatColor bukkitColor = org.bukkit.ChatColor.getByChar(prefixColor.substring(prefixColor.length() - 1).charAt(0));
                  if (bukkitColor == null) {
                     return null;
                  }
                  color = bukkitColor.asBungee();
               } catch (Exception e) {
                  return null;
               }
            }
         } else {
            org.bukkit.ChatColor bukkitColor = org.bukkit.ChatColor.getByChar(prefixColor.substring(prefixColor.length() - 1).charAt(0));
            if (bukkitColor == null) {
               return null;
            }

            color = bukkitColor.asBungee();
         }

         return color;
      }
   }

   public static String color(String text) {
      if (text == null) {
         return "";
      } else {
         text = org.bukkit.ChatColor.translateAlternateColorCodes('&', text);
         if (MINOR_VERSION >= 16) {
            Matcher matcher = hexPattern.matcher(text);

            while(matcher.find()) {
               try {
                  String color = matcher.group();
                  String hexColor = color.replace("&", "").replace("x", "#");
                  String hexCode = buildHexCode(hexColor);
                  text = text.replace(color, hexCode);
               } catch (Exception e) {
               }
            }
         }

         return text;
      }
   }

   /** Builds the 1.16+ hex color code string (§x§R§R§G§G§B§B) from a #RRGGBB hex string. */
   private static String buildHexCode(String hexColor) {
      if (hexColor == null || hexColor.length() != 7 || hexColor.charAt(0) != '#') {
         return "";
      }
      StringBuilder sb = new StringBuilder("\u00A7x");
      for (char c : hexColor.substring(1).toCharArray()) {
         sb.append('\u00A7').append(c);
      }
      return sb.toString();
   }

   private StringUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   static {
      MINOR_VERSION = Integer.parseInt(VERSION.split("_")[1]);
      hexPattern = Pattern.compile("&#[A-Fa-f0-9]{6}");
   }
}
