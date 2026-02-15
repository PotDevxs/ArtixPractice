package dev.artixdev.api.practice.tablist.util;

import org.bukkit.entity.Player;

public class GlitchFixEvent extends BaseEvent {
   private final Player player;

   public Player getPlayer() {
      return this.player;
   }

   public GlitchFixEvent(Player player) {
      this.player = player;
   }
}
