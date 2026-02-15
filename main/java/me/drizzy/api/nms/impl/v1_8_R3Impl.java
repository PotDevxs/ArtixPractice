package me.drizzy.api.nms.impl;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import java.util.Iterator;
import java.util.List;
import me.drizzy.api.nms.INMSImpl;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.PacketPlayOutAnimation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.practice.llIllIlIIlIIlII.IIIIIllIIlIIlII;
import dev.artixdev.practice.llIllIlIIlIIlII.IllllIlIIlIIlII;

public class v1_8_R3Impl implements INMSImpl {
   private final JavaPlugin plugin;

   public void removeArrows(Player player) {
      EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
      entityPlayer.o(0);
   }

   public void setCanCollide(Player player, boolean canCollide) {
      player.spigot().setCollidesWithEntities(canCollide);
   }

   public boolean isViewingInventory(Player player) {
      CraftPlayer craftPlayer = (CraftPlayer)player;
      return craftPlayer.getHandle().activeContainer.windowId != 0;
   }

   public void sendEatingAnimation(Player player) {
      EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
      entityPlayer.u().getTracker().sendPacketToEntity(entityPlayer, new PacketPlayOutAnimation(entityPlayer, 3));
   }

   public void setJumping(Player player) {
      EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
      entityPlayer.i(true);
      if (!entityPlayer.V() && !entityPlayer.ab()) {
         if (entityPlayer.onGround) {
            entityPlayer.bF();
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
      List<Player> sentTo = IllllIlIIlIIlII.lllllIlIIlIIlII(player.getLocation());
      Iterator var6 = sentTo.iterator();

      while(var6.hasNext()) {
         Player watcher = (Player)var6.next();
         if (!watcher.getUniqueId().equals(player.getUniqueId())) {
            IllllIlIIlIIlII.lIIIIllIIlIIlII(watcher, spawnPacket);
            IllllIlIIlIIlII.lIIIIllIIlIIlII(watcher, statusPacket);
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
      EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
      entityPlayer.f(true);
   }

   public void stopRightClick(Player player) {
      EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
      entityPlayer.f(false);
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
      Iterator var3 = IllllIlIIlIIlII.lllllIlIIlIIlII(location).iterator();

      while(var3.hasNext()) {
         Player player = (Player)var3.next();
         ProtocolLibrary.getProtocolManager().sendServerPacket(player, lightningPacket);
      }

   }

   public v1_8_R3Impl(JavaPlugin plugin) {
      this.plugin = plugin;
   }
}
