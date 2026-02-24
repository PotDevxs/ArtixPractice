package me.drizzy.api.hider.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import java.util.Iterator;
import me.drizzy.api.hider.AbstractPacketListener;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class v1_8_R3PacketListener extends AbstractPacketListener {
   private static final PacketType[] ENTITY_PACKETS;

   public v1_8_R3PacketListener(JavaPlugin plugin) {
      super(plugin, ENTITY_PACKETS);
   }

   public void onPacketSending(PacketEvent event) {
      if (event.getPlayer() != null) {
         if (event.getPacket() != null) {
            PacketType type = event.getPacketType();
            Player receiver = event.getPlayer();
            int entityID;
            int x;
            if (type == Server.SPAWN_ENTITY) {
               entityID = (Integer)event.getPacket().getIntegers().readSafely(9);
               x = (Integer)event.getPacket().getIntegers().readSafely(10);
               if (entityID == 73 && x == 0) {
                  event.getPacket().getIntegers().writeSafely(10, 16421);
               }
            }

            int y;
            int z;
            boolean isInMatch;
            if (type == Server.WORLD_EVENT) {
               entityID = (Integer)event.getPacket().getIntegers().readSafely(0);
               if (entityID != 2002) {
                  return;
               }

               BlockPosition position = (BlockPosition)event.getPacket().getBlockPositionModifier().readSafely(0);
               y = position.getX();
               z = position.getY();
               int posZ = position.getZ();
               isInMatch = false;
               Iterator<ThrownPotion> var11 = receiver.getWorld().getEntitiesByClass(ThrownPotion.class).iterator();

               while(var11.hasNext()) {
                  ThrownPotion potion = var11.next();
                  int potionX = (int) Math.floor((double)y);
                  int potionY = (int) Math.floor((double)z);
                  int potionZ = (int) Math.floor((double)posZ);
                  if (potion.getShooter() instanceof Player && y == potionX && z == potionY && posZ == potionZ) {
                     isInMatch = true;
                     Player shooter = (Player)potion.getShooter();
                     if (receiver.canSee(shooter)) {
                        isInMatch = true;
                     }
                  }
               }

               if (isInMatch && !isInMatch) {
                  event.setCancelled(true);
               }
            } else if (type == Server.NAMED_SOUND_EFFECT) {
               String sound = (String)event.getPacket().getStrings().readSafely(0);
               if (!sound.equals("random.bow") && !sound.equals("random.bowhit") && !sound.equals("random.pop") && !sound.equals("game.player.hurt") && !sound.equals("random.drink") && !sound.equals("random.eat")) {
                  return;
               }

               x = (Integer)event.getPacket().getIntegers().readSafely(0);
               y = (Integer)event.getPacket().getIntegers().readSafely(1);
               z = (Integer)event.getPacket().getIntegers().readSafely(2);
               boolean isVisible = false;
               isInMatch = false;
               Iterator<Entity> var29 = receiver.getWorld().getEntitiesByClasses(new Class[]{Player.class, Projectile.class}).iterator();

               while(true) {
                  Entity entity;
                  Player player;
                  boolean two;
                  boolean three;
                  do {
                     boolean one;
                     do {
                        do {
                           Location location;
                           do {
                              do {
                                 if (!var29.hasNext()) {
                                    if (isInMatch && !isVisible) {
                                       event.setCancelled(true);
                                    }

                                    return;
                                 }

                                 entity = (Entity)var29.next();
                              } while(!(entity instanceof Player) && !(entity instanceof Projectile));

                              player = null;
                              location = entity.getLocation();
                              if (entity instanceof Player) {
                                 player = (Player)entity;
                              }

                              if (entity instanceof Projectile) {
                                 Projectile projectile = (Projectile)entity;
                                 if (projectile.getShooter() instanceof Player) {
                                    player = (Player)projectile.getShooter();
                                 }
                              }
                           } while(player == null);

                           one = location.getX() * 8.0D == (double)x;
                           two = location.getY() * 8.0D == (double)y;
                           three = location.getZ() * 8.0D == (double)z;
                        } while(!one);
                     } while(!two);
                  } while(!three);

                  boolean pass = false;
                  byte var19 = -1;
                  switch(sound.hashCode()) {
                  case 114504223:
                     if (sound.equals("random.bow")) {
                        var19 = 0;
                     }
                     break;
                  case 991377684:
                     if (sound.equals("random.bowhit")) {
                        var19 = 1;
                     }
                  }

                  switch(var19) {
                  case 0:
                     ItemStack hand = player.getItemInHand();
                     if (hand != null && (hand.getType() == Material.POTION || hand.getType() == Material.BOW || hand.getType() == Material.ENDER_PEARL)) {
                        pass = true;
                     }
                     break;
                  case 1:
                     if (entity instanceof Arrow) {
                        pass = true;
                        break;
                     }
                  default:
                     if (entity instanceof Player) {
                        pass = true;
                     }
                  }

                  if (pass) {
                     isInMatch = true;
                     if (receiver.canSee(player)) {
                        isVisible = true;
                     }
                  }
               }
            } else {
               entityID = (Integer)event.getPacket().getIntegers().readSafely(0);
               Entity entity = getBukkitEntityById(receiver.getWorld(), entityID);
               if (entity == null) {
                  return;
               }
               Player dropper;
               if (entity instanceof Projectile) {
                  Projectile projectile = (Projectile)entity;
                  if (!(projectile.getShooter() instanceof Player)) {
                     return;
                  }

                  dropper = (Player)projectile.getShooter();
                  if (receiver.canSee(dropper)) {
                     return;
                  }

                  event.setCancelled(true);
               } else if (entity instanceof Item) {
                  Item item = (Item)entity;
                  dropper = this.getPlayerWhoDropped(item);
                  if (dropper == null) {
                     return;
                  }

                  if (receiver.canSee(dropper)) {
                     return;
                  }

                  event.setCancelled(true);
               }
            }

         }
      }
   }

   public Player getPlayerWhoDropped(Item item) {
      try {
         Object handle = item.getClass().getMethod("getHandle").invoke(item);
         String name = (String) handle.getClass().getMethod("n").invoke(handle);
         return Bukkit.getPlayer(name);
      } catch (Exception e) {
         return null;
      }
   }

   private static Entity getBukkitEntityById(World world, int entityId) {
      try {
         Object worldHandle = world.getClass().getMethod("getHandle").invoke(world);
         Object nmsEntity = worldHandle.getClass().getMethod("a", int.class).invoke(worldHandle, entityId);
         if (nmsEntity == null) return null;
         return (Entity) nmsEntity.getClass().getMethod("getBukkitEntity").invoke(nmsEntity);
      } catch (Exception e) {
         return null;
      }
   }

   static {
      ENTITY_PACKETS = new PacketType[]{Server.ENTITY_EQUIPMENT, Server.BED, Server.ANIMATION, Server.NAMED_ENTITY_SPAWN, Server.COLLECT, Server.SPAWN_ENTITY, Server.SPAWN_ENTITY_LIVING, Server.SPAWN_ENTITY_EXPERIENCE_ORB, Server.ENTITY_VELOCITY, Server.REL_ENTITY_MOVE, Server.ENTITY_LOOK, Server.ENTITY_MOVE_LOOK, Server.ENTITY_TELEPORT, Server.ENTITY_HEAD_ROTATION, Server.ENTITY_STATUS, Server.ATTACH_ENTITY, Server.ENTITY_METADATA, Server.ENTITY_EFFECT, Server.REMOVE_ENTITY_EFFECT, Server.BLOCK_BREAK_ANIMATION, Server.WORLD_EVENT, Server.NAMED_SOUND_EFFECT};
   }
}
