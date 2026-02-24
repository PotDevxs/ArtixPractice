package dev.artixdev.api.practice.tablist.adapter.impl;

import java.util.List;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.tablist.adapter.TabAdapter;
import dev.artixdev.api.practice.tablist.setup.TabEntry;
import dev.artixdev.api.practice.tablist.util.Skin;
import dev.artixdev.api.practice.tablist.util.StringUtils;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class ExampleAdapter implements TabAdapter {

   private static int getPlayerPing(Player player) {
      try {
         return (int) Player.class.getMethod("getPing").invoke(player);
      } catch (Exception e) {
         return 0;
      }
   }
   public String getHeader(Player player) {
      return "&bArtixDevelopment";
   }

   public String getFooter(Player player) {
      return "&edc.artixdevelopment.com";
   }

   public List<TabEntry> getLines(Player player) {
      List<TabEntry> entries = new ObjectArrayList();

      for(int i = 0; i < 4; ++i) {
         int ping = StringUtils.MINOR_VERSION > 12 ? getPlayerPing(player) : 69;
         Skin skin = player.isSprinting() ? Skin.getPlayer(player) : Skin.YOUTUBE_SKIN;
         TabEntry tabEntry = new TabEntry(i, 0, "&#FF0000Sprinting " + player.isSprinting() + "(" + i + ")", ping, skin);
         entries.add(tabEntry);
      }

      return entries;
   }
}
