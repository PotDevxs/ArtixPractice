package dev.artixdev.api.practice.spigot.hider;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import com.google.common.base.Preconditions;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.api.practice.spigot.util.MathUtil;

public class EntityHider {
   protected Table<Integer, Integer, Boolean> observerEntityMap = HashBasedTable.create();
   private Field itemOwner;
   private final PacketType[] ENTITY_PACKETS;
   private final ProtocolManager manager;
   private final JavaPlugin plugin;
   private final Listener bukkitListener;
   private final PacketAdapter protocolListener;
   protected final EntityHider.Policy policy;

   public EntityHider(JavaPlugin plugin, EntityHider.Policy policy) {
      this.ENTITY_PACKETS = new PacketType[]{Server.ENTITY_EQUIPMENT, Server.BED, Server.ANIMATION, Server.NAMED_ENTITY_SPAWN, Server.COLLECT, Server.SPAWN_ENTITY, Server.SPAWN_ENTITY_LIVING, Server.SPAWN_ENTITY_PAINTING, Server.SPAWN_ENTITY_EXPERIENCE_ORB, Server.ENTITY_VELOCITY, Server.REL_ENTITY_MOVE, Server.ENTITY_LOOK, Server.ENTITY_MOVE_LOOK, Server.ENTITY_TELEPORT, Server.ENTITY_HEAD_ROTATION, Server.ENTITY_STATUS, Server.ATTACH_ENTITY, Server.ENTITY_METADATA, Server.ENTITY_EFFECT, Server.REMOVE_ENTITY_EFFECT, Server.BLOCK_BREAK_ANIMATION, Server.WORLD_EVENT, Server.NAMED_SOUND_EFFECT};
      Preconditions.checkNotNull(plugin, "plugin cannot be NULL.");
      this.plugin = plugin;
      this.policy = policy;
      this.manager = ProtocolLibrary.getProtocolManager();
      this.bukkitListener = this.constructBukkit();
      this.protocolListener = this.constructProtocol(plugin);
   }

   public void startListening() {
      try {
         this.manager.addPacketListener(this.protocolListener);
         this.plugin.getServer().getPluginManager().registerEvents(this.bukkitListener, this.plugin);
         // Use reflection to get EntityItem field without version-specific imports
         // Try to find an Item entity to get its NMS class
         Item sampleItem = null;
         for (org.bukkit.World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
               if (entity instanceof Item) {
                  sampleItem = (Item) entity;
                  break;
               }
            }
            if (sampleItem != null) break;
         }
         if (sampleItem != null) {
            Class<?> craftItemClass = sampleItem.getClass();
            Method getHandle = craftItemClass.getMethod("getHandle");
            Object nmsItem = getHandle.invoke(sampleItem);
            this.itemOwner = nmsItem.getClass().getDeclaredField("f");
            this.itemOwner.setAccessible(true);
         }
      } catch (Throwable e) {
         // Field may not exist in this version, continue without it
         this.itemOwner = null;
      }
   }

   protected boolean setVisibility(Player observer, int entityID, boolean visible) {
      switch(this.policy) {
      case BLACKLIST:
         return !this.setMembership(observer, entityID, !visible);
      case WHITELIST:
         return this.setMembership(observer, entityID, visible);
      default:
         throw new IllegalArgumentException("Unknown policy: " + this.policy);
      }
   }

   protected boolean setMembership(Player observer, int entityID, boolean member) {
      if (member) {
         return this.observerEntityMap.put(observer.getEntityId(), entityID, true) != null;
      } else {
         return this.observerEntityMap.remove(observer.getEntityId(), entityID) != null;
      }
   }

   protected boolean getMembership(Player observer, int entityID) {
      return this.observerEntityMap.contains(observer.getEntityId(), entityID);
   }

   protected boolean isVisible(Player observer, int entityID) {
      boolean presence = this.getMembership(observer, entityID);
      return this.policy == EntityHider.Policy.WHITELIST == presence;
   }

   protected void removeEntity(Entity entity) {
      int entityID = entity.getEntityId();
      Iterator var3 = this.observerEntityMap.rowMap().values().iterator();

      while(var3.hasNext()) {
         Map<Integer, Boolean> maps = (Map)var3.next();
         maps.remove(entityID);
      }

   }

   protected void removePlayer(Player player) {
      this.observerEntityMap.rowMap().remove(player.getEntityId());
   }

   private Listener constructBukkit() {
      return new Listener() {
         @EventHandler
         public void onEntityDeath(EntityDeathEvent e) {
            EntityHider.this.removeEntity(e.getEntity());
         }

         @EventHandler
         public void onChunkUnload(ChunkUnloadEvent e) {
            Entity[] var2 = e.getChunk().getEntities();
            int var3 = var2.length;

            for(int var4 = 0; var4 < var3; ++var4) {
               Entity entity = var2[var4];
               EntityHider.this.removeEntity(entity);
            }

         }

         @EventHandler
         public void onPlayerQuit(PlayerQuitEvent e) {
            EntityHider.this.removePlayer(e.getPlayer());
         }

         @EventHandler(
            priority = EventPriority.MONITOR
         )
         public void onPlayerPickupItem(PlayerPickupItemEvent event) {
            Player receiver = event.getPlayer();
            Item item = event.getItem();
            Player dropper = EntityHider.this.getPlayerWhoDropped(item);
            if (dropper != null) {
               if (!receiver.canSee(dropper)) {
                  event.setCancelled(true);
               }

            }
         }

         @EventHandler(
            priority = EventPriority.MONITOR
         )
         public void onPickup(PlayerPickupItemEvent event) {
            Player receiver = event.getPlayer();
            Item item = event.getItem();
            if (item.getItemStack().getType() == Material.ARROW) {
               try {
                  Object handle = item.getClass().getMethod("getHandle").invoke(item);
                  Entity entity = (Entity)handle.getClass().getMethod("getBukkitEntity").invoke(handle);
                  if (entity instanceof Arrow) {
                     Arrow arrow = (Arrow)entity;
                     if (arrow.getShooter() instanceof Player) {
                        Player shooter = (Player)arrow.getShooter();
                        if (!receiver.canSee(shooter)) {
                           event.setCancelled(true);
                        }
                     }
                  }
               } catch (Exception e) {
                  // Reflection failed, skip
               }
            }
         }

         @EventHandler(
            priority = EventPriority.MONITOR
         )
         public void onPotionSplash(PotionSplashEvent event) {
            ThrownPotion potion = event.getEntity();
            if (potion.getShooter() instanceof Player) {
               Player shooter = (Player)potion.getShooter();
               Iterator var4 = event.getAffectedEntities().iterator();

               while(var4.hasNext()) {
                  LivingEntity livingEntity = (LivingEntity)var4.next();
                  if (!(livingEntity instanceof Player)) {
                     return;
                  }

                  Player receiver = (Player)livingEntity;
                  if (!receiver.canSee(shooter)) {
                     event.setIntensity(receiver, 0.0D);
                  }
               }

            }
         }
      };
   }

   private PacketAdapter constructProtocol(Plugin plugin) {
      return new PacketAdapter(plugin, this.ENTITY_PACKETS) {
         public void onPacketSending(PacketEvent event) {
            if (event.getPlayer() != null) {
               if (event.getPacket() != null) {
                  int entityID = (Integer)event.getPacket().getIntegers().read(0);
                  if (!EntityHider.this.isVisible(event.getPlayer(), entityID)) {
                     event.setCancelled(true);
                  }

                  PacketType type = event.getPacketType();
                  Player receiver = event.getPlayer();
                  int y;
                  int z;
                  boolean isInMatch;
                  if (type == Server.WORLD_EVENT) {
                     int effect = (Integer)event.getPacket().getIntegers().read(0);
                     if (effect != 2002) {
                        return;
                     }

                     BlockPosition position = (BlockPosition)event.getPacket().getBlockPositionModifier().read(0);
                     y = position.getX();
                     z = position.getY();
                     int zx = position.getZ();
                     isInMatch = false;
                     boolean isInMatchx = false;
                     Iterator var12 = receiver.getWorld().getEntitiesByClass(ThrownPotion.class).iterator();

                     while(var12.hasNext()) {
                        ThrownPotion potion = (ThrownPotion)var12.next();
                        int potionX = MathUtil.floor((double)y);
                        int potionY = MathUtil.floor((double)z);
                        int potionZ = MathUtil.floor((double)zx);
                        if (potion.getShooter() instanceof Player && y == potionX && z == potionY && zx == potionZ) {
                           isInMatchx = true;
                           Player shooter = (Player)potion.getShooter();
                           if (receiver.canSee(shooter)) {
                              isInMatch = true;
                           }
                        }
                     }

                     if (isInMatchx && !isInMatch) {
                        event.setCancelled(true);
                     }
                  } else if (type == Server.NAMED_SOUND_EFFECT) {
                     String sound = (String)event.getPacket().getStrings().read(0);
                     if (!sound.equals("random.bow") && !sound.equals("random.bowhit") && !sound.equals("random.pop") && !sound.equals("game.player.hurt")) {
                        return;
                     }

                     int x = (Integer)event.getPacket().getIntegers().read(0);
                     y = (Integer)event.getPacket().getIntegers().read(1);
                     z = (Integer)event.getPacket().getIntegers().read(2);
                     boolean isVisible = false;
                     isInMatch = false;
                     Iterator var30 = receiver.getWorld().getEntitiesByClasses(new Class[]{Player.class, Projectile.class}).iterator();

                     while(true) {
                        Entity entityx;
                        Player playerx;
                        boolean two;
                        boolean three;
                        do {
                           boolean one;
                           do {
                              do {
                                 Location location;
                                 do {
                                    do {
                                       if (!var30.hasNext()) {
                                          if (isInMatch && !isVisible) {
                                             event.setCancelled(true);
                                          }

                                          return;
                                       }

                                       entityx = (Entity)var30.next();
                                    } while(!(entityx instanceof Player) && !(entityx instanceof Projectile));

                                    playerx = null;
                                    location = entityx.getLocation();
                                    if (entityx instanceof Player) {
                                       playerx = (Player)entityx;
                                    }

                                    if (entityx instanceof Projectile) {
                                       Projectile projectilex = (Projectile)entityx;
                                       if (projectilex.getShooter() instanceof Player) {
                                          playerx = (Player)projectilex.getShooter();
                                       }
                                    }
                                 } while(playerx == null);

                                 one = location.getX() * 8.0D == (double)x;
                                 two = location.getY() * 8.0D == (double)y;
                                 three = location.getZ() * 8.0D == (double)z;
                              } while(!one);
                           } while(!two);
                        } while(!three);

                        boolean pass = false;
                        byte var20 = -1;
                        switch(sound.hashCode()) {
                        case 114504223:
                           if (sound.equals("random.bow")) {
                              var20 = 0;
                           }
                           break;
                        case 991377684:
                           if (sound.equals("random.bowhit")) {
                              var20 = 1;
                           }
                        }

                        switch(var20) {
                        case 0:
                           ItemStack hand = playerx.getItemInHand();
                           if (hand != null && (hand.getType() == Material.POTION || hand.getType() == Material.BOW || hand.getType() == Material.ENDER_PEARL)) {
                              pass = true;
                           }
                           break;
                        case 1:
                           if (entityx instanceof Arrow) {
                              pass = true;
                              break;
                           }
                        default:
                           if (entityx instanceof Player) {
                              pass = true;
                           }
                        }

                        if (pass) {
                           isInMatch = true;
                           if (receiver.canSee(playerx)) {
                              isVisible = true;
                           }
                        }
                     }
                  } else {
                     // Use reflection to get entity without version-specific imports
                     try {
                        Object craftWorld = receiver.getWorld();
                        Method getHandleMethod = craftWorld.getClass().getMethod("getHandle");
                        Object nmsWorld = getHandleMethod.invoke(craftWorld);
                        Method getEntityMethod = nmsWorld.getClass().getMethod("getEntity", int.class);
                        Object nmsEntity = getEntityMethod.invoke(nmsWorld, entityID);
                        if (nmsEntity == null) {
                           return;
                        }
                        Method getBukkitEntityMethod = nmsEntity.getClass().getMethod("getBukkitEntity");
                        Entity entity = (Entity)getBukkitEntityMethod.invoke(nmsEntity);
                        
                        if (entity instanceof Player) {
                           Player player = (Player)entity;
                           if (receiver.canSee(player)) {
                              return;
                           }

                           event.setCancelled(true);
                        } else {
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
                              dropper = EntityHider.this.getPlayerWhoDropped(item);
                              if (dropper == null) {
                                 return;
                              }

                              if (receiver.canSee(dropper)) {
                                 return;
                              }

                              event.setCancelled(true);
                           }
                        }
                     } catch (Exception e) {
                        // Reflection failed, skip
                        return;
                     }
                  }

               }
            }
         }
      };
   }

   public final boolean toggleEntity(Player observer, Entity entity) {
      if (this.isVisible(observer, entity.getEntityId())) {
         return this.hideEntity(observer, entity);
      } else {
         return !this.showEntity(observer, entity);
      }
   }

   public final boolean showEntity(Player observer, Entity entity) {
      this.validate(observer, entity);
      boolean hiddenBefore = !this.setVisibility(observer, entity.getEntityId(), true);
      if (this.manager != null && hiddenBefore) {
         this.manager.updateEntity(entity, Collections.singletonList(observer));
      }

      return hiddenBefore;
   }

   public final boolean hideEntity(Player observer, Entity entity) {
      this.validate(observer, entity);
      boolean visibleBefore = this.setVisibility(observer, entity.getEntityId(), false);
      if (visibleBefore) {
         try {
            this.destroy(observer, entity.getEntityId());
         } catch (Exception e) {
            throw new RuntimeException("Cannot send server packet.", e);
         }
      }

      return visibleBefore;
   }

   public final boolean canSee(Player observer, Entity entity) {
      this.validate(observer, entity);
      return this.isVisible(observer, entity.getEntityId());
   }

   private void validate(Player observer, Entity entity) {
      Preconditions.checkNotNull(observer, "observer cannot be NULL.");
      Preconditions.checkNotNull(entity, "entity cannot be NULL.");
   }

   public EntityHider.Policy getPolicy() {
      return this.policy;
   }

   public void close() {
      HandlerList.unregisterAll(this.bukkitListener);
      this.manager.removePacketListener(this.protocolListener);
      this.itemOwner.setAccessible(false);
   }

   private Player getPlayerWhoDropped(Item item) {
      try {
         Object handle = item.getClass().getMethod("getHandle").invoke(item);
         String name = (String)this.itemOwner.get(handle);
         return name == null ? null : Bukkit.getPlayer(name);
      } catch (Exception e) {
         return null;
      }
   }

   public void destroy(Player player, int entityId) {
      PacketContainer container = new PacketContainer(Server.ENTITY_DESTROY);
      container.getIntegerArrays().write(0, new int[]{entityId});

      try {
         ProtocolLibrary.getProtocolManager().sendServerPacket(player, container);
      } catch (Exception e) {
      }

   }

   public static enum Policy {
      WHITELIST,
      BLACKLIST;
   }
}
