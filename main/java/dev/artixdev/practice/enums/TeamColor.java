package dev.artixdev.practice.enums;

import org.bukkit.inventory.ItemStack;
import dev.artixdev.practice.utils.ItemBuilder;

/**
 * Team Color Enum
 * Enum representing different team colors
 */
public enum TeamColor {
   
   BLUE("Blue", "§9"),
   RED("Red", "§c"),
   GREEN("Green", "§a"),
   YELLOW("Yellow", "§e"),
   PURPLE("Purple", "§5"),
   ORANGE("Orange", "§6"),
   PINK("Pink", "§d"),
   GRAY("Gray", "§7"),
   CYAN("Cyan", "§b"),
   LIME("Lime", "§a"),
   MAGENTA("Magenta", "§d"),
   LIGHT_BLUE("Light Blue", "§b"),
   LIGHT_GRAY("Light Gray", "§7"),
   WHITE("White", "§f"),
   BLACK("Black", "§0");
   
   private final String name;
   private final String colorCode;
   
   TeamColor(String name, String colorCode) {
      this.name = name;
      this.colorCode = colorCode;
   }
   
   /**
    * Get team name
    * @return team name
    */
   public String getName() {
      return name;
   }
   
   /**
    * Get color code
    * @return color code
    */
   public String getColorCode() {
      return colorCode;
   }
   
   /**
    * Get colored name
    * @return colored name
    */
   public String getColoredName() {
      return colorCode + name;
   }
   
   /**
    * Get team item
    * @return team item
    */
   public ItemStack getTeamItem() {
      return new ItemBuilder(org.bukkit.Material.WOOL)
         .name(getColoredName())
         .lore("", "§7Click to join this team!")
         .build();
   }
   
   /**
    * Get team by name
    * @param name the name
    * @return team color or null
    */
   public static TeamColor getByName(String name) {
      for (TeamColor color : values()) {
         if (color.getName().equalsIgnoreCase(name)) {
            return color;
         }
      }
      return null;
   }
   
   /**
    * Get team by color code
    * @param colorCode the color code
    * @return team color or null
    */
   public static TeamColor getByColorCode(String colorCode) {
      for (TeamColor color : values()) {
         if (color.getColorCode().equals(colorCode)) {
            return color;
         }
      }
      return null;
   }
   
   /**
    * Get random team color
    * @return random team color
    */
   public static TeamColor getRandom() {
      TeamColor[] colors = values();
      return colors[(int) (Math.random() * colors.length)];
   }
   
   /**
    * Get team colors count
    * @return team colors count
    */
   public static int getCount() {
      return values().length;
   }
   
   @Override
   public String toString() {
      return getColoredName();
   }
}
