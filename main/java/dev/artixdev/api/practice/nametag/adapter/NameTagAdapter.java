package dev.artixdev.api.practice.nametag.adapter;

import org.bukkit.entity.Player;
import dev.artixdev.api.practice.nametag.NameTagHandler;
import dev.artixdev.api.practice.nametag.setup.NameTagTeam;

public abstract class NameTagAdapter {
   public abstract NameTagTeam fetchNameTag(Player var1, Player var2);

   public NameTagTeam createNameTag(String prefix, String suffix) {
      return NameTagHandler.getInstance().getOrCreate(prefix, suffix);
   }
}
