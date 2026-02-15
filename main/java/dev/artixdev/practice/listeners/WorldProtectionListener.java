package dev.artixdev.practice.listeners;

import java.util.List;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Difficulty;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.BlockIgniteEvent.IgniteCause;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.event.world.WorldLoadEvent;
import dev.artixdev.api.practice.nametag.util.VersionUtil;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.utils.ChatUtils;

/**
 * World Protection Listener
 * Protects practice worlds from unwanted changes
 */
public class WorldProtectionListener extends BaseListener {
   
   /**
    * Constructor
    * @param plugin the plugin instance
    */
   public WorldProtectionListener(Main plugin) {
      super(plugin);
   }
   
   @EventHandler
   public void onWorldLoad(WorldLoadEvent event) {
      World world = event.getWorld();
      
      // Set world properties for practice
      world.setDifficulty(Difficulty.NORMAL);
      world.setGameRuleValue("doDaylightCycle", "false");
      world.setGameRuleValue("doWeatherCycle", "false");
      world.setGameRuleValue("doMobSpawning", "false");
      world.setGameRuleValue("doEntityDrops", "false");
      world.setGameRuleValue("doTileDrops", "false");
      world.setGameRuleValue("doFireTick", "false");
      world.setGameRuleValue("doMobLoot", "false");
      world.setGameRuleValue("doMobGriefing", "false");
      world.setGameRuleValue("doVinesSpread", "false");
      world.setGameRuleValue("doMobSpawning", "false");
      
      // Clear existing entities
      List<Entity> entities = world.getEntities();
      entities.forEach(entity -> {
         if (entity.getType() != EntityType.PLAYER) {
            entity.remove();
         }
      });
      
      plugin.getLogger().info("Protected world loaded: " + world.getName());
   }
   
   @EventHandler
   public void onBlockBurn(BlockBurnEvent event) {
      // Prevent block burning in practice worlds
      if (isPracticeWorld(event.getBlock().getWorld())) {
         event.setCancelled(true);
      }
   }
   
   @EventHandler
   public void onBlockIgnite(BlockIgniteEvent event) {
      // Prevent block ignition in practice worlds
      if (isPracticeWorld(event.getBlock().getWorld())) {
         event.setCancelled(true);
      }
   }
   
   @EventHandler
   public void onBlockSpread(BlockSpreadEvent event) {
      // Prevent block spreading in practice worlds
      if (isPracticeWorld(event.getBlock().getWorld())) {
         event.setCancelled(true);
      }
   }
   
   @EventHandler
   public void onLeavesDecay(LeavesDecayEvent event) {
      // Prevent leaves decay in practice worlds
      if (isPracticeWorld(event.getBlock().getWorld())) {
         event.setCancelled(true);
      }
   }
   
   @EventHandler
   public void onCreatureSpawn(CreatureSpawnEvent event) {
      // Prevent creature spawning in practice worlds
      if (isPracticeWorld(event.getEntity().getWorld())) {
         event.setCancelled(true);
      }
   }
   
   @EventHandler
   public void onExplosionPrime(ExplosionPrimeEvent event) {
      // Prevent explosions in practice worlds
      if (isPracticeWorld(event.getEntity().getWorld())) {
         event.setCancelled(true);
      }
   }
   
   @EventHandler
   public void onHangingBreak(HangingBreakEvent event) {
      // Prevent hanging break in practice worlds
      if (isPracticeWorld(event.getEntity().getWorld())) {
         event.setCancelled(true);
      }
   }
   
   @EventHandler
   public void onPlayerBedEnter(PlayerBedEnterEvent event) {
      // Prevent bed entering in practice worlds
      if (isPracticeWorld(event.getPlayer().getWorld())) {
         event.setCancelled(true);
         event.getPlayer().sendMessage(ChatUtils.colorize("&cYou cannot sleep in practice worlds!"));
      }
   }
   
   @EventHandler
   public void onPlayerInteract(PlayerInteractEvent event) {
      // Prevent certain interactions in practice worlds
      if (isPracticeWorld(event.getPlayer().getWorld())) {
         if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Block block = event.getClickedBlock();
            if (block != null) {
               // Prevent bed usage
               if (XMaterial.matchXMaterial(block.getType()) == XMaterial.RED_BED) {
                  event.setCancelled(true);
                  event.getPlayer().sendMessage(ChatUtils.colorize("&cYou cannot sleep in practice worlds!"));
               }
               
               // Prevent ender chest usage
               if (XMaterial.matchXMaterial(block.getType()) == XMaterial.ENDER_CHEST) {
                  event.setCancelled(true);
                  event.getPlayer().sendMessage(ChatUtils.colorize("&cYou cannot use ender chests in practice worlds!"));
               }
               
               // Prevent enchanting table usage
               if (XMaterial.matchXMaterial(block.getType()) == XMaterial.ENCHANTING_TABLE) {
                  event.setCancelled(true);
                  event.getPlayer().sendMessage(ChatUtils.colorize("&cYou cannot use enchanting tables in practice worlds!"));
               }
            }
         }
      }
   }
   
   @EventHandler
   public void onWeatherChange(WeatherChangeEvent event) {
      // Prevent weather changes in practice worlds
      if (isPracticeWorld(event.getWorld())) {
         event.setCancelled(true);
      }
   }
   
   /**
    * Check if world is a practice world
    * @param world the world
    * @return true if practice world
    */
   private boolean isPracticeWorld(World world) {
      if (world == null) {
         return false;
      }
      
      // Check if world is in arena manager
      if (plugin.getArenaManager().isInArena(world.getSpawnLocation())) {
         return true;
      }
      
      // Check if world is in match manager
      if (plugin.getMatchManager().isLocationInMatch(world.getSpawnLocation())) {
         return true;
      }
      
      // Check if world name contains practice keywords
      String worldName = world.getName().toLowerCase();
      return worldName.contains("practice") || 
             worldName.contains("arena") || 
             worldName.contains("match") ||
             worldName.contains("duel");
   }
   
   /**
    * Protect world from unwanted changes
    * @param world the world to protect
    */
   public void protectWorld(World world) {
      if (world == null) {
         return;
      }
      
      // Set world properties
      world.setDifficulty(Difficulty.NORMAL);
      world.setGameRuleValue("doDaylightCycle", "false");
      world.setGameRuleValue("doWeatherCycle", "false");
      world.setGameRuleValue("doMobSpawning", "false");
      world.setGameRuleValue("doEntityDrops", "false");
      world.setGameRuleValue("doTileDrops", "false");
      world.setGameRuleValue("doFireTick", "false");
      world.setGameRuleValue("doMobLoot", "false");
      world.setGameRuleValue("doMobGriefing", "false");
      world.setGameRuleValue("doVinesSpread", "false");
      
      // Clear unwanted entities
      List<Entity> entities = world.getEntities();
      entities.forEach(entity -> {
         if (entity.getType() != EntityType.PLAYER) {
            entity.remove();
         }
      });
      
      plugin.getLogger().info("Protected world: " + world.getName());
   }
   
   /**
    * Unprotect world
    * @param world the world to unprotect
    */
   public void unprotectWorld(World world) {
      if (world == null) {
         return;
      }
      
      // Reset world properties
      world.setDifficulty(Difficulty.NORMAL);
      world.setGameRuleValue("doDaylightCycle", "true");
      world.setGameRuleValue("doWeatherCycle", "true");
      world.setGameRuleValue("doMobSpawning", "true");
      world.setGameRuleValue("doEntityDrops", "true");
      world.setGameRuleValue("doTileDrops", "true");
      world.setGameRuleValue("doFireTick", "true");
      world.setGameRuleValue("doMobLoot", "true");
      world.setGameRuleValue("doMobGriefing", "true");
      world.setGameRuleValue("doVinesSpread", "true");
      
      plugin.getLogger().info("Unprotected world: " + world.getName());
   }
}
