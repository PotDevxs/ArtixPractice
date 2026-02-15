package dev.artixdev.practice.enums;

import org.bukkit.Color;

/**
 * Chat Color Enum
 * Defines different chat colors and their properties
 */
public enum ChatColor {
   BLACK("&0", "Black", Color.BLACK),
   DARK_BLUE("&1", "Dark Blue", Color.fromRGB(0, 0, 170)),
   DARK_GREEN("&2", "Dark Green", Color.fromRGB(0, 170, 0)),
   DARK_AQUA("&3", "Dark Aqua", Color.fromRGB(0, 170, 170)),
   DARK_RED("&4", "Dark Red", Color.fromRGB(170, 0, 0)),
   DARK_PURPLE("&5", "Dark Purple", Color.fromRGB(170, 0, 170)),
   GOLD("&6", "Gold", Color.fromRGB(255, 170, 0)),
   GRAY("&7", "Gray", Color.fromRGB(170, 170, 170)),
   DARK_GRAY("&8", "Dark Gray", Color.fromRGB(85, 85, 85)),
   BLUE("&9", "Blue", Color.fromRGB(85, 85, 255)),
   GREEN("&a", "Green", Color.fromRGB(85, 255, 85)),
   AQUA("&b", "Aqua", Color.fromRGB(85, 255, 255)),
   RED("&c", "Red", Color.fromRGB(255, 85, 85)),
   LIGHT_PURPLE("&d", "Light Purple", Color.fromRGB(255, 85, 255)),
   YELLOW("&e", "Yellow", Color.fromRGB(255, 255, 85)),
   WHITE("&f", "White", Color.fromRGB(255, 255, 255)),
   MAGIC("&k", "Magic", null),
   BOLD("&l", "Bold", null),
   STRIKETHROUGH("&m", "Strikethrough", null),
   UNDERLINE("&n", "Underline", null),
   ITALIC("&o", "Italic", null),
   RESET("&r", "Reset", null);
   
   private final String code;
   private final String name;
   private final Color color;
   
   ChatColor(String code, String name, Color color) {
      this.code = code;
      this.name = name;
      this.color = color;
   }
   
   public String getCode() {
      return code;
   }
   
   public String getName() {
      return name;
   }
   
   public Color getColor() {
      return color;
   }
   
   /**
    * Check if color is a formatting code
    * @return true if formatting
    */
   public boolean isFormatting() {
      return this == MAGIC || this == BOLD || this == STRIKETHROUGH || 
             this == UNDERLINE || this == ITALIC || this == RESET;
   }
   
   /**
    * Check if color is a color code
    * @return true if color
    */
   public boolean isColor() {
      return !isFormatting();
   }
   
   /**
    * Get color from code
    * @param code the code
    * @return color or null
    */
   public static ChatColor fromCode(String code) {
      if (code == null) {
         return null;
      }
      
      for (ChatColor color : values()) {
         if (color.getCode().equals(code)) {
            return color;
         }
      }
      return null;
   }
   
   /**
    * Get color from name
    * @param name the name
    * @return color or null
    */
   public static ChatColor fromName(String name) {
      if (name == null) {
         return null;
      }
      
      for (ChatColor color : values()) {
         if (color.getName().equalsIgnoreCase(name)) {
            return color;
         }
      }
      return null;
   }
   
   /**
    * Get all color codes
    * @return array of color codes
    */
   public static ChatColor[] getColors() {
      return new ChatColor[]{
         BLACK, DARK_BLUE, DARK_GREEN, DARK_AQUA, DARK_RED, DARK_PURPLE,
         GOLD, GRAY, DARK_GRAY, BLUE, GREEN, AQUA, RED, LIGHT_PURPLE,
         YELLOW, WHITE
      };
   }
   
   /**
    * Get all formatting codes
    * @return array of formatting codes
    */
   public static ChatColor[] getFormatting() {
      return new ChatColor[]{
         MAGIC, BOLD, STRIKETHROUGH, UNDERLINE, ITALIC, RESET
      };
   }
   
   /**
    * Get all codes
    * @return array of all codes
    */
   public static ChatColor[] getAll() {
      return values();
   }
   
   /**
    * Check if code is valid
    * @param code the code
    * @return true if valid
    */
   public static boolean isValidCode(String code) {
      return fromCode(code) != null;
   }
   
   /**
    * Get random color
    * @return random color
    */
   public static ChatColor getRandomColor() {
      ChatColor[] colors = getColors();
      return colors[(int) (Math.random() * colors.length)];
   }
   
   /**
    * Get color by index
    * @param index the index
    * @return color or null
    */
   public static ChatColor getByIndex(int index) {
      ChatColor[] colors = getColors();
      if (index >= 0 && index < colors.length) {
         return colors[index];
      }
      return null;
   }
   
   /**
    * Get color count
    * @return color count
    */
   public static int getColorCount() {
      return getColors().length;
   }
   
   /**
    * Get formatting count
    * @return formatting count
    */
   public static int getFormattingCount() {
      return getFormatting().length;
   }
   
   /**
    * Get total count
    * @return total count
    */
   public static int getTotalCount() {
      return values().length;
   }
   
   @Override
   public String toString() {
      return code;
   }
}
