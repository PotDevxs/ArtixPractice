package dev.artixdev.practice.menus;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.api.practice.menu.Menu;
import dev.artixdev.api.practice.menu.MenuHandler;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.practice.utils.StatisticsUtils;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import java.util.ArrayList;
import java.util.List;

public class StatisticsMenu extends Menu {
    private final PlayerProfile playerProfile;
    private final Menu previousMenu;

    public StatisticsMenu(Menu previousMenu, PlayerProfile playerProfile) {
        this.setPlaceholder(true);
        this.playerProfile = playerProfile;
        this.previousMenu = previousMenu;
    }

    @Override
    public String getTitle(Player player) {
        return "Statistics - " + playerProfile.getUsername();
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        return createStatisticsButtons(player);
    }

    @Override
    public int getSize() {
        return 54;
    }

    @Override
    public void onClose(Player player) {
        Main plugin = Main.getInstance();
        PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
        if (profile != null && plugin.getStorageManager() != null) {
            plugin.getStorageManager().savePlayerProfile(profile);
        }
    }

    private Map<Integer, Button> createStatisticsButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        int k = playerProfile.getKills();
        int d = playerProfile.getDeaths();
        int w = playerProfile.getWins();
        int l = playerProfile.getLosses();
        buttons.put(4, new StatDisplayButton(XMaterial.PAPER, "&6&lStatistics",
            "&7Kills: &f" + k,
            "&7Deaths: &f" + d,
            "&7Wins: &f" + w,
            "&7Losses: &f" + l,
            "&7Winrate: &f" + StatisticsUtils.formatWinrate(w, l),
            "&7KDR: &f" + StatisticsUtils.formatKdr(k, d),
            "&7ELO: &f" + playerProfile.getElo()));
        if (previousMenu != null) {
            buttons.put(49, new BackButton());
        }
        return buttons;
    }

    private static class StatDisplayButton extends Button {
        private final XMaterial mat;
        private final String name;
        private final String[] lore;

        StatDisplayButton(XMaterial mat, String name, String... lore) {
            this.mat = mat;
            this.name = name;
            this.lore = lore;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> loreList = new ArrayList<>();
            for (String line : lore) loreList.add(ChatUtils.translate(line));
            return new ItemBuilder(mat).name(ChatUtils.translate(name)).lore(loreList).build();
        }
    }

    private class BackButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(XMaterial.ARROW).name(ChatUtils.translate("&cBack")).build();
        }

        @Override
        public void clicked(Player player, ClickType clickType) {
            if (previousMenu != null) {
                MenuHandler.getInstance().openMenu(previousMenu, player);
            }
        }
    }
}