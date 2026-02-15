package dev.artixdev.api.practice.nametag.adapter;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.nametag.setup.NameTagTeam;

public class DefaultNameTagAdapter extends NameTagAdapter {
   public NameTagTeam fetchNameTag(Player toRefresh, Player refreshFor) {
      return this.createNameTag("&c", "&9 [Gay]");
   }
}
