package me.drizzy.api.nms.impl;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
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

public class v1_18_R2Impl implements INMSImpl {
   private static final String NMS_PROTOCOL_GAME = "net.minecraft.network.protocol.game";
   private static final String NMS_WORLD_ENTITY = "net.minecraft.world.entity";
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
         Method setArrowCount = entityPlayer.getClass().getMethod("setArrowCount", int.class, boolean.class);
         setArrowCount.invoke(entityPlayer, 0, true);
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
         Field containerField = findField(entityPlayer.getClass(), "bV");
         Object container = containerField.get(entityPlayer);
         Field windowField = findField(container.getClass(), "j");
         int windowId = windowField.getInt(container);
         return windowId != 0;
      } catch (Exception e) {
         return false;
      }
   }

   public void sendEatingAnimation(Player player) {
      try {
         Object entityPlayer = getHandle(player);
         // WorldServer world = (WorldServer) entityPlayer.s;
         Field worldField = findField(entityPlayer.getClass(), "s");
         Object world = worldField.get(entityPlayer);
         // ChunkProviderServer chunkproviderserver = world.k();
         Method k = world.getClass().getMethod("k");
         Object chunkProvider = k.invoke(world);
         Class<?> entityClass = Class.forName(NMS_WORLD_ENTITY + ".Entity");
         Class<?> packetClass = Class.forName(NMS_PROTOCOL_GAME + ".PacketPlayOutAnimation");
         Class<?> packetInterface = Class.forName("net.minecraft.network.protocol.Packet");
         Object packet = packetClass.getConstructor(entityClass, int.class).newInstance(entityPlayer, 2);
         Method b = chunkProvider.getClass().getMethod("b", entityClass, packetInterface);
         b.invoke(chunkProvider, entityPlayer, packet);
      } catch (Exception e) {
         throw new RuntimeException("sendEatingAnimation failed", e);
      }
   }

   public void setJumping(Player player) {
      try {
         Object entityPlayer = getHandle(player);
         Method q = entityPlayer.getClass().getMethod("q", boolean.class);
         q.invoke(entityPlayer, true);
         Method aQ = entityPlayer.getClass().getMethod("aQ");
         Method bc = entityPlayer.getClass().getMethod("bc");
         if (!(Boolean) aQ.invoke(entityPlayer) && !(Boolean) bc.invoke(entityPlayer)) {
            Method aw = entityPlayer.getClass().getMethod("aw");
            if ((Boolean) aw.invoke(entityPlayer)) {
               Method eA = entityPlayer.getClass().getMethod("eA");
               eA.invoke(entityPlayer);
            }
         } else {
            Method da = entityPlayer.getClass().getMethod("da");
            Object vec = da.invoke(entityPlayer);
            Method d = vec.getClass().getMethod("d", double.class, double.class, double.class);
            Object newVec = d.invoke(vec, 0.0D, 0.03999999910593033D, 0.0D);
            Method g = entityPlayer.getClass().getMethod("g", vec.getClass());
            g.invoke(entityPlayer, newVec);
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
      PacketContainer statusPacket = new PacketContainer(Server.ENTITY_STATUS);
      statusPacket.getModifier().writeDefaults();
      statusPacket.getIntegers().write(0, entityId);
      statusPacket.getBytes().write(0, (byte)3);
      List<Player> sentTo = getNearbyPlayers(player.getLocation());
      Iterator<Player> var6 = sentTo.iterator();

      while(var6.hasNext()) {
         Player watcher = var6.next();
         if (!watcher.getUniqueId().equals(player.getUniqueId())) {
            sendPacket(watcher, spawnPacket);
            sendPacket(watcher, statusPacket);
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
         Class<?> enumHandClass = Class.forName("net.minecraft.world.EnumHand");
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
         Method eR = entityPlayer.getClass().getMethod("eR");
         eR.invoke(entityPlayer);
      } catch (Exception e) {
         throw new RuntimeException("stopRightClick failed", e);
      }
   }

   public void setUnbreakable(ItemStack itemStack) {
      if (itemStack.hasItemMeta()) {
         ItemMeta itemMeta = itemStack.getItemMeta();
         if (itemMeta != null) {
            itemMeta.spigot().setUnbreakable(true);
         }
      }
   }

   public void spawnLightning(Location location) {
      location.getWorld().spigot().strikeLightningEffect(location, true);
   }

   public v1_18_R2Impl(JavaPlugin plugin) {
      this.plugin = plugin;
   }
}
