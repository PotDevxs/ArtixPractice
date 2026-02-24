package dev.artixdev.api.practice.nametag.util;

import com.google.common.collect.ImmutableMap;
import java.awt.Color;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.ChatColor;
import dev.artixdev.api.practice.nametag.NameTagHandler;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.adventure.AdventureSerializer;
import dev.artixdev.libs.net.kyori.adventure.text.Component;
import dev.artixdev.libs.net.kyori.adventure.text.format.NamedTextColor;
import dev.artixdev.libs.net.kyori.adventure.text.format.TextColor;

public final class ColorUtil {
   private static final Logger log = LogManager.getLogger(ColorUtil.class);
   private static final Map<ChatColor, Color> COLOR_MAPPINGS;
   private static final Pattern hexPattern;

   /** Gets java.awt.Color from Bungee ChatColor via reflection (getColor() may not exist in older API). */
   private static Color getBungeeColor(net.md_5.bungee.api.ChatColor bungeeColor) {
      if (bungeeColor == null) return null;
      try {
         Object color = bungeeColor.getClass().getMethod("getColor").invoke(bungeeColor);
         return color instanceof Color ? (Color) color : null;
      } catch (Exception e) {
         return null;
      }
   }

   public static NamedTextColor getLastColor(String input) {
      ChatColor color = getLastColors(input);
      NamedTextColor textColor = NamedTextColor.WHITE;
      if (color == null) {
         return textColor;
      } else {
         net.md_5.bungee.api.ChatColor md5Color = color.asBungee();
         if (NameTagHandler.getInstance().isDebugMode()) {
            log.info("Last color is {} " + getRaw(md5Color.toString()));
         }

         Color awtColor = getBungeeColor(md5Color);
         if (awtColor == null) {
            awtColor = COLOR_MAPPINGS.get(color);
         }
         if (awtColor == null) {
            if (NameTagHandler.getInstance().isDebugMode()) {
               log.info("MD5 color was null, {} " + getRaw(md5Color.toString()));
            }

            return textColor;
         } else {
            TextColor parsed = TextColor.color(awtColor.getRed(), awtColor.getGreen(), awtColor.getBlue());
            return NamedTextColor.nearestTo(parsed);
         }
      }
   }

   public static ChatColor getLastColors(String input) {
      String prefixColor = ChatColor.getLastColors(color(input));
      if (prefixColor.isEmpty()) {
         return null;
      } else {
         ChatColor color;
         if (VersionUtil.canHex()) {
            try {
               String hexColor = prefixColor.replace("§", "").replace("x", "#");
               if (hexColor.length() == 7 && hexColor.charAt(0) == '#') {
                  Color awtColor = new Color(Integer.parseInt(hexColor.substring(1), 16));
                  color = getClosestChatColor(awtColor);
               } else {
                  ChatColor bukkitColor = ChatColor.getByChar(prefixColor.substring(prefixColor.length() - 1).charAt(0));
                  color = bukkitColor;
               }
            } catch (Exception e) {
               ChatColor bukkitColor = ChatColor.getByChar(prefixColor.substring(prefixColor.length() - 1).charAt(0));
               if (bukkitColor == null) {
                  return null;
               }

               color = bukkitColor;
            }
         } else {
            ChatColor bukkitColor = ChatColor.getByChar(prefixColor.substring(prefixColor.length() - 1).charAt(0));
            if (bukkitColor == null) {
               return null;
            }

            color = bukkitColor;
         }

         return color;
      }
   }

   public static String color(String text) {
      if (text == null) {
         return "";
      } else {
         if (VersionUtil.canHex()) {
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

         text = ChatColor.translateAlternateColorCodes('&', text);
         return text;
      }
   }

   public static Component translate(String string) {
      return AdventureSerializer.fromLegacyFormat(color(string));
   }

   public static String getRaw(String string) {
      return string.replace("§", "&");
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

   public static ChatColor getClosestChatColor(Color color) {
      ChatColor closest = null;
      int mark = 0;
      Iterator var3 = COLOR_MAPPINGS.entrySet().iterator();

      while(true) {
         ChatColor key;
         int diff;
         do {
            if (!var3.hasNext()) {
               return closest;
            }

            Entry<ChatColor, Color> entry = (Entry)var3.next();
            key = (ChatColor)entry.getKey();
            Color value = (Color)entry.getValue();
            diff = getDiff(value, color);
         } while(closest != null && diff >= mark);

         closest = key;
         mark = diff;
      }
   }

   private static int getDiff(Color color, Color compare) {
      int a = color.getAlpha() - compare.getAlpha();
      int r = color.getRed() - compare.getRed();
      int g = color.getGreen() - compare.getGreen();
      int b = color.getBlue() - compare.getBlue();
      return a * a + r * r + g * g + b * b;
   }

   private ColorUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   static {
      COLOR_MAPPINGS = ImmutableMap.<ChatColor, Color>builder().put(ChatColor.BLACK, new Color(0, 0, 0)).put(ChatColor.DARK_BLUE, new Color(0, 0, 170)).put(ChatColor.DARK_GREEN, new Color(0, 170, 0)).put(ChatColor.DARK_AQUA, new Color(0, 170, 170)).put(ChatColor.DARK_RED, new Color(170, 0, 0)).put(ChatColor.DARK_PURPLE, new Color(170, 0, 170)).put(ChatColor.GOLD, new Color(255, 170, 0)).put(ChatColor.GRAY, new Color(170, 170, 170)).put(ChatColor.DARK_GRAY, new Color(85, 85, 85)).put(ChatColor.BLUE, new Color(85, 85, 255)).put(ChatColor.GREEN, new Color(85, 255, 85)).put(ChatColor.AQUA, new Color(85, 255, 255)).put(ChatColor.RED, new Color(255, 85, 85)).put(ChatColor.LIGHT_PURPLE, new Color(255, 85, 255)).put(ChatColor.YELLOW, new Color(255, 255, 85)).put(ChatColor.WHITE, new Color(255, 255, 255)).build();
      hexPattern = Pattern.compile("&#[A-Fa-f0-9]{6}");
   }
}
