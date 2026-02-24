package dev.artixdev.practice.menus;

import java.util.HashMap;
import java.util.List;
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
import dev.artixdev.practice.utils.Messages;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.practice.utils.ModernMenuStyle;
import dev.artixdev.libs.com.cryptomorin.xseries.SkullUtils;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import java.util.stream.Collectors;

/**
 * Profile Menu – estilo moderno: 45 slots, bordas, tooltips com título | descrição | ação.
 */
public class ProfileMenu extends Menu {

    private static final int SIZE = 45;
    private final Main plugin = Main.getInstance();

    public ProfileMenu() {
        this.setPlaceholder(true);
        this.setBordered(true);
    }

    @Override
    public int getSize() {
        return SIZE;
    }

    @Override
    public String getTitle(Player player) {
        return Messages.get("MENU.PROFILE_TITLE");
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();
        PlayerProfile profile = plugin.getPlayerManager().getPlayerProfile(player.getUniqueId());
        if (profile == null) {
            return buttons;
        }

        // Linha 2 (slots 9–17): Statistics, Achievements, Player head, Leaderboard, Shop, Settings
        buttons.put(10, new StatisticsButton(profile));
        buttons.put(12, new AchievementsButton(profile));
        buttons.put(13, new PlayerHeadButton(profile, player));
        buttons.put(14, new LeaderboardButton(profile));
        buttons.put(15, new ShopButton(profile));
        buttons.put(16, new SettingsButton(profile));

        // Linha 4 (slots 27–35): Titles, ELO/Ranked, Cosmetics, Divisions
        buttons.put(28, new TitlesButton(profile));
        buttons.put(29, new RankedInfoButton(profile));
        buttons.put(30, new CosmeticsButton(profile));
        buttons.put(31, new DivisionsButton(profile));

        // Fechar (canto inferior direito)
        buttons.put(44, new CloseButton());

        return buttons;
    }

    private static List<String> modernLore(String title, String desc1, String desc2, String action) {
        return ModernMenuStyle.tooltip(title, desc1, desc2, null, action);
    }

    private class StatisticsButton extends Button {
        private final PlayerProfile profile;

        StatisticsButton(PlayerProfile profile) {
            this.profile = profile;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = new java.util.ArrayList<>(modernLore("Statistics",
                "View your combat and match statistics.",
                "Kills, deaths, wins, losses, ELO and more.",
                "Click to view detailed statistics."));
            int beforeAction = lore.size() - 1;
            lore.add(beforeAction, "");
            lore.add(beforeAction + 1, "&7Kills: &a" + profile.getKills() + " &7| &7Deaths: &4" + profile.getDeaths());
            lore.add(beforeAction + 2, "&7Wins: &e" + profile.getWins() + " &7| &7Losses: &c" + profile.getLosses());
            lore.add(beforeAction + 3, "&7ELO: &b" + profile.getElo());
            return new ItemBuilder(XMaterial.PAPER)
                .name(Messages.get("MENU.ITEM_STATISTICS"))
                .lore(lore)
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            MenuHandler.getInstance().openMenu(new StatisticsMenu(ProfileMenu.this, profile), player);
        }
    }

    private class AchievementsButton extends Button {
        private final PlayerProfile profile;

        AchievementsButton(PlayerProfile profile) {
            this.profile = profile;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            int unlocked = profile.getUnlockedAchievements().size();
            List<String> lore = modernLore("Achievements",
                "View your achievements and progress.",
                "Unlock new ones by playing and winning.",
                "Click to view achievements.");
            lore.add("");
            lore.add("&a• &7Unlocked: &a" + unlocked + "&7 achievements");
            return new ItemBuilder(XMaterial.ENCHANTED_BOOK)
                .name(Messages.get("MENU.ITEM_ACHIEVEMENTS"))
                .lore(lore)
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            MenuHandler.getInstance().openMenu(new AchievementsMenu(profile), player);
        }
    }

    private class PlayerHeadButton extends Button {
        private final PlayerProfile profile;
        private final Player target;

        PlayerHeadButton(PlayerProfile profile, Player target) {
            this.profile = profile;
            this.target = target;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = new java.util.ArrayList<>(modernLore("Your Profile",
                "Quick overview of your account.",
                "Level, XP and main stats at a glance.",
                "Click to refresh."));
            int insertIdx = lore.size() - 1; // before last line (action)
            lore.add(insertIdx, "&7Level: &a" + profile.getLevel() + " &7| &7XP: &a" + profile.getExperience());
            lore.add(insertIdx + 1, "&7Winstreak: &e" + profile.getWinStreak());
            lore.add(insertIdx + 2, "");
            ItemStack head = new ItemBuilder(XMaterial.PLAYER_HEAD).build();
            try {
                org.bukkit.inventory.meta.SkullMeta meta = (org.bukkit.inventory.meta.SkullMeta) head.getItemMeta();
                if (meta != null && target != null) {
                    meta = (org.bukkit.inventory.meta.SkullMeta) SkullUtils.applySkin(meta, target);
                    meta.setDisplayName(Messages.get("MENU.ITEM_YOUR_PROFILE", "player", target.getName()));
                    meta.setLore(lore.stream().map(ChatUtils::colorize).collect(Collectors.toList()));
                    head.setItemMeta(meta);
                    return head;
                }
            } catch (Throwable ignored) { }
            return new ItemBuilder(XMaterial.PLAYER_HEAD)
                .name(Messages.get("MENU.ITEM_YOUR_PROFILE", "player", player.getName()))
                .lore(lore)
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            // Refresh / reabrir
            MenuHandler.getInstance().openMenu(new ProfileMenu(), player);
        }
    }

    private class LeaderboardButton extends Button {
        private final PlayerProfile profile;

        LeaderboardButton(PlayerProfile profile) {
            this.profile = profile;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = modernLore("Leaderboard",
                "View the top players by ELO, wins",
                "and other statistics.",
                "Click to view leaderboard.");
            return new ItemBuilder(XMaterial.EMERALD)
                .name(Messages.get("MENU.ITEM_LEADERBOARD"))
                .lore(lore)
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            player.sendMessage(Messages.get("MENU.OPENING_LEADERBOARD"));
        }
    }

    private class ShopButton extends Button {
        private final PlayerProfile profile;

        ShopButton(PlayerProfile profile) {
            this.profile = profile;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = new java.util.ArrayList<>(modernLore("Coin Shop",
                "Buy titles, reset K/D, reset ELO",
                "and other resets with coins.",
                "Click to open shop."));
            int beforeAction = lore.size() - 1;
            lore.add(beforeAction, "");
            lore.add(beforeAction + 1, "&a• Coins: &f" + (profile != null ? profile.getCoins() : 0));
            return new ItemBuilder(XMaterial.GOLD_INGOT)
                .name(Messages.get("MENU.ITEM_SHOP"))
                .lore(lore)
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            MenuHandler.getInstance().openMenu(new ShopMenu(), player);
        }
    }

    private class SettingsButton extends Button {
        private final PlayerProfile profile;

        SettingsButton(PlayerProfile profile) {
            this.profile = profile;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = modernLore("Profile Settings",
                "Configure your preferences and",
                "personal settings.",
                "Click to open settings.");
            return new ItemBuilder(XMaterial.ANVIL)
                .name(Messages.get("MENU.ITEM_SETTINGS"))
                .lore(lore)
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            MenuHandler.getInstance().openMenu(new SettingsMenu(), player);
        }
    }

    private class TitlesButton extends Button {
        private final PlayerProfile profile;

        TitlesButton(PlayerProfile profile) {
            this.profile = profile;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = modernLore("Titles",
                "Choose your display title.",
                "Unlock new titles by meeting requirements.",
                "Click to open titles.");
            return new ItemBuilder(XMaterial.NAME_TAG)
                .name(Messages.get("MENU.ITEM_TITLES"))
                .lore(lore)
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            MenuHandler.getInstance().openMenu(new TitlesMenu(profile), player);
        }
    }

    private class RankedInfoButton extends Button {
        private final PlayerProfile profile;

        RankedInfoButton(PlayerProfile profile) {
            this.profile = profile;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = new java.util.ArrayList<>(modernLore("Ranked",
                "Your ranked stats and ELO.",
                "Win matches to climb the ladder.",
                "Click to view ranked info."));
            int beforeAction = lore.size() - 1;
            lore.add(beforeAction, "");
            lore.add(beforeAction + 1, "&a• ELO: &b" + profile.getElo());
            lore.add(beforeAction + 2, "&7Ranked W/L: &b" + profile.getRankedWins() + "&7/&c" + profile.getRankedLosses());
            return new ItemBuilder(XMaterial.GOLD_INGOT)
                .name(Messages.get("MENU.ITEM_RANKED"))
                .lore(lore)
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            player.sendMessage(Messages.get("MENU.RANKED_ELO", "elo", String.valueOf(profile.getElo())));
        }
    }

    private class CosmeticsButton extends Button {
        private final PlayerProfile profile;

        CosmeticsButton(PlayerProfile profile) {
            this.profile = profile;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = modernLore("Cosmetics",
                "Kill effects, trails and",
                "kill messages.",
                "Click to open cosmetics.");
            return new ItemBuilder(XMaterial.FIREWORK_ROCKET)
                .name(Messages.get("MENU.ITEM_COSMETICS"))
                .lore(lore)
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            try {
                player.performCommand("cosmetics");
            } catch (Exception e) {
                player.sendMessage(Messages.get("MENU.OPENING_COSMETICS"));
            }
        }
    }

    private class DivisionsButton extends Button {
        private final PlayerProfile profile;

        DivisionsButton(PlayerProfile profile) {
            this.profile = profile;
        }

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = new java.util.ArrayList<>(modernLore("Divisions",
                "View your ranked division and progress",
                "towards the next division.",
                "Click to view divisions."));
            String divName = "Unranked";
            if (plugin.getRankManager() != null && plugin.getRankManager().getDivision(profile.getElo()) != null) {
                divName = plugin.getRankManager().getDivision(profile.getElo()).getDisplayName();
            }
            int beforeAction = lore.size() - 1;
            lore.add(beforeAction, "");
            lore.add(beforeAction + 1, "&a• Current: &f" + divName);
            return new ItemBuilder(XMaterial.EMERALD)
                .name(Messages.get("MENU.ITEM_DIVISIONS"))
                .lore(lore)
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            MenuHandler.getInstance().openMenu(new DivisionsMenu(profile), player);
        }
    }

    private static class CloseButton extends Button {
        @Override
        public ItemStack getButtonItem(Player player) {
            return new ItemBuilder(XMaterial.RED_STAINED_GLASS_PANE)
                .name(Messages.get("MENU.CLOSE"))
                .lore(ModernMenuStyle.closeButtonLore())
                .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
            player.closeInventory();
        }
    }
}
