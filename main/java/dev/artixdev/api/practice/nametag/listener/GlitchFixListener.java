package dev.artixdev.api.practice.nametag.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import dev.artixdev.api.practice.nametag.NameTagHandler;
import dev.artixdev.api.practice.tablist.util.GlitchFixEvent;

public class GlitchFixListener implements Listener {
   private final NameTagHandler nameTagHandler;

   @EventHandler
   public void onGlitch(GlitchFixEvent event) {
      Player player = event.getPlayer();
      this.nameTagHandler.reloadPlayer(player);
      this.nameTagHandler.reloadOthersFor(player);
   }

   public GlitchFixListener(NameTagHandler nameTagHandler) {
      this.nameTagHandler = nameTagHandler;
   }
}
