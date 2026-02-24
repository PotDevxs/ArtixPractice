package dev.artixdev.practice.menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.Division;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.models.Rank;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.Messages;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.practice.utils.ModernMenuStyle;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

/**
 * Menu de Divisions – estilo moderno: lista de ranks com Experience e barra de progresso no tooltip.
 */
public class DivisionsMenu extends Menu {

    private static final int SIZE = 45;
    private final Main plugin = Main.getInstance();
    private final PlayerProfile profile;

    public DivisionsMenu(PlayerProfile profile) {
        this.profile = profile;
        this.setPlaceholder(true);
        this.setBordered(true);
    }

    @Override
    public int getSize() {
        return SIZE;
    }

    @Override
    public String getTitle(Player player) {
        return Messages.get("DIVISIONS.MENU_TITLE");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        dev.artixdev.practice.managers.RankManager rankManager = plugin.getRankManager();
        List<Rank> ranks = rankManager != null ? rankManager.getAllRanks() : null;

        if (ranks == null || ranks.isEmpty()) {
            buttons.put(22, new UnrankedPlaceholderButton());
            return buttons;
        }

        int elo = profile.getElo();
        Rank currentRank = rankManager.getRankByElo(elo);
        Rank nextRank = getNextRank(rankManager, elo);

        int[] contentSlots = {10, 11, 12, 13, 14, 15, 19, 20, 21, 22, 23, 24, 28, 29, 30, 31, 32, 33, 37, 38, 39, 41, 42};
        for (int i = 0; i < ranks.size() && i < contentSlots.length; i++) {
            Rank rank = ranks.get(i);
            boolean isCurrent = rank.equals(currentRank);
            buttons.put(contentSlots[i], new DivisionButton(rank, isCurrent, nextRank, elo));
        }

        buttons.put(40, new BackButton());
        return buttons;
    }

    private Rank getNextRank(dev.artixdev.practice.managers.RankManager rankManager, int elo) {
        Rank current = rankManager.getRankByElo(elo);
        if (current == null) return null;
        int nextPriority = current.getPriority() + 1;
        for (Rank r : rankManager.getAllRanks()) {
            if (r.getPriority() == nextPriority) return r;
        }
        return null;
    }

    private static String divisionColorCode(ChatColor color) {
        if (color == null) return "&7";
        return "&" + color.getChar();
    }

    private static String buildExperienceLine(int elo, Rank current, Rank next) {
        if (current == null || next == null) return "0/0 (0%)";
        int currentElo = current.getEloNeeded();
        int nextElo = next.getEloNeeded();
        int range = nextElo - currentElo;
        if (range <= 0) return elo + "/" + nextElo + " (100%)";
        int pct = Math.min(100, Math.max(0, ((elo - currentElo) * 100) / range));
        return elo + "/" + nextElo + " (" + String.format("%.2f", pct) + "%)";
    }

    private static String buildProgressBar(int elo, Rank current, Rank next) {
        if (current == null || next == null) return "&8░░░░░░░░░░";
        int currentElo = current.getEloNeeded();
        int nextElo = next.getEloNeeded();
        int range = nextElo - currentElo;
        if (range <= 0) return "&a██████████";
        int pct = Math.min(100, Math.max(0, ((elo - currentElo) * 100) / range));
        int len = 10;
        int filled = (pct * len) / 100;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < filled; i++) sb.append("&a█");
        for (int i = filled; i < len; i++) sb.append("&8░");
        return sb.toString();
    }

    private class UnrankedPlaceholderButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = ModernMenuStyle.tooltip("Unranked",
                new String[]{
                    "Play ranked matches to earn ELO and",
                    "unlock divisions. Win to climb the ladder."
                },
                "Click to view requirements.");
            return new ItemBuilder(XMaterial.GRAY_DYE)
                .name(Messages.get("DIVISIONS.UNRANKED_NAME"))
                .lore(lore)
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) { }
    }

    private class DivisionButton extends Button {
        private final Rank rank;
        private final boolean isCurrent;
        private final Rank nextRank;
        private final int playerElo;

        DivisionButton(Rank rank, boolean isCurrent, Rank nextRank, int playerElo) {
            this.rank = rank;
            this.isCurrent = isCurrent;
            this.nextRank = nextRank;
            this.playerElo = playerElo;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            Division div = new Division(rank);
            String name = div.getDisplayName();
            String colorCode = divisionColorCode(div.getColor());

            List<String> lore = new ArrayList<>();
            lore.add(ChatUtils.colorize(colorCode + "&l" + name));
            lore.add("");
            if (isCurrent && nextRank != null) {
                String expLine = buildExperienceLine(playerElo, rank, nextRank);
                lore.add(Messages.get("DIVISIONS.LORE_EXPERIENCE", "experience", expLine));
                String bar = buildProgressBar(playerElo, rank, nextRank);
                double pct = 0;
                int currentElo = rank.getEloNeeded();
                int nextElo = nextRank.getEloNeeded();
                int range = nextElo - currentElo;
                if (range > 0) {
                    pct = Math.min(100, Math.max(0, ((playerElo - currentElo) * 100.0) / range));
                } else {
                    pct = 100;
                }
                lore.add(Messages.get("DIVISIONS.LORE_PROGRESS", "bar", bar, "percent", String.format("%.2f", pct)));
            } else {
                lore.add(Messages.get("DIVISIONS.LORE_ELO_REQUIRED", "elo", String.valueOf(rank.getEloNeeded())));
                if (isCurrent) {
                    lore.add(Messages.get("DIVISIONS.LORE_YOUR_DIVISION"));
                }
            }
            lore.add("");
            lore.add(Messages.get("DIVISIONS.LORE_CLICK_DETAILS"));

            ItemStack icon = rank.getIcon();
            if (icon != null && icon.getType() != org.bukkit.Material.AIR) {
                ItemStack item = icon.clone();
                org.bukkit.inventory.meta.ItemMeta meta = item.getItemMeta();
                if (meta != null) {
                    meta.setDisplayName(ChatUtils.colorize(colorCode + "&l" + name));
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                }
                return item;
            }
            XMaterial fallback = isCurrent ? XMaterial.GOLD_INGOT : XMaterial.IRON_INGOT;
            return new ItemBuilder(fallback)
                .name(ChatUtils.colorize(colorCode + "&l" + name))
                .lore(lore)
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) { }
    }

    private static class BackButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(XMaterial.ARROW)
                .name(Messages.get("DIVISIONS.BACK"))
                .lore(ModernMenuStyle.tooltip(null, new String[]{"Return to Profile menu."}, null, "Click to go back."))
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            MenuHandler.getInstance().openMenu(new ProfileMenu(), player);
        }
    }
}
