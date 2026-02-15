package dev.artixdev.api.practice.spigot.equipment;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.inventory.ItemStack;

public class EquipmentListener implements Listener {
   private final List<String> blockedMaterials = Arrays.asList("FURNACE", "CHEST", "TRAPPED_CHEST", "BEACON", "DISPENSER", "DROPPER", "HOPPER", "WORKBENCH", "ENCHANTMENT_TABLE", "ENDER_CHEST", "ANVIL", "BED_BLOCK", "FENCE_GATE", "SPRUCE_FENCE_GATE", "BIRCH_FENCE_GATE", "ACACIA_FENCE_GATE", "JUNGLE_FENCE_GATE", "DARK_OAK_FENCE_GATE", "IRON_DOOR_BLOCK", "WOODEN_DOOR", "SPRUCE_DOOR", "BIRCH_DOOR", "JUNGLE_DOOR", "ACACIA_DOOR", "DARK_OAK_DOOR", "WOOD_BUTTON", "STONE_BUTTON", "TRAP_DOOR", "IRON_TRAPDOOR", "DIODE_BLOCK_OFF", "DIODE_BLOCK_ON", "REDSTONE_COMPARATOR_OFF", "REDSTONE_COMPARATOR_ON", "FENCE", "SPRUCE_FENCE", "BIRCH_FENCE", "JUNGLE_FENCE", "DARK_OAK_FENCE", "ACACIA_FENCE", "NETHER_FENCE", "BREWING_STAND", "CAULDRON", "SIGN_POST", "WALL_SIGN", "SIGN", "LEVER", "BLACK_SHULKER_BOX", "BLUE_SHULKER_BOX", "BROWN_SHULKER_BOX", "CYAN_SHULKER_BOX", "GRAY_SHULKER_BOX", "GREEN_SHULKER_BOX", "LIGHT_BLUE_SHULKER_BOX", "LIME_SHULKER_BOX", "MAGENTA_SHULKER_BOX", "ORANGE_SHULKER_BOX", "PINK_SHULKER_BOX", "PURPLE_SHULKER_BOX", "RED_SHULKER_BOX", "SILVER_SHULKER_BOX", "WHITE_SHULKER_BOX", "RED_SHULKER_BOX");

   @EventHandler
   public final void onInventoryClick(InventoryClickEvent e) {
      boolean shift = false;
      boolean numberkey = false;
      if (!e.isCancelled()) {
         if (e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)) {
            shift = true;
         }

         if (e.getClick().equals(ClickType.NUMBER_KEY)) {
            numberkey = true;
         }

         if (e.getSlotType() == SlotType.ARMOR || e.getSlotType() == SlotType.QUICKBAR || e.getSlotType() == SlotType.CONTAINER) {
            if (e.getClickedInventory() == null || e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
               if (e.getInventory().getType().equals(InventoryType.CRAFTING) || e.getInventory().getType().equals(InventoryType.PLAYER)) {
                  if (e.getWhoClicked() instanceof Player) {
                     EquipmentType newEquipmentType = EquipmentType.matchType(shift ? e.getCurrentItem() : e.getCursor());
                     if (shift || newEquipmentType == null || e.getRawSlot() == newEquipmentType.getSlot()) {
                        if (shift) {
                           newEquipmentType = EquipmentType.matchType(e.getCurrentItem());
                           if (newEquipmentType != null) {
                              boolean equipping = true;
                              if (e.getRawSlot() == newEquipmentType.getSlot()) {
                                 equipping = false;
                              }

                              if (newEquipmentType.equals(EquipmentType.HELMET) && equipping == (e.getWhoClicked().getInventory().getHelmet() == null) || newEquipmentType.equals(EquipmentType.CHESTPLATE) && equipping == (e.getWhoClicked().getInventory().getChestplate() == null) || newEquipmentType.equals(EquipmentType.LEGGINGS) && equipping == (e.getWhoClicked().getInventory().getLeggings() == null) || newEquipmentType.equals(EquipmentType.BOOTS) && equipping == (e.getWhoClicked().getInventory().getBoots() == null)) {
                                 EquipmentSetEvent equipmentSetEvent = new EquipmentSetEvent((Player)e.getWhoClicked(), EquipmentSetEvent.EquipMethod.SHIFT_CLICK, newEquipmentType, equipping ? null : e.getCurrentItem(), equipping ? e.getCurrentItem() : null);
                                 Bukkit.getServer().getPluginManager().callEvent(equipmentSetEvent);
                                 if (equipmentSetEvent.isCancelled()) {
                                    e.setCancelled(true);
                                 }
                              }
                           }
                        } else {
                           ItemStack newArmorPiece = e.getCursor();
                           ItemStack oldArmorPiece = e.getCurrentItem();
                           if (numberkey) {
                              if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
                                 ItemStack hotbarItem = e.getClickedInventory().getItem(e.getHotbarButton());
                                 if (hotbarItem != null) {
                                    newEquipmentType = EquipmentType.matchType(hotbarItem);
                                    newArmorPiece = hotbarItem;
                                    oldArmorPiece = e.getClickedInventory().getItem(e.getSlot());
                                 } else {
                                    newEquipmentType = EquipmentType.matchType(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR ? e.getCurrentItem() : e.getCursor());
                                 }
                              }
                           } else {
                              newEquipmentType = EquipmentType.matchType(e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR ? e.getCurrentItem() : e.getCursor());
                           }

                           if (newEquipmentType != null && e.getRawSlot() == newEquipmentType.getSlot()) {
                              EquipmentSetEvent.EquipMethod method = EquipmentSetEvent.EquipMethod.DRAG;
                              if (e.getAction().equals(InventoryAction.HOTBAR_SWAP) || numberkey) {
                                 method = EquipmentSetEvent.EquipMethod.HOTBAR_SWAP;
                              }

                              EquipmentSetEvent equipmentSetEvent = new EquipmentSetEvent((Player)e.getWhoClicked(), method, newEquipmentType, oldArmorPiece, newArmorPiece);
                              Bukkit.getServer().getPluginManager().callEvent(equipmentSetEvent);
                              if (equipmentSetEvent.isCancelled()) {
                                 e.setCancelled(true);
                              }
                           }
                        }

                     }
                  }
               }
            }
         }
      }
   }

   @EventHandler
   public void playerInteractEvent(PlayerInteractEvent e) {
      if (e.getAction() != Action.PHYSICAL) {
         if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = e.getPlayer();
            if (e.getClickedBlock() != null && e.getAction() == Action.RIGHT_CLICK_BLOCK) {
               Material mat = e.getClickedBlock().getType();
               Iterator var4 = this.blockedMaterials.iterator();

               while(var4.hasNext()) {
                  String s = (String)var4.next();
                  if (mat.name().equalsIgnoreCase(s)) {
                     return;
                  }
               }
            }

            EquipmentType newEquipmentType = EquipmentType.matchType(e.getItem());
            if (newEquipmentType != null && (newEquipmentType.equals(EquipmentType.HELMET) && e.getPlayer().getInventory().getHelmet() == null || newEquipmentType.equals(EquipmentType.CHESTPLATE) && e.getPlayer().getInventory().getChestplate() == null || newEquipmentType.equals(EquipmentType.LEGGINGS) && e.getPlayer().getInventory().getLeggings() == null || newEquipmentType.equals(EquipmentType.BOOTS) && e.getPlayer().getInventory().getBoots() == null)) {
               EquipmentSetEvent equipmentSetEvent = new EquipmentSetEvent(e.getPlayer(), EquipmentSetEvent.EquipMethod.HOTBAR, EquipmentType.matchType(e.getItem()), (ItemStack)null, e.getItem());
               Bukkit.getServer().getPluginManager().callEvent(equipmentSetEvent);
               if (equipmentSetEvent.isCancelled()) {
                  e.setCancelled(true);
                  player.updateInventory();
               }
            }
         }

      }
   }

   @EventHandler
   public void dispenserFireEvent(BlockDispenseEvent e) {
      EquipmentType type = EquipmentType.matchType(e.getItem());
      if (EquipmentType.matchType(e.getItem()) != null) {
         Location loc = e.getBlock().getLocation();
         Iterator var4 = loc.getWorld().getPlayers().iterator();

         Player p;
         BlockFace directionFacing;
         do {
            do {
               do {
                  do {
                     if (!var4.hasNext()) {
                        return;
                     }

                     p = (Player)var4.next();
                  } while(loc.getBlockY() - p.getLocation().getBlockY() < -1);
               } while(loc.getBlockY() - p.getLocation().getBlockY() > 1);
            } while((p.getInventory().getHelmet() != null || !type.equals(EquipmentType.HELMET)) && (p.getInventory().getChestplate() != null || !type.equals(EquipmentType.CHESTPLATE)) && (p.getInventory().getLeggings() != null || !type.equals(EquipmentType.LEGGINGS)) && (p.getInventory().getBoots() != null || !type.equals(EquipmentType.BOOTS)));

            Dispenser dispenser = (Dispenser)e.getBlock().getState();
            org.bukkit.material.Dispenser dis = (org.bukkit.material.Dispenser)dispenser.getData();
            directionFacing = dis.getFacing();
         } while((directionFacing != BlockFace.EAST || p.getLocation().getBlockX() == loc.getBlockX() || !(p.getLocation().getX() <= loc.getX() + 2.3D) || !(p.getLocation().getX() >= loc.getX())) && (directionFacing != BlockFace.WEST || !(p.getLocation().getX() >= loc.getX() - 1.3D) || !(p.getLocation().getX() <= loc.getX())) && (directionFacing != BlockFace.SOUTH || p.getLocation().getBlockZ() == loc.getBlockZ() || !(p.getLocation().getZ() <= loc.getZ() + 2.3D) || !(p.getLocation().getZ() >= loc.getZ())) && (directionFacing != BlockFace.NORTH || !(p.getLocation().getZ() >= loc.getZ() - 1.3D) || !(p.getLocation().getZ() <= loc.getZ())));

         EquipmentSetEvent equipmentSetEvent = new EquipmentSetEvent(p, EquipmentSetEvent.EquipMethod.DISPENSER, EquipmentType.matchType(e.getItem()), (ItemStack)null, e.getItem());
         Bukkit.getServer().getPluginManager().callEvent(equipmentSetEvent);
         if (equipmentSetEvent.isCancelled()) {
            e.setCancelled(true);
         }

      }
   }

   @EventHandler
   public void itemBreakEvent(PlayerItemBreakEvent e) {
      EquipmentType type = EquipmentType.matchType(e.getBrokenItem());
      if (type != null) {
         Player p = e.getPlayer();
         EquipmentSetEvent equipmentSetEvent = new EquipmentSetEvent(p, EquipmentSetEvent.EquipMethod.BROKE, type, e.getBrokenItem(), (ItemStack)null);
         Bukkit.getServer().getPluginManager().callEvent(equipmentSetEvent);
         if (equipmentSetEvent.isCancelled()) {
            ItemStack i = e.getBrokenItem().clone();
            i.setAmount(1);
            i.setDurability((short)(i.getDurability() - 1));
            if (type.equals(EquipmentType.HELMET)) {
               p.getInventory().setHelmet(i);
            } else if (type.equals(EquipmentType.CHESTPLATE)) {
               p.getInventory().setChestplate(i);
            } else if (type.equals(EquipmentType.LEGGINGS)) {
               p.getInventory().setLeggings(i);
            } else if (type.equals(EquipmentType.BOOTS)) {
               p.getInventory().setBoots(i);
            }
         }
      }

   }

   @EventHandler
   public void playerDeathEvent(PlayerDeathEvent e) {
      Player p = e.getEntity();
      ItemStack[] var3 = p.getInventory().getArmorContents();
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ItemStack i = var3[var5];
         if (i != null && !i.getType().equals(Material.AIR)) {
            Bukkit.getServer().getPluginManager().callEvent(new EquipmentSetEvent(p, EquipmentSetEvent.EquipMethod.DEATH, EquipmentType.matchType(i), i, (ItemStack)null));
         }
      }

   }
}
