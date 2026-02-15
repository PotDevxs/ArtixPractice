package me.drizzy.api.hider;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.events.PacketContainer;
import java.util.Iterator;
import me.drizzy.api.nms.INMSImpl;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityHider {
   private final ProtocolManager manager;
   private final JavaPlugin plugin;
   private final Listener bukkitListener;
   private AbstractPacketListener packetListener;

   public EntityHider(JavaPlugin plugin, INMSImpl impl) {
      this.plugin = plugin;
      this.manager = ProtocolLibrary.getProtocolManager();
      this.bukkitListener = this.constructBukkit();
   }

   public void startListening(AbstractPacketListener adapter) {
      try {
         this.packetListener = adapter;
         this.manager.addPacketListener(adapter);
         Bukkit.getPluginManager().registerEvents(this.bukkitListener, this.plugin);
      } catch (Throwable e) {
         throw e;
      }
   }

   private Listener constructBukkit() {
      return new Listener() {
         @EventHandler(
            priority = EventPriority.MONITOR
         )
         public void onPlayerPickupItem(PlayerPickupItemEvent event) {
            Player receiver = event.getPlayer();
            Item item = event.getItem();
            Player dropper = EntityHider.this.packetListener.getPlayerWhoDropped(item);
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
               Arrow arrow = (Arrow)item.getItemStack();
               if (arrow.getShooter() instanceof Player) {
                  Player shooter = (Player)arrow.getShooter();
                  if (!receiver.canSee(shooter)) {
                     event.setCancelled(true);
                  }

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

   public void destroy(Player player, int entityId) {
      PacketContainer container = new PacketContainer(Server.ENTITY_DESTROY);
      container.getIntegerArrays().write(0, new int[]{entityId});

      try {
         ProtocolLibrary.getProtocolManager().sendServerPacket(player, container);
      } catch (Exception ignored) {
      }

   }
}
