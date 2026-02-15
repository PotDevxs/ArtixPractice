package dev.artixdev.api.practice.spigot.knockback.impl.fox;

import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import dev.artixdev.api.practice.spigot.event.IListener;
import dev.artixdev.api.practice.spigot.event.impl.RefinePotionAddEvent;
import dev.artixdev.api.practice.spigot.event.impl.RefinePotionExtendEvent;
import dev.artixdev.api.practice.spigot.event.impl.RefinePotionRemoveEvent;
import dev.artixdev.api.practice.spigot.event.impl.RefineRefinePotionExpireEvent;

public class FoxSpigotListener implements Listener, IListener {
   private Class<?> potionEffectExpireEventClass;
   private Class<?> potionEffectAddEventClass;
   private Class<?> potionEffectExtendEventClass;
   private Class<?> potionEffectRemoveEventClass;
   private boolean paperEventsAvailable = false;

   public FoxSpigotListener() {
      try {
         potionEffectExpireEventClass = Class.forName("org.bukkit.event.entity.PotionEffectExpireEvent");
         potionEffectAddEventClass = Class.forName("org.bukkit.event.entity.PotionEffectAddEvent");
         potionEffectExtendEventClass = Class.forName("org.bukkit.event.entity.PotionEffectExtendEvent");
         potionEffectRemoveEventClass = Class.forName("org.bukkit.event.entity.PotionEffectRemoveEvent");
         paperEventsAvailable = true;
      } catch (ClassNotFoundException e) {
         // Paper events not available, will use alternative approach
         paperEventsAvailable = false;
      }
   }

   @EventHandler
   public void onExpire(Object event) {
      if (!paperEventsAvailable || !potionEffectExpireEventClass.isInstance(event)) {
         return;
      }
      try {
         Method getEntity = potionEffectExpireEventClass.getMethod("getEntity");
         Method getEffect = potionEffectExpireEventClass.getMethod("getEffect");
         Object entity = getEntity.invoke(event);
         Object effect = getEffect.invoke(event);
         RefineRefinePotionExpireEvent custom = new RefineRefinePotionExpireEvent((org.bukkit.entity.LivingEntity)entity, (org.bukkit.potion.PotionEffect)effect);
         Bukkit.getPluginManager().callEvent(custom);
      } catch (Exception e) {
         // Reflection failed
      }
   }

   @EventHandler
   public void onAdd(Object event) {
      if (!paperEventsAvailable || !potionEffectAddEventClass.isInstance(event)) {
         return;
      }
      try {
         Method getEntity = potionEffectAddEventClass.getMethod("getEntity");
         Method getEffect = potionEffectAddEventClass.getMethod("getEffect");
         Object entity = getEntity.invoke(event);
         Object effect = getEffect.invoke(event);
         RefinePotionAddEvent.EffectAddReason reason = RefinePotionAddEvent.EffectAddReason.UNKNOWN;
         RefinePotionAddEvent custom = new RefinePotionAddEvent((org.bukkit.entity.LivingEntity)entity, (org.bukkit.potion.PotionEffect)effect, reason);
         Bukkit.getPluginManager().callEvent(custom);
      } catch (Exception e) {
         // Reflection failed
      }
   }

   @EventHandler
   public void onExtend(Object event) {
      if (!paperEventsAvailable || !potionEffectExtendEventClass.isInstance(event)) {
         return;
      }
      try {
         Method getEntity = potionEffectExtendEventClass.getMethod("getEntity");
         Method getEffect = potionEffectExtendEventClass.getMethod("getEffect");
         Method getOldEffect = potionEffectExtendEventClass.getMethod("getOldEffect");
         Object entity = getEntity.invoke(event);
         Object effect = getEffect.invoke(event);
         Object oldEffect = getOldEffect.invoke(event);
         RefinePotionAddEvent.EffectAddReason reason = RefinePotionAddEvent.EffectAddReason.UNKNOWN;
         RefinePotionExtendEvent custom = new RefinePotionExtendEvent((org.bukkit.entity.LivingEntity)entity, (org.bukkit.potion.PotionEffect)effect, reason, (org.bukkit.potion.PotionEffect)oldEffect);
         Bukkit.getPluginManager().callEvent(custom);
      } catch (Exception e) {
         // Reflection failed
      }
   }

   @EventHandler
   public void onRemove(Object event) {
      if (!paperEventsAvailable || !potionEffectRemoveEventClass.isInstance(event)) {
         return;
      }
      try {
         Method getEntity = potionEffectRemoveEventClass.getMethod("getEntity");
         Method getEffect = potionEffectRemoveEventClass.getMethod("getEffect");
         Object entity = getEntity.invoke(event);
         Object effect = getEffect.invoke(event);
         RefinePotionRemoveEvent custom = new RefinePotionRemoveEvent((org.bukkit.entity.LivingEntity)entity, (org.bukkit.potion.PotionEffect)effect);
         Bukkit.getPluginManager().callEvent(custom);
      } catch (Exception e) {
         // Reflection failed
      }
   }

   public void register(JavaPlugin plugin) {
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
   }

   public boolean isApplicable() {
      return true;
   }
}
