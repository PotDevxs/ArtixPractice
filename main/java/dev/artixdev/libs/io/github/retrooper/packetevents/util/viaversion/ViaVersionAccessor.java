package dev.artixdev.libs.io.github.retrooper.packetevents.util.viaversion;

import org.bukkit.entity.Player;

public interface ViaVersionAccessor {
   int getProtocolVersion(Player var1);

   Class<?> getUserConnectionClass();

   Class<?> getBukkitDecodeHandlerClass();

   Class<?> getBukkitEncodeHandlerClass();
}
