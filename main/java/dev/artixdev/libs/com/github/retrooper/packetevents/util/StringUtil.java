package dev.artixdev.libs.com.github.retrooper.packetevents.util;

public class StringUtil {
   public static String maximizeLength(String msg, int maxLength) {
      return msg.length() > maxLength ? msg.substring(0, maxLength) : msg;
   }
}
