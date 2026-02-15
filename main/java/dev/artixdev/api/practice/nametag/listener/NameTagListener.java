package dev.artixdev.api.practice.nametag.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import dev.artixdev.api.practice.nametag.NameTagHandler;

public final class NameTagListener implements Listener {
   private static boolean firstJoin;
   private final NameTagHandler handler;

   @EventHandler(
      priority = EventPriority.HIGH
   )
   public void onPlayerJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      Bukkit.getScheduler().runTaskLater(this.handler.getPlugin(), () -> {
         if (!firstJoin) {
            this.handler.createTeams(player);
            firstJoin = true;
         } else {
            this.handler.initiatePlayer(player);
         }

         this.handler.reloadPlayer(player);
         this.handler.reloadOthersFor(player);
      }, 20L);
   }

   @EventHandler
   public void onPlayerQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      this.handler.unloadPlayer(player);
   }

   public NameTagListener(NameTagHandler handler) {
      this.handler = handler;
   }
}
