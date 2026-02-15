package dev.artixdev.api.practice.tablist.thread;

import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import dev.artixdev.api.practice.tablist.TablistHandler;
import dev.artixdev.api.practice.tablist.setup.TabEntry;
import dev.artixdev.api.practice.tablist.setup.TabLayout;

public class TablistThread extends BukkitRunnable {
   private static final Logger log = LogManager.getLogger(TablistThread.class);
   private final TablistHandler handler;

   public void run() {
      if (!this.handler.getPlugin().isEnabled()) {
         this.cancel();
      } else {
         this.tick();
      }
   }

   private void tick() {
      if (this.handler.getPlugin().isEnabled()) {
         Iterator var1 = Bukkit.getOnlinePlayers().iterator();

         while(var1.hasNext()) {
            Player player = (Player)var1.next();
            TabLayout layout = (TabLayout)this.handler.getLayoutMapping().get(player.getUniqueId());
            Iterator var4 = this.handler.getAdapter().getLines(player).iterator();

            while(var4.hasNext()) {
               TabEntry entry = (TabEntry)var4.next();
               int x = entry.getX();
               int y = entry.getY();
               int i = y * layout.getMod() + x;

               try {
                  layout.update(i, entry.getText(), entry.getPing(), entry.getSkin());
               } catch (NullPointerException e) {
                  if (!this.handler.getPlugin().getName().equals("Bolt") || this.handler.isDebug()) {
                     log.fatal("[{}] There was an error updating tablist for {}", this.handler.getPlugin().getName(), player.getName());
                     log.error(e);
                     e.printStackTrace();
                  }
               } catch (Exception e) {
                  log.fatal("[{}] There was an error updating tablist for {}", this.handler.getPlugin().getName(), player.getName());
                  log.error(e);
                  e.printStackTrace();
               }
            }
         }

      }
   }

   public TablistThread(TablistHandler handler) {
      this.handler = handler;
   }
}
