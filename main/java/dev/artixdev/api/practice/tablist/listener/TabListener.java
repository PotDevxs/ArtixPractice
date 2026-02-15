package dev.artixdev.api.practice.tablist.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Team;
import dev.artixdev.api.practice.tablist.TablistHandler;
import dev.artixdev.api.practice.tablist.setup.TabLayout;

public class TabListener implements Listener {
   private final TablistHandler instance;

   @EventHandler(
      priority = EventPriority.LOW
   )
   public void onJoin(PlayerJoinEvent event) {
      Player player = event.getPlayer();
      TabLayout layout = new TabLayout(player);
      layout.create();
      layout.setHeaderAndFooter();
      this.instance.getLayoutMapping().put(player.getUniqueId(), layout);
   }

   @EventHandler(
      priority = EventPriority.LOWEST
   )
   public void onQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      Team team = player.getScoreboard().getTeam("tab");
      if (team != null) {
         team.removeEntry(player.getName());
      }

      this.instance.getSkinCache().removeCache(player);
      this.instance.getLayoutMapping().remove(player.getUniqueId());
   }

   public TabListener(TablistHandler instance) {
      this.instance = instance;
   }
}
