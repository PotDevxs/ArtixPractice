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

public class v1_8_R3Impl implements INMSImpl {
   private static final String NMS = "net.minecraft.server.v1_8_R3";
   private static final double PACKET_VIEW_RADIUS = 64.0;
   private static final AtomicInteger ENTITY_ID_COUNTER = new AtomicInteger(-1);
   private final JavaPlugin plugin;

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

   public void removeArrows(Player player) {
      try {
         Object entityPlayer = getHandle(player);
         Method o = entityPlayer.getClass().getMethod("o", int.class);
         o.invoke(entityPlayer, 0);
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
         Method u = entityPlayer.getClass().getMethod("u");
         Object playerConnection = u.invoke(entityPlayer);
         Object tracker = playerConnection.getClass().getMethod("getTracker").invoke(playerConnection);
         Class<?> packetClass = Class.forName(NMS + ".PacketPlayOutAnimation");
         Object packet = packetClass.getConstructor(Class.forName(NMS + ".Entity"), int.class)
             .newInstance(entityPlayer, 3);
         Method sendPacketToEntity = tracker.getClass().getMethod("sendPacketToEntity",
             Class.forName(NMS + ".Entity"),
             Class.forName(NMS + ".Packet"));
         sendPacketToEntity.invoke(tracker, entityPlayer, packet);
      } catch (Exception e) {
         throw new RuntimeException("sendEatingAnimation failed", e);
      }
   }

   public void setJumping(Player player) {
      try {
         Object entityPlayer = getHandle(player);
         Method i = entityPlayer.getClass().getMethod("i", boolean.class);
         i.invoke(entityPlayer, true);
         Field onGroundField = findField(entityPlayer.getClass(), "onGround");
         boolean onGround = onGroundField.getBoolean(entityPlayer);
         Method V = entityPlayer.getClass().getMethod("V");
         Method ab = entityPlayer.getClass().getMethod("ab");
         if (!(Boolean) V.invoke(entityPlayer) && !(Boolean) ab.invoke(entityPlayer)) {
            if (onGround) {
               Method bF = entityPlayer.getClass().getMethod("bF");
               bF.invoke(entityPlayer);
            }
         } else {
            Field motYField = findField(entityPlayer.getClass(), "motY");
            double newMotY = motYField.getDouble(entityPlayer) + 0.03999999910593033D;
            motYField.setDouble(entityPlayer, newMotY);
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
      spawnPacket.getIntegers().write(1, (int)Math.floor(player.getLocation().getX() * 32.0D));
      spawnPacket.getIntegers().write(2, (int)Math.floor(player.getLocation().getY() * 32.0D));
      spawnPacket.getIntegers().write(3, (int)Math.floor(player.getLocation().getZ() * 32.0D));
      spawnPacket.getBytes().write(0, (byte)((int)(player.getLocation().getYaw() * 256.0F / 360.0F)));
      spawnPacket.getBytes().write(1, (byte)((int)(player.getLocation().getPitch() * 256.0F / 360.0F)));
      spawnPacket.getDataWatcherModifier().write(0, WrappedDataWatcher.getEntityWatcher(player));
      PacketContainer statusPacket = new PacketContainer(Server.ENTITY_STATUS);
      statusPacket.getModifier().writeDefaults();
      statusPacket.getIntegers().write(0, entityId);
      statusPacket.getBytes().write(0, (byte)3);
      List<Player> sentTo = getNearbyPlayers(player.getLocation());
      Iterator<Player> var6 = sentTo.iterator();

      while(var6.hasNext()) {
         Player watcher = (Player)var6.next();
         if (!watcher.getUniqueId().equals(player.getUniqueId())) {
            sendPacket(watcher, spawnPacket);
            sendPacket(watcher, statusPacket);
         }
      }

      PacketContainer destroyPacket = new PacketContainer(Server.ENTITY_DESTROY);
      destroyPacket.getModifier().writeDefaults();
      destroyPacket.getIntegerArrays().write(0, new int[]{entityId});
      Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
         sentTo.forEach((watcher) -> {
            sendPacket(watcher, destroyPacket);
         });
      }, 60L);
   }

   public void startRightClick(Player player) {
      try {
         Object entityPlayer = getHandle(player);
         Method f = entityPlayer.getClass().getMethod("f", boolean.class);
         f.invoke(entityPlayer, true);
      } catch (Exception e) {
         throw new RuntimeException("startRightClick failed", e);
      }
   }

   public void stopRightClick(Player player) {
      try {
         Object entityPlayer = getHandle(player);
         Method f = entityPlayer.getClass().getMethod("f", boolean.class);
         f.invoke(entityPlayer, false);
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
      PacketContainer lightningPacket = new PacketContainer(Server.SPAWN_ENTITY_WEATHER);
      lightningPacket.getModifier().writeDefaults();
      lightningPacket.getIntegers().write(0, 128);
      lightningPacket.getIntegers().write(4, 1);
      lightningPacket.getIntegers().write(1, (int)(location.getX() * 32.0D));
      lightningPacket.getIntegers().write(2, (int)(location.getY() * 32.0D));
      lightningPacket.getIntegers().write(3, (int)(location.getZ() * 32.0D));
      Iterator<Player> var3 = getNearbyPlayers(location).iterator();

      while(var3.hasNext()) {
         Player player = (Player)var3.next();
         ProtocolLibrary.getProtocolManager().sendServerPacket(player, lightningPacket);
      }

   }

   public v1_8_R3Impl(JavaPlugin plugin) {
      this.plugin = plugin;
   }
}
