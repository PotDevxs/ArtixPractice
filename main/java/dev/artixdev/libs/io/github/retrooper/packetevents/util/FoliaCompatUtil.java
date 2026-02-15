package dev.artixdev.libs.io.github.retrooper.packetevents.util;

import java.lang.reflect.Method;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import dev.artixdev.libs.com.github.retrooper.packetevents.util.reflection.Reflection;

public class FoliaCompatUtil {
   private static boolean folia;

   public static boolean isFolia() {
      return folia;
   }

   public static void runTaskAsync(Plugin plugin, Runnable run) {
      if (!folia) {
         Bukkit.getScheduler().runTaskAsynchronously(plugin, run);
      } else {
         Executors.defaultThreadFactory().newThread(run).start();
      }
   }

   public static void runTaskTimerAsync(Plugin plugin, Consumer<Object> run, long delay, long period) {
      if (!folia) {
         Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () -> {
            run.accept((Object)null);
         }, delay, period);
      } else {
         try {
            Method getSchedulerMethod = Reflection.getMethod(Server.class, (String)"getGlobalRegionScheduler", 0);
            Object globalRegionScheduler = getSchedulerMethod.invoke(Bukkit.getServer());
            Class<?> schedulerClass = globalRegionScheduler.getClass();
            Method executeMethod = schedulerClass.getMethod("runAtFixedRate", Plugin.class, Consumer.class, Long.TYPE, Long.TYPE);
            executeMethod.invoke(globalRegionScheduler, plugin, run, delay, period);
         } catch (Exception e) {
            e.printStackTrace();
         }

      }
   }

   public static void runTask(Plugin plugin, Consumer<Object> run) {
      if (!folia) {
         Bukkit.getScheduler().runTask(plugin, () -> {
            run.accept((Object)null);
         });
      } else {
         try {
            Method getSchedulerMethod = Reflection.getMethod(Server.class, (String)"getGlobalRegionScheduler", 0);
            Object globalRegionScheduler = getSchedulerMethod.invoke(Bukkit.getServer());
            Class<?> schedulerClass = globalRegionScheduler.getClass();
            Method executeMethod = schedulerClass.getMethod("run", Plugin.class, Consumer.class);
            executeMethod.invoke(globalRegionScheduler, plugin, run);
         } catch (Exception e) {
            e.printStackTrace();
         }

      }
   }

   public static void runTaskOnInit(Plugin plugin, Runnable run) {
      if (!folia) {
         Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, run);
      } else {
         Class serverInitEventClass;
         try {
            serverInitEventClass = Class.forName("io.papermc.paper.threadedregions.RegionizedServerInitEvent");
         } catch (ReflectiveOperationException e) {
            throw new RuntimeException(e);
         }

         Bukkit.getServer().getPluginManager().registerEvent(serverInitEventClass, new Listener() {
         }, EventPriority.HIGHEST, (listener, event) -> {
            run.run();
         }, plugin);
      }
   }

   public static void runTaskForEntity(Entity entity, Plugin plugin, Runnable run, Runnable retired, long delay) {
      if (!folia) {
         Bukkit.getScheduler().runTaskLater(plugin, run, delay);
      } else {
         try {
            Method getSchedulerMethod = Reflection.getMethod(Entity.class, (String)"getScheduler", 0);
            Object entityScheduler = getSchedulerMethod.invoke(entity);
            Class<?> schedulerClass = entityScheduler.getClass();
            Method executeMethod = schedulerClass.getMethod("execute", Plugin.class, Runnable.class, Runnable.class, Long.TYPE);
            executeMethod.invoke(entityScheduler, plugin, run, retired, delay);
         } catch (Exception e) {
            e.printStackTrace();
         }

      }
   }

   static {
      try {
         Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
         folia = true;
      } catch (ClassNotFoundException ignored) {
         folia = false;
      }

   }
}
