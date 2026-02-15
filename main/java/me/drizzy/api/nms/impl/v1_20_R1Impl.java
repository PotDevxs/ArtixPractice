package me.drizzy.api.nms.impl;

import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import java.util.Iterator;
import java.util.List;
import me.drizzy.api.nms.INMSImpl;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.server.level.ChunkProviderServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.EnumHand;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.practice.llIllIlIIlIIlII.IIIIIllIIlIIlII;
import dev.artixdev.practice.llIllIlIIlIIlII.IllllIlIIlIIlII;

public class v1_20_R1Impl implements INMSImpl {
   private final JavaPlugin plugin;

   public void removeArrows(Player player) {
      CraftPlayer craftPlayer = (CraftPlayer)player;
      craftPlayer.getHandle().setArrowCount(0, true);
   }

   public void setCanCollide(Player player, boolean canCollide) {
      player.setCollidable(canCollide);
   }

   public boolean isViewingInventory(Player player) {
      CraftPlayer craftPlayer = (CraftPlayer)player;
      return craftPlayer.getHandle().bR.j != 0;
   }

   public void sendEatingAnimation(Player player) {
      Entity entityPlayer = ((CraftPlayer)player).getHandle();
      ChunkProviderServer chunkproviderserver = ((WorldServer)entityPlayer.dI()).k();
      chunkproviderserver.b(entityPlayer, new PacketPlayOutAnimation(entityPlayer, 2));
   }

   public void setJumping(Player player) {
      EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
      entityPlayer.r(true);
      if (!entityPlayer.aV() && !entityPlayer.bi()) {
         if (player.isOnGround()) {
            entityPlayer.eW();
         }
      } else {
         entityPlayer.f(entityPlayer.dl().b(0.0D, 0.03999999910593033D, 0.0D));
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
      CraftPlayer craftPlayer = (CraftPlayer)player;
      craftPlayer.getHandle().c(EnumHand.a);
   }

   public void stopRightClick(Player player) {
      CraftPlayer craftPlayer = (CraftPlayer)player;
      craftPlayer.getHandle().fo();
   }

   public void setUnbreakable(ItemStack itemStack) {
      if (itemStack.hasItemMeta()) {
         ItemMeta itemMeta = itemStack.getItemMeta();
         if (itemMeta != null) {
            itemMeta.setUnbreakable(true);
         }
      }
   }

   public void spawnLightning(Location location) {
      location.getWorld().spigot().strikeLightningEffect(location, true);
   }

   public v1_20_R1Impl(JavaPlugin plugin) {
      this.plugin = plugin;
   }
}
