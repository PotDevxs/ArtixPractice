package dev.artixdev.libs.io.github.retrooper.packetevents.util;

import java.util.logging.Level;
import org.bukkit.Bukkit;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.ColorUtil;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.LogManager;
import dev.artixdev.libs.net.kyori.adventure.text.format.NamedTextColor;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public class BukkitLogManager extends LogManager {
   private final String prefixText;

   public BukkitLogManager() {
      this.prefixText = ColorUtil.toString(NamedTextColor.AQUA) + "[packetevents] " + ColorUtil.toString(NamedTextColor.WHITE);
   }

   protected void log(Level level, @Nullable NamedTextColor color, String message) {
      Bukkit.getConsoleSender().sendMessage(this.prefixText + ColorUtil.toString(color) + message);
   }

   public void info(String message) {
      this.log(Level.INFO, NamedTextColor.WHITE, message);
   }

   public void warn(String message) {
      this.log(Level.WARNING, NamedTextColor.YELLOW, message);
   }

   public void severe(String message) {
      this.log(Level.SEVERE, NamedTextColor.RED, message);
   }

   public void debug(String message) {
      if (PacketEvents.getAPI().getSettings().isDebugEnabled()) {
         this.log(Level.FINE, NamedTextColor.GRAY, message);
      }

   }
}
