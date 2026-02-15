package dev.artixdev.libs.io.github.retrooper.packetevents.manager.server;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerManager;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.PEVersion;

public class ServerManagerImpl implements ServerManager {
   private ServerVersion serverVersion;

   private ServerVersion resolveVersionNoCache() {
      Plugin plugin = (Plugin)PacketEvents.getAPI().getPlugin();
      String bukkitVersion = Bukkit.getBukkitVersion();
      ServerVersion fallbackVersion = ServerVersion.V_1_8_8;
      if (bukkitVersion.contains("Unknown")) {
         return fallbackVersion;
      } else {
         PEVersion version = new PEVersion(bukkitVersion.substring(0, bukkitVersion.indexOf("-")));
         PEVersion latestVersion = new PEVersion(ServerVersion.getLatest().getReleaseName());
         if (version.isNewerThan(latestVersion)) {
            plugin.getLogger().warning("[packetevents] We currently do not support the minecraft version " + version.toString() + ", so things might break. PacketEvents will behave as if the minecraft version were " + latestVersion.toString() + "!");
            return ServerVersion.getLatest();
         } else {
            ServerVersion[] var6 = ServerVersion.reversedValues();
            int var7 = var6.length;

            for(int var8 = 0; var8 < var7; ++var8) {
               ServerVersion val = var6[var8];
               if (bukkitVersion.contains(val.getReleaseName())) {
                  return val;
               }
            }

            plugin.getLogger().warning("[packetevents] Your server software is preventing us from checking the server version. This is what we found: " + Bukkit.getBukkitVersion() + ". We will assume the server version is " + fallbackVersion.name() + "...");
            return fallbackVersion;
         }
      }
   }

   public ServerVersion getVersion() {
      if (this.serverVersion == null) {
         this.serverVersion = this.resolveVersionNoCache();
      }

      return this.serverVersion;
   }
}
