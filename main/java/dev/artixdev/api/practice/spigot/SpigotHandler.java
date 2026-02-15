package dev.artixdev.api.practice.spigot;

import com.google.common.base.Preconditions;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.plugin.java.JavaPlugin;

import dev.artixdev.api.practice.nametag.NameTagHandler;
import dev.artixdev.api.practice.spigot.equipment.EquipmentListener;
import dev.artixdev.api.practice.spigot.event.IListener;
import dev.artixdev.api.practice.spigot.hider.EntityHider;
import dev.artixdev.api.practice.spigot.knockback.IKnockbackType;

public class SpigotHandler {
   private static final Logger log = LogManager.getLogger(SpigotHandler.class);
   private static SpigotHandler instance;
   private final JavaPlugin plugin;
   private IKnockbackType knockback;
   private IListener listener;
   protected SpigotType type;

   public SpigotHandler(JavaPlugin plugin) {
      Preconditions.checkNotNull(plugin, "Plugin instance can not be null!");
      this.plugin = plugin;
      instance = this;
   }

   public void init() {
      this.init(false);
   }

   public void init(boolean teamFight) {
      this.type = SpigotType.get();
      this.knockback = this.type.getKnockbackType();
      if (teamFight && this.type != SpigotType.Default) {
         this.listener = this.type.getListener();
         this.listener.register(this.plugin);
         EquipmentListener equipmentListener = new EquipmentListener();
         this.plugin.getServer().getPluginManager().registerEvents(equipmentListener, this.plugin);
      }
   }

   /** @deprecated */
   @Deprecated
   public void initiateEntityHider() {
      boolean protocolLib = this.plugin.getServer().getPluginManager().isPluginEnabled("ProtocolLib");
      Preconditions.checkArgument(protocolLib, "ProtocolLib is required for the EntityHider!");
      if (!this.isEntityHiderRequired()) {
         log.info("[EntityHider] Detected a decent spigot, using spigot-sided entity hider!");
      } else {
         EntityHider entityHider = new EntityHider(this.plugin, EntityHider.Policy.BLACKLIST);
         entityHider.startListening();
         log.info("[EntityHider] Successfully enabled the custom Entity Hider, intercepting packets...");
      }
   }

   public boolean isEntityHiderRequired() {
      boolean required = true;

      Class classInstance;
      try {
         classInstance = Class.forName("net.minecraft.server.v1_8_R3.EntityItem");
         Field field = classInstance.getDeclaredField("owner");
         required = false;
      } catch (NoSuchFieldException | ClassNotFoundException ignored) {
      }

      Method method;
      try {
         classInstance = Class.forName("net.minecraft.server.v1_8_R3.PlayerList");
         method = classInstance.getDeclaredMethod("sendPacketNearbyIncludingSelf");
         required = false;
      } catch (NoSuchMethodException | ClassNotFoundException ignored) {
      }

      try {
         classInstance = Class.forName("org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer");
         method = classInstance.getDeclaredMethod("canSeeEntity");
         required = false;
      } catch (NoSuchMethodException | ClassNotFoundException ignored) {
      }

      return required;
   }

   public JavaPlugin getPlugin() {
      return this.plugin;
   }

   public IKnockbackType getKnockback() {
      return this.knockback;
   }

   public IListener getListener() {
      return this.listener;
   }

   public SpigotType getType() {
      return this.type;
   }

   public static SpigotHandler getInstance() {
      if (instance == null) {
         // Try to get plugin from Bukkit
         JavaPlugin plugin = (JavaPlugin) org.bukkit.Bukkit.getPluginManager().getPlugin("Bolt");
         if (plugin == null) {
            // Try to get any JavaPlugin
            for (org.bukkit.plugin.Plugin p : org.bukkit.Bukkit.getPluginManager().getPlugins()) {
               if (p instanceof JavaPlugin) {
                  plugin = (JavaPlugin) p;
                  break;
               }
            }
         }
         if (plugin != null) {
            instance = new SpigotHandler(plugin);
         }
      }
      return instance;
   }

   public NameTagHandler getNameTagHandler() {
      return new NameTagHandler(null);
   }
}
