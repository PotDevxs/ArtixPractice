package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.models.Arena;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.practice.utils.cuboid.Cuboid;

import java.util.ArrayList;
import java.util.List;

/**
 * Arena Button
 * Button for arena selection and management in menus
 */
public class ArenaButton extends Button {
   
   private final Arena arena;
   
   /**
    * Constructor
    * @param arena the arena
    */
   public ArenaButton(Arena arena) {
      this.arena = arena;
   }
   
   @Override
   public ItemStack getButtonItem(Player player) {
      // Use arena's display icon if available, otherwise create one
      ItemStack icon = arena.getDisplayIcon();
      
      XMaterial material = arena.isEnabled() ? XMaterial.DIAMOND_BLOCK : XMaterial.REDSTONE_BLOCK;
      
      ItemBuilder itemBuilder;
      if (icon != null && icon.getType() != org.bukkit.Material.AIR) {
         itemBuilder = new ItemBuilder(icon.clone());
      } else {
         itemBuilder = new ItemBuilder(material);
      }
      
      // Set name
      String name = arena.isEnabled() ? "&a" + arena.getName() : "&c" + arena.getName();
      itemBuilder.name(ChatUtils.colorize(name));
      
      // Build lore
      List<String> lore = new ArrayList<>();
      lore.add(ChatUtils.colorize("&7Click to select this arena"));
      lore.add("");
      lore.add(ChatUtils.colorize("&7Kit Type: &f" + arena.getKitType().getDisplayName()));
      lore.add(ChatUtils.colorize("&7Status: " + (arena.isEnabled() ? "&aEnabled" : "&cDisabled")));
      lore.add(ChatUtils.colorize("&7Ranked: " + (arena.isRanked() ? "&aYes" : "&cNo")));
      
      if (arena.getSpawn1() != null && arena.getSpawn2() != null) {
         lore.add(ChatUtils.colorize("&7Spawns: &aConfigured"));
      } else {
         lore.add(ChatUtils.colorize("&7Spawns: &cNot Configured"));
      }
      
      itemBuilder.lore(lore);
      
      return itemBuilder.build();
   }
   
   @Override
   public void clicked(Player player, ClickType clickType) {
      if (!arena.isEnabled()) {
         player.sendMessage(ChatUtils.colorize("&cThis arena is currently disabled!"));
         return;
      }
      
      if (arena.getSpawn1() == null || arena.getSpawn2() == null) {
         player.sendMessage(ChatUtils.colorize("&cThis arena is not properly configured!"));
         return;
      }
      
      // Handle arena selection
      if (clickType == ClickType.LEFT) {
         player.sendMessage(ChatUtils.colorize("&aSelected arena: &f" + arena.getName()));
         // TODO: Implement arena selection logic
      } else if (clickType == ClickType.SHIFT_LEFT) {
         // Teleport to arena
         player.teleport(arena.getSpawn1());
         player.sendMessage(ChatUtils.colorize("&aTeleported to arena: &f" + arena.getName()));
      } else if (clickType == ClickType.RIGHT) {
         // Show arena info
         showArenaInfo(player);
      }
   }
   
   /**
    * Show arena information
    * @param player the player
    */
   private void showArenaInfo(Player player) {
      player.sendMessage(ChatUtils.colorize("&6&lArena Information"));
      player.sendMessage(ChatUtils.colorize("&7Name: &f" + arena.getName()));
      player.sendMessage(ChatUtils.colorize("&7Kit Type: &f" + arena.getKitType().getDisplayName()));
      player.sendMessage(ChatUtils.colorize("&7Status: " + (arena.isEnabled() ? "&aEnabled" : "&cDisabled")));
      player.sendMessage(ChatUtils.colorize("&7Ranked: " + (arena.isRanked() ? "&aYes" : "&cNo")));
      
      if (arena.getSpawn1() != null) {
         player.sendMessage(ChatUtils.colorize("&7Spawn 1: &f" + formatLocation(arena.getSpawn1())));
      }
      
      if (arena.getSpawn2() != null) {
         player.sendMessage(ChatUtils.colorize("&7Spawn 2: &f" + formatLocation(arena.getSpawn2())));
      }
      
      Cuboid bounds = arena.getBounds();
      if (bounds != null) {
         int sizeX = bounds.getSizeX();
         int sizeY = bounds.getSizeY();
         int sizeZ = bounds.getSizeZ();
         player.sendMessage(ChatUtils.colorize("&7Bounds: &f" + sizeX + "x" + sizeY + "x" + sizeZ));
      }
   }
   
   /**
    * Format location for display
    * @param location the location
    * @return formatted string
    */
   private String formatLocation(org.bukkit.Location location) {
      return String.format("%.1f, %.1f, %.1f", 
         location.getX(), location.getY(), location.getZ());
   }
   
   /**
    * Get the arena
    * @return arena
    */
   public Arena getArena() {
      return this.arena;
   }
}
