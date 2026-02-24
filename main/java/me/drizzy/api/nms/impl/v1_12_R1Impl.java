package me.drizzy.api.nms.impl;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import me.drizzy.api.nms.INMSImpl;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class v1_12_R1Impl implements INMSImpl {
   private static final String NMS = "net.minecraft.server.v1_12_R1";
   private static final double PACKET_VIEW_RADIUS = 64.0;
   private static final AtomicInteger ENTITY_ID_COUNTER = new AtomicInteger(-1);
   private final JavaPlugin plugin;

   private static Object getHandle(Player player) {
      try {
         Method getHandle = player.getClass().getMethod("getHandle");
         return getHandle.invoke(player);
      } catch (Exception e) {
         throw new RuntimeException("Failed to get NMS handle for player", e);
      }
   }

   private static Field findField(Class<?> start, String name) throws NoSuchFieldException {
      for (Class<?> c = start; c != null; c = c.getSuperclass()) {
         try {
            Field f = c.getDeclaredField(name);
            f.setAccessible(true);
            return f;
         } catch (NoSuchFieldException ignored) { }
      }
      throw new NoSuchFieldException(name);
   }

   private static int nextEntityId() {
      return ENTITY_ID_COUNTER.decrementAndGet();
   }

   private static List<Player> getNearbyPlayers(Location location) {
      List<Player> out = new ArrayList<>();
      if (location.getWorld() == null) return out;
      for (Player p : location.getWorld().getPlayers()) {
         if (p.getLocation().distanceSquared(location) <= PACKET_VIEW_RADIUS * PACKET_VIEW_RADIUS) {
            out.add(p);
         }
      }
      return out;
   }

   private static void sendPacket(Player player, PacketContainer packet) {
      ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
   }

   public void removeArrows(Player player) {
      try {
         Object entityPlayer = getHandle(player);
         Method setArrowCount = entityPlayer.getClass().getMethod("setArrowCount", int.class);
         setArrowCount.invoke(entityPlayer, 0);
      } catch (Exception e) {
         throw new RuntimeException("removeArrows failed", e);
      }
   }

   public void setCanCollide(Player player, boolean canCollide) {
      player.spigot().setCollidesWithEntities(canCollide);
   }

   public boolean isViewingInventory(Player player) {
      try {
         Object entityPlayer = getHandle(player);
         Object activeContainer = entityPlayer.getClass().getField("activeContainer").get(entityPlayer);
         int windowId = activeContainer.getClass().getField("windowId").getInt(activeContainer);
         return windowId != 0;
      } catch (Exception e) {
         return false;
      }
   }

   public void sendEatingAnimation(Player player) {
      try {
         Object entityPlayer = getHandle(player);
         Method getWorld = entityPlayer.getClass().getMethod("getWorld");
         Object world = getWorld.invoke(entityPlayer);
         Method getTracker = world.getClass().getMethod("getTracker");
         Object tracker = getTracker.invoke(world);
         Class<?> packetClass = Class.forName(NMS + ".PacketPlayOutAnimation");
         Object packet = packetClass.getConstructor(Class.forName(NMS + ".Entity"), int.class)
             .newInstance(entityPlayer, 2);
         Method a = tracker.getClass().getMethod("a", Class.forName(NMS + ".Entity"),
             Class.forName(NMS + ".Packet"));
         a.invoke(tracker, entityPlayer, packet);
      } catch (Exception e) {
         throw new RuntimeException("sendEatingAnimation failed", e);
      }
   }

   public void setJumping(Player player) {
      try {
         Object entityPlayer = getHandle(player);
         Method l = entityPlayer.getClass().getMethod("l", boolean.class);
         l.invoke(entityPlayer, true);
         Method isInWater = entityPlayer.getClass().getMethod("isInWater");
         Method au = entityPlayer.getClass().getMethod("au");
         if (!(Boolean) isInWater.invoke(entityPlayer) && !(Boolean) au.invoke(entityPlayer)) {
            Field onGroundField = findField(entityPlayer.getClass(), "onGround");
            if (onGroundField.getBoolean(entityPlayer)) {
               Method cu = entityPlayer.getClass().getMethod("cu");
               cu.invoke(entityPlayer);
            }
         } else {
            Field motYField = findField(entityPlayer.getClass(), "motY");
            motYField.setDouble(entityPlayer, motYField.getDouble(entityPlayer) + 0.03999999910593033D);
         }
      } catch (Exception e) {
         throw new RuntimeException("setJumping failed", e);
      }
   }

   public void animateDeath(Player player) {
      int entityId = nextEntityId();
      PacketContainer spawnPacket = new PacketContainer(Server.NAMED_ENTITY_SPAWN);
      spawnPacket.getModifier().writeDefaults();
      spawnPacket.getIntegers().write(0, entityId);
      spawnPacket.getUUIDs().write(0, player.getUniqueId());
      spawnPacket.getDoubles().write(0, player.getLocation().getX());
      spawnPacket.getDoubles().write(1, player.getLocation().getY());
      spawnPacket.getDoubles().write(2, player.getLocation().getZ());
      spawnPacket.getBytes().write(0, (byte)((int)(player.getLocation().getYaw() * 256.0F / 360.0F)));
      spawnPacket.getBytes().write(1, (byte)((int)(player.getLocation().getPitch() * 256.0F / 360.0F)));
      spawnPacket.getDataWatcherModifier().write(0, WrappedDataWatcher.getEntityWatcher(player));
      PacketContainer statusPacket = new PacketContainer(Server.ENTITY_STATUS);
      statusPacket.getModifier().writeDefaults();
      statusPacket.getIntegers().write(0, entityId);
      statusPacket.getBytes().write(0, (byte)3);
      PacketContainer velocityPacket = new PacketContainer(Server.REL_ENTITY_MOVE);
      velocityPacket.getModifier().writeDefaults();
      velocityPacket.getIntegers().write(0, entityId);
      velocityPacket.getIntegers().write(1, 0);
      velocityPacket.getIntegers().write(2, (int)(0 + 3.0D));
      velocityPacket.getIntegers().write(3, 0);
      List<Player> sentTo = getNearbyPlayers(player.getLocation());
      Iterator<Player> var8 = sentTo.iterator();

      while(var8.hasNext()) {
         Player watcher = var8.next();
         if (!watcher.getUniqueId().equals(player.getUniqueId())) {
            sendPacket(watcher, spawnPacket);
            sendPacket(watcher, statusPacket);
            sendPacket(watcher, velocityPacket);
         }
      }

      PacketContainer destroyPacket = new PacketContainer(Server.ENTITY_DESTROY);
      destroyPacket.getModifier().writeDefaults();
      destroyPacket.getIntegerArrays().write(0, new int[]{entityId});
      Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
         sentTo.forEach((watcher) -> sendPacket(watcher, destroyPacket));
      }, 60L);
   }

   public void startRightClick(Player player) {
      try {
         Object entityPlayer = getHandle(player);
         Class<?> enumHandClass = Class.forName(NMS + ".EnumHand");
         Object mainHand = enumHandClass.getEnumConstants()[0];
         Method c = entityPlayer.getClass().getMethod("c", enumHandClass);
         c.invoke(entityPlayer, mainHand);
      } catch (Exception e) {
         throw new RuntimeException("startRightClick failed", e);
      }
   }

   public void stopRightClick(Player player) {
      try {
         Object entityPlayer = getHandle(player);
         Method cN = entityPlayer.getClass().getMethod("cN");
         cN.invoke(entityPlayer);
      } catch (Exception e) {
         throw new RuntimeException("stopRightClick failed", e);
      }
   }

   public void setUnbreakable(ItemStack itemStack) {
      if (itemStack.hasItemMeta()) {
         ItemMeta itemMeta = itemStack.getItemMeta();
         itemMeta.spigot().setUnbreakable(true);
      }
   }

   public void spawnLightning(Location location) {
      PacketContainer lightningPacket = new PacketContainer(Server.WORLD_EVENT);
      lightningPacket.getModifier().writeDefaults();
      lightningPacket.getIntegers().write(0, 128);
      lightningPacket.getIntegers().write(1, 1);
      lightningPacket.getDoubles().write(0, location.getX());
      lightningPacket.getDoubles().write(1, location.getY());
      lightningPacket.getDoubles().write(2, location.getZ());
      Iterator<Player> var3 = getNearbyPlayers(location).iterator();

      while(var3.hasNext()) {
         Player target = var3.next();
         ProtocolLibrary.getProtocolManager().sendServerPacket(target, lightningPacket);
      }
   }

   public v1_12_R1Impl(JavaPlugin plugin) {
      this.plugin = plugin;
   }
}
