package me.drizzy.api.nms.impl;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import me.drizzy.api.nms.INMSImpl;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumHand;
import net.minecraft.server.v1_12_R1.PacketPlayOutAnimation;
import net.minecraft.server.v1_12_R1.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.practice.llIllIlIIlIIlII.IIIIIllIIlIIlII;
import dev.artixdev.practice.llIllIlIIlIIlII.IllllIlIIlIIlII;

public class v1_12_R1Impl implements INMSImpl {
   private final JavaPlugin plugin;
   private final Random random = ThreadLocalRandom.current();

   public void removeArrows(Player player) {
      CraftPlayer craftPlayer = (CraftPlayer)player;
      craftPlayer.getHandle().setArrowCount(0);
   }

   public void setCanCollide(Player player, boolean canCollide) {
      player.setCollidable(canCollide);
   }

   public boolean isViewingInventory(Player player) {
      CraftPlayer craftPlayer = (CraftPlayer)player;
      return craftPlayer.getHandle().activeContainer.windowId != 0;
   }

   public void sendEatingAnimation(Player player) {
      EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
      WorldServer worldServer = (WorldServer)entityPlayer.getWorld();
      worldServer.getTracker().a(entityPlayer, new PacketPlayOutAnimation(entityPlayer, 2));
   }

   public void setJumping(Player player) {
      EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
      entityPlayer.l(true);
      if (!entityPlayer.isInWater() && !entityPlayer.au()) {
         if (entityPlayer.onGround) {
            entityPlayer.cu();
         }
      } else {
         entityPlayer.motY += 0.03999999910593033D;
      }

   }

   public void animateDeath(Player player) {
      int entityId = IIIIIllIIlIIlII.llllIIlIIlIIlII();
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
      double[] delta = IIIIIllIIlIIlII.lIIIIllIIlIIlII(player.getLocation());
      PacketContainer velocityPacket = new PacketContainer(Server.REL_ENTITY_MOVE);
      velocityPacket.getModifier().writeDefaults();
      velocityPacket.getIntegers().write(0, entityId);
      velocityPacket.getIntegers().write(1, (int)delta[0]);
      velocityPacket.getIntegers().write(2, (int)(delta[1] + 3.0D));
      velocityPacket.getIntegers().write(3, (int)delta[2]);
      List<Player> sentTo = IllllIlIIlIIlII.lllllIlIIlIIlII(player.getLocation());
      Iterator var8 = sentTo.iterator();

      while(var8.hasNext()) {
         Player watcher = (Player)var8.next();
         if (!watcher.getUniqueId().equals(player.getUniqueId())) {
            IllllIlIIlIIlII.lIIIIllIIlIIlII(watcher, spawnPacket);
            IllllIlIIlIIlII.lIIIIllIIlIIlII(watcher, statusPacket);
            IllllIlIIlIIlII.lIIIIllIIlIIlII(watcher, velocityPacket);
         }
      }

      PacketContainer destroyPacket = new PacketContainer(Server.ENTITY_DESTROY);
      destroyPacket.getModifier().writeDefaults();
      destroyPacket.getIntegerArrays().write(0, new int[]{entityId});
      Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
         sentTo.forEach((watcher) -> {
            IllllIlIIlIIlII.lIIIIllIIlIIlII(watcher, destroyPacket);
         });
      }, 60L);
   }

   public void startRightClick(Player player) {
      CraftPlayer craftPlayer = (CraftPlayer)player;
      craftPlayer.getHandle().c(EnumHand.MAIN_HAND);
   }

   public void stopRightClick(Player player) {
      CraftPlayer craftPlayer = (CraftPlayer)player;
      craftPlayer.getHandle().cN();
   }

   public void setUnbreakable(ItemStack itemStack) {
      if (itemStack.hasItemMeta()) {
         ItemMeta itemMeta = itemStack.getItemMeta();
         itemMeta.setUnbreakable(true);
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
      Iterator var3 = IllllIlIIlIIlII.lllllIlIIlIIlII(location).iterator();

      while(var3.hasNext()) {
         Player player = (Player)var3.next();
         ProtocolLibrary.getProtocolManager().sendServerPacket(player, lightningPacket);
      }

   }

   public v1_12_R1Impl(JavaPlugin plugin) {
      this.plugin = plugin;
   }
}
