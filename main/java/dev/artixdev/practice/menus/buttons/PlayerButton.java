package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.ArrayList;
import java.util.List;

public class PlayerButton extends Button {
    private final PlayerProfile playerProfile;

    public PlayerButton(PlayerProfile playerProfile) {
        this.playerProfile = playerProfile;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return createPlayerItem(playerProfile);
    }

    private ItemStack createPlayerItem(PlayerProfile profile) {
        if (profile == null) return new ItemBuilder(XMaterial.BARRIER).name(ChatUtils.colorize("&cNo player")).build();
        String name = profile.getUsername() != null ? profile.getUsername() : "Unknown";
        List<String> lore = new ArrayList<>();
        lore.add(ChatUtils.colorize("&7ELO: &f" + profile.getElo()));
        lore.add(ChatUtils.colorize("&7Wins: &f" + profile.getWins() + " &7| Losses: &f" + profile.getLosses()));
        return new ItemBuilder(XMaterial.PLAYER_HEAD).name(ChatUtils.colorize("&f" + name)).lore(lore).build();
    }
}
