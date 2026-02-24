package dev.artixdev.practice.menus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.pagination.PaginatedMenu;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.configs.menus.DivisionMenus;
import dev.artixdev.practice.managers.RankManager;
import dev.artixdev.practice.models.Rank;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;

import java.util.ArrayList;

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
        RankManager rankManager = Main.getInstance() != null ? Main.getInstance().getRankManager() : null;
        if (rankManager == null) return buttons;
        List<Rank> ranks = rankManager.getAllRanks();
        if (ranks == null) return buttons;
        int slot = 0;
        for (Rank rank : ranks) {
            buttons.put(slot++, new RankDisplayButton(rank));
        }
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
        return 54;
    }

    private static class RankDisplayButton extends Button {
        private final Rank rank;

        RankDisplayButton(Rank rank) {
            this.rank = rank;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            ItemStack icon = rank.getIcon() != null ? rank.getIcon().clone() : dev.artixdev.libs.com.cryptomorin.xseries.XMaterial.IRON_INGOT.parseItem();
            String name = rank.getDisplayName() != null ? rank.getDisplayName() : rank.getName();
            List<String> lore = new ArrayList<>();
            lore.add(ChatUtils.translate("&7ELO required: &f" + rank.getEloNeeded()));
            return new ItemBuilder(icon != null ? icon : dev.artixdev.libs.com.cryptomorin.xseries.XMaterial.IRON_INGOT.parseItem())
                .name(ChatUtils.translate("&" + (rank.getColor() != null ? rank.getColor().getChar() : '7') + name))
                .lore(lore)
                .build();
        }
    }
}
