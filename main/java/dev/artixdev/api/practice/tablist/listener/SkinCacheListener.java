package dev.artixdev.api.practice.tablist.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import dev.artixdev.api.practice.tablist.TablistHandler;
import dev.artixdev.api.practice.tablist.skin.SkinCache;

public class SkinCacheListener implements Listener {
   private final TablistHandler instance;

   @EventHandler
   public void onLoginEvent(PlayerLoginEvent event) {
      Player player = event.getPlayer();
      SkinCache cache = this.instance.getSkinCache();
      cache.registerCache(player);
   }

   @EventHandler
   public void onQuit(PlayerQuitEvent event) {
      Player player = event.getPlayer();
      this.instance.getSkinCache().removeCache(player);
   }

   public SkinCacheListener(TablistHandler instance) {
      this.instance = instance;
   }
}
