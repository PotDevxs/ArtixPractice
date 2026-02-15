package dev.artixdev.libs.com.github.retrooper.packetevents.util;

import java.util.logging.Level;
import java.util.regex.Pattern;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.net.kyori.adventure.text.format.NamedTextColor;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class LogManager {
   private static final Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)§[0-9A-FK-ORX]");

   protected void log(Level level, @Nullable NamedTextColor color, String message) {
      message = STRIP_COLOR_PATTERN.matcher(message).replaceAll("");
      PacketEvents.getAPI().getLogger().log(level, color != null ? color.toString() : "" + message);
   }

   public void info(String message) {
      this.log(Level.INFO, (NamedTextColor)null, message);
   }

   public void warn(String message) {
      this.log(Level.WARNING, (NamedTextColor)null, message);
   }

   public void severe(String message) {
      this.log(Level.SEVERE, (NamedTextColor)null, message);
   }

   public void debug(String message) {
      if (PacketEvents.getAPI().getSettings().isDebugEnabled()) {
         this.log(Level.FINE, (NamedTextColor)null, message);
      }

   }
}
