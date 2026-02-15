package me.drizzy.api.hider.impl;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import java.util.Iterator;
import me.drizzy.api.hider.AbstractPacketListener;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.world.entity.item.EntityItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_20_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftItem;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Egg;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class v1_20_R1PacketListener extends AbstractPacketListener {
   private static final PacketType[] ENTITY_PACKETS;

   public v1_20_R1PacketListener(JavaPlugin plugin) {
      super(plugin, ENTITY_PACKETS);
   }

   public void onPacketSending(PacketEvent event) {
      if (event.getPlayer() != null) {
         if (event.getPacket() != null && !event.isPlayerTemporary()) {
            PacketType type = event.getPacketType();
            Player receiver = event.getPlayer();
            int entityID;
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
               int z = position.getZ();
               isInMatch = false;
               boolean isInMatch = false;
               Iterator var11 = receiver.getWorld().getEntitiesByClass(ThrownPotion.class).iterator();

               while(var11.hasNext()) {
                  ThrownPotion potion = (ThrownPotion)var11.next();
                  int potionX = MathHelper.b(y);
                  int potionY = MathHelper.b(z);
                  int potionZ = MathHelper.b(z);
                  if (potion.getShooter() instanceof Player && y == potionX && z == potionY && z == potionZ) {
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
               Sound sound = (Sound)event.getPacket().getSoundEffects().readSafely(0);
               if (!sound.equals(Sound.ENTITY_ARROW_HIT) && !sound.equals(Sound.ENTITY_ARROW_HIT_PLAYER) && !sound.equals(Sound.ENTITY_ARROW_SHOOT) && !sound.equals(Sound.ENTITY_CHICKEN_EGG) && !sound.equals(Sound.ENTITY_SNOWBALL_THROW) && !sound.equals(Sound.ENTITY_PLAYER_HURT) && !sound.equals(Sound.ENTITY_ENDER_PEARL_THROW) && !sound.equals(Sound.ENTITY_GENERIC_EAT) && !sound.equals(Sound.ENTITY_GENERIC_DRINK)) {
                  return;
               }

               int x = (Integer)event.getPacket().getIntegers().readSafely(0);
               y = (Integer)event.getPacket().getIntegers().readSafely(1);
               z = (Integer)event.getPacket().getIntegers().readSafely(2);
               boolean isVisible = false;
               isInMatch = false;
               Iterator var27 = receiver.getWorld().getEntitiesByClasses(new Class[]{Player.class, Projectile.class}).iterator();

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
                                 if (!var27.hasNext()) {
                                    if (isInMatch && !isVisible) {
                                       event.setCancelled(true);
                                    }

                                    return;
                                 }

                                 entity = (Entity)var27.next();
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
                  switch(sound) {
                  case ENTITY_ARROW_SHOOT:
                     ItemStack hand = player.getItemInHand();
                     if (hand.getType() == Material.POTION || hand.getType() == Material.BOW || hand.getType() == Material.ENDER_PEARL) {
                        pass = true;
                     }
                     break;
                  case ENTITY_ARROW_HIT:
                  case ENTITY_ARROW_HIT_PLAYER:
                     if (entity instanceof Arrow) {
                        pass = true;
                        break;
                     }
                  case ENTITY_CHICKEN_EGG:
                     if (entity instanceof Egg) {
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
               net.minecraft.world.entity.Entity nmsEntity = ((CraftWorld)receiver.getWorld()).getHandle().a(entityID);
               if (nmsEntity == null) {
                  return;
               }

               Entity entity = nmsEntity.getBukkitEntity();
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
      net.minecraft.world.entity.Entity entity = ((EntityItem)((CraftItem)item).getHandle()).v();
      if (entity instanceof EntityPlayer) {
         EntityPlayer entityPlayer = (EntityPlayer)entity;
         return entityPlayer.getBukkitEntity();
      } else {
         return null;
      }
   }

   static {
      ENTITY_PACKETS = new PacketType[]{Server.ENTITY_EQUIPMENT, Server.ANIMATION, Server.NAMED_ENTITY_SPAWN, Server.COLLECT, Server.SPAWN_ENTITY, Server.SPAWN_ENTITY_EXPERIENCE_ORB, Server.ENTITY_VELOCITY, Server.REL_ENTITY_MOVE, Server.ENTITY_LOOK, Server.ENTITY_MOVE_LOOK, Server.ENTITY_TELEPORT, Server.ENTITY_HEAD_ROTATION, Server.ENTITY_STATUS, Server.ATTACH_ENTITY, Server.ENTITY_METADATA, Server.ENTITY_EFFECT, Server.REMOVE_ENTITY_EFFECT, Server.BLOCK_BREAK_ANIMATION, Server.WORLD_EVENT, Server.NAMED_SOUND_EFFECT};
   }
}
