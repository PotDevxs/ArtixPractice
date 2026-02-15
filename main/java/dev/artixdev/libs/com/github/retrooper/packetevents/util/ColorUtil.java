package dev.artixdev.libs.com.github.retrooper.packetevents.util;

import dev.artixdev.libs.net.kyori.adventure.text.format.NamedTextColor;

public class ColorUtil {
   public static String toString(NamedTextColor color) {
      String prefix = "§";
      if (color == null) {
         return prefix + "f";
      } else {
         String var2 = color.toString();
         byte var3 = -1;
         switch(var2.hashCode()) {
         case -1852648987:
            if (var2.equals("dark_aqua")) {
               var3 = 3;
            }
            break;
         case -1852623997:
            if (var2.equals("dark_blue")) {
               var3 = 1;
            }
            break;
         case -1852469876:
            if (var2.equals("dark_gray")) {
               var3 = 8;
            }
            break;
         case -1846156123:
            if (var2.equals("dark_purple")) {
               var3 = 5;
            }
            break;
         case -1591987974:
            if (var2.equals("dark_green")) {
               var3 = 2;
            }
            break;
         case -734239628:
            if (var2.equals("yellow")) {
               var3 = 14;
            }
            break;
         case 112785:
            if (var2.equals("red")) {
               var3 = 12;
            }
            break;
         case 3002044:
            if (var2.equals("aqua")) {
               var3 = 11;
            }
            break;
         case 3027034:
            if (var2.equals("blue")) {
               var3 = 9;
            }
            break;
         case 3178592:
            if (var2.equals("gold")) {
               var3 = 6;
            }
            break;
         case 3181155:
            if (var2.equals("gray")) {
               var3 = 7;
            }
            break;
         case 93818879:
            if (var2.equals("black")) {
               var3 = 0;
            }
            break;
         case 98619139:
            if (var2.equals("green")) {
               var3 = 10;
            }
            break;
         case 113101865:
            if (var2.equals("white")) {
               var3 = 15;
            }
            break;
         case 1331038981:
            if (var2.equals("light_purple")) {
               var3 = 13;
            }
            break;
         case 1741368392:
            if (var2.equals("dark_red")) {
               var3 = 4;
            }
         }

         switch(var3) {
         case 0:
            return prefix + "0";
         case 1:
            return prefix + "1";
         case 2:
            return prefix + "2";
         case 3:
            return prefix + "3";
         case 4:
            return prefix + "4";
         case 5:
            return prefix + "5";
         case 6:
            return prefix + "6";
         case 7:
            return prefix + "7";
         case 8:
            return prefix + "8";
         case 9:
            return prefix + "9";
         case 10:
            return prefix + "a";
         case 11:
            return prefix + "b";
         case 12:
            return prefix + "c";
         case 13:
            return prefix + "d";
         case 14:
            return prefix + "e";
         case 15:
            return prefix + "f";
         default:
            return prefix + "f";
         }
      }
   }

   public static int getId(NamedTextColor color) {
      if (color == null) {
         return -1;
      } else {
         String var1 = color.toString();
         byte var2 = -1;
         switch(var1.hashCode()) {
         case -1852648987:
            if (var1.equals("dark_aqua")) {
               var2 = 3;
            }
            break;
         case -1852623997:
            if (var1.equals("dark_blue")) {
               var2 = 1;
            }
            break;
         case -1852469876:
            if (var1.equals("dark_gray")) {
               var2 = 8;
            }
            break;
         case -1846156123:
            if (var1.equals("dark_purple")) {
               var2 = 5;
            }
            break;
         case -1591987974:
            if (var1.equals("dark_green")) {
               var2 = 2;
            }
            break;
         case -734239628:
            if (var1.equals("yellow")) {
               var2 = 14;
            }
            break;
         case 112785:
            if (var1.equals("red")) {
               var2 = 12;
            }
            break;
         case 3002044:
            if (var1.equals("aqua")) {
               var2 = 11;
            }
            break;
         case 3027034:
            if (var1.equals("blue")) {
               var2 = 9;
            }
            break;
         case 3178592:
            if (var1.equals("gold")) {
               var2 = 6;
            }
            break;
         case 3181155:
            if (var1.equals("gray")) {
               var2 = 7;
            }
            break;
         case 93818879:
            if (var1.equals("black")) {
               var2 = 0;
            }
            break;
         case 98619139:
            if (var1.equals("green")) {
               var2 = 10;
            }
            break;
         case 113101865:
            if (var1.equals("white")) {
               var2 = 15;
            }
            break;
         case 1331038981:
            if (var1.equals("light_purple")) {
               var2 = 13;
            }
            break;
         case 1741368392:
            if (var1.equals("dark_red")) {
               var2 = 4;
            }
         }

         switch(var2) {
         case 0:
            return 0;
         case 1:
            return 1;
         case 2:
            return 2;
         case 3:
            return 3;
         case 4:
            return 4;
         case 5:
            return 5;
         case 6:
            return 6;
         case 7:
            return 7;
         case 8:
            return 8;
         case 9:
            return 9;
         case 10:
            return 10;
         case 11:
            return 11;
         case 12:
            return 12;
         case 13:
            return 13;
         case 14:
            return 14;
         case 15:
            return 15;
         default:
            return 15;
         }
      }
   }

   public static NamedTextColor fromId(int id) {
      if (id < 0) {
         return null;
      } else {
         switch(id) {
         case 0:
            return NamedTextColor.BLACK;
         case 1:
            return NamedTextColor.DARK_BLUE;
         case 2:
            return NamedTextColor.DARK_GREEN;
         case 3:
            return NamedTextColor.DARK_AQUA;
         case 4:
            return NamedTextColor.DARK_RED;
         case 5:
            return NamedTextColor.DARK_PURPLE;
         case 6:
            return NamedTextColor.GOLD;
         case 7:
            return NamedTextColor.GRAY;
         case 8:
            return NamedTextColor.DARK_GRAY;
         case 9:
            return NamedTextColor.BLUE;
         case 10:
            return NamedTextColor.GREEN;
         case 11:
            return NamedTextColor.AQUA;
         case 12:
            return NamedTextColor.RED;
         case 13:
            return NamedTextColor.LIGHT_PURPLE;
         case 14:
            return NamedTextColor.YELLOW;
         case 15:
            return NamedTextColor.WHITE;
         default:
            return NamedTextColor.WHITE;
         }
      }
   }
}
