package dev.artixdev.practice.listeners;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.utils.ChatUtils;

/**
 * World Listener
 * Handles world-related events
 */
public class WorldListener extends BaseListener {
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public WorldListener(Main plugin) {
      super(plugin);
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onBlockBreak(BlockBreakEvent event) {
      // Prevent block breaking in practice worlds
      if (plugin.getArenaManager().isInArena(event.getBlock().getLocation())) {
         event.setCancelled(true);
         event.getPlayer().sendMessage(ChatUtils.colorize("&cYou cannot break blocks in arenas!"));
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onBlockPlace(BlockPlaceEvent event) {
      // Prevent block placing in practice worlds
      if (plugin.getArenaManager().isInArena(event.getBlock().getLocation())) {
         event.setCancelled(true);
         event.getPlayer().sendMessage(ChatUtils.colorize("&cYou cannot place blocks in arenas!"));
      }
   }
   
   @EventHandler(priority = EventPriority.HIGH)
   public void onWeatherChange(WeatherChangeEvent event) {
      // Disable weather in practice worlds
      if (plugin.getArenaManager().isInArena(event.getWorld().getSpawnLocation())) {
         event.setCancelled(true);
      }
   }
   
   @EventHandler
   public void onWorldLoad(WorldLoadEvent event) {
      plugin.getLogger().info("World loaded: " + event.getWorld().getName());
   }
   
   @EventHandler
   public void onWorldUnload(WorldUnloadEvent event) {
      plugin.getLogger().info("World unloaded: " + event.getWorld().getName());
   }
}