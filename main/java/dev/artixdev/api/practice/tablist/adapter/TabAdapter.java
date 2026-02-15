package dev.artixdev.api.practice.tablist.adapter;

import java.util.List;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.tablist.setup.TabEntry;

public interface TabAdapter {
   String getHeader(Player var1);

   String getFooter(Player var1);

   List<TabEntry> getLines(Player var1);
}
