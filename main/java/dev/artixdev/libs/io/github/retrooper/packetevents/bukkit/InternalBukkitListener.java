package dev.artixdev.libs.io.github.retrooper.packetevents.bukkit;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.User;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.FakeChannelUtil;
import dev.artixdev.libs.io.github.retrooper.packetevents.injector.SpigotChannelInjector;
import dev.artixdev.libs.io.github.retrooper.packetevents.util.FoliaCompatUtil;

public class InternalBukkitListener implements Listener {
   private final Plugin plugin;

   public InternalBukkitListener(Plugin plugin) {
      this.plugin = plugin;
   }

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onJoin(PlayerJoinEvent e) {
      Player player = e.getPlayer();
      SpigotChannelInjector injector = (SpigotChannelInjector)PacketEvents.getAPI().getInjector();
      User user = PacketEvents.getAPI().getPlayerManager().getUser(player);
      if (user == null) {
         Object channel = PacketEvents.getAPI().getPlayerManager().getChannel(player);
         if (!FakeChannelUtil.isFakeChannel(channel)) {
            FoliaCompatUtil.runTaskForEntity(player, this.plugin, () -> {
               player.kickPlayer("PacketEvents 2.0 failed to inject");
            }, (Runnable)null, 0L);
         }

      } else {
         injector.updatePlayer(user, player);
      }
   }
}
