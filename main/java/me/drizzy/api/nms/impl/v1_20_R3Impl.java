package me.drizzy.api.nms.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import me.drizzy.api.nms.INMSImpl;
import net.minecraft.network.protocol.game.PacketPlayOutAnimation;
import net.minecraft.server.level.ChunkProviderServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.EnumHand;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.entity.type.EntityTypes;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.Vector3d;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerDestroyEntities;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityStatus;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerSpawnEntity;
import dev.artixdev.practice.llIllIlIIlIIlII.IIIIIllIIlIIlII;
import dev.artixdev.practice.llIllIlIIlIIlII.IllllIlIIlIIlII;

public class v1_20_R3Impl implements INMSImpl {
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
      ChunkProviderServer chunkproviderserver = ((WorldServer)entityPlayer.dM()).l();
      chunkproviderserver.b(entityPlayer, new PacketPlayOutAnimation(entityPlayer, 2));
   }

   public void setJumping(Player player) {
   }

   public void animateDeath(Player player) {
      int entityId = IIIIIllIIlIIlII.llllIIlIIlIIlII();
      Location location = player.getLocation();
      Entity entity = ((CraftPlayer)player).getHandle();
      WrapperPlayServerSpawnEntity spawnPacket = new WrapperPlayServerSpawnEntity(entityId, Optional.of(player.getUniqueId()), EntityTypes.PLAYER, new Vector3d(location.getX(), location.getY(), location.getZ()), entity.dC(), entity.dE(), entity.cp(), 0, Optional.empty());
      WrapperPlayServerEntityStatus statusPacket = new WrapperPlayServerEntityStatus(entityId, 3);
      List<Player> sentTo = IllllIlIIlIIlII.lllllIlIIlIIlII(player.getLocation());
      Iterator var8 = sentTo.iterator();

      while(var8.hasNext()) {
         Player watcher = (Player)var8.next();
         if (!watcher.getUniqueId().equals(player.getUniqueId())) {
            IllllIlIIlIIlII.sendPacket(watcher, spawnPacket);
            IllllIlIIlIIlII.sendPacket(watcher, statusPacket);
         }
      }

      WrapperPlayServerDestroyEntities destroyPacket = new WrapperPlayServerDestroyEntities(entityId);
      Bukkit.getScheduler().runTaskLater(this.plugin, () -> {
         sentTo.forEach((watcher) -> {
            IllllIlIIlIIlII.sendPacket(watcher, destroyPacket);
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

   public v1_20_R3Impl(JavaPlugin plugin) {
      this.plugin = plugin;
   }
}
