package dev.artixdev.practice.menus;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.bukkit.entity.Player;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.pagination.PaginatedMenu;
import dev.artixdev.practice.configs.menus.DivisionMenus;
import dev.artixdev.practice.models.Rank;

public class DivisionMenu extends PaginatedMenu {
    
    public DivisionMenu() {
        this.setBordered(true);
    }

    @Override
    public String getPrePaginatedTitle(Player player) {
        return DivisionMenus.DIVISION_TITLE;
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        
        // Get rank manager and available ranks
        // This would need to be injected or accessed through a service locator
        // For now, using placeholder logic
        // RankManager rankManager = Practice.getInstance().getRankManager();
        // List<Rank> ranks = rankManager.getRanks();
        
        // Placeholder for ranks - in real implementation, get from RankManager
        // List<Rank> ranks = new ArrayList<>();
        
        int slot = 0;
        // for (Rank rank : ranks) {
        //     buttons.put(slot++, new DivisionButton(rank));
        // }
        
        return buttons;
    }

    @Override
    public Map<Integer, Button> getGlobalButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        // Add global buttons like back button, etc.
        return buttons;
    }

    @Override
    public int getSize() {
        return 54; // Example size
    }
}
