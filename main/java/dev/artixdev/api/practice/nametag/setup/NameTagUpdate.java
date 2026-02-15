package dev.artixdev.api.practice.nametag.setup;

import java.util.UUID;
import org.bukkit.entity.Player;

public class NameTagUpdate {
   private UUID toRefresh;
   private UUID refreshFor;

   public NameTagUpdate(Player toRefresh) {
      if (toRefresh != null) {
         this.toRefresh = toRefresh.getUniqueId();
      }
   }

   public NameTagUpdate(Player toRefresh, Player refreshFor) {
      this.toRefresh = toRefresh.getUniqueId();
      this.refreshFor = refreshFor.getUniqueId();
   }

   public UUID getToRefresh() {
      return this.toRefresh;
   }

   public UUID getRefreshFor() {
      return this.refreshFor;
   }
}
