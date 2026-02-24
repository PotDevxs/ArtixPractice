package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.practice.models.Match;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.ArrayList;
import java.util.List;

public class MatchButton extends Button {
    private final Match match;
    private final PlayerProfile playerProfile;

    public MatchButton(PlayerProfile playerProfile, Match match) {
        this.playerProfile = playerProfile;
        this.match = match;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return createMatchItem(match);
    }

    private ItemStack createMatchItem(Match match) {
        if (match == null) return new ItemBuilder(XMaterial.BARRIER).name(ChatUtils.colorize("&cNo match")).build();
        String kitName = match.getKitType() != null ? match.getKitType().getDisplayName() : "?";
        String arenaName = match.getArena() != null ? match.getArena().getName() : "Unknown";
        String p1 = match.getPlayer1() != null ? match.getPlayer1().getName() : "-";
        String p2 = match.getPlayer2() != null ? match.getPlayer2().getName() : "-";
        List<String> lore = new ArrayList<>();
        lore.add(ChatUtils.colorize("&7Kit: &f" + kitName));
        lore.add(ChatUtils.colorize("&7Arena: &f" + arenaName));
        lore.add(ChatUtils.colorize("&7Players: &f" + p1 + " &7vs &f" + p2));
        return new ItemBuilder(XMaterial.DIAMOND_SWORD).name(ChatUtils.colorize("&eMatch")).lore(lore).build();
    }
}