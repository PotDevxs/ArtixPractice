package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.practice.models.Team;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.ArrayList;
import java.util.List;

public class TeamButton extends Button {
    private final Team team;
    private final PlayerProfile playerProfile;
    private final PlayerProfile opponentProfile;

    public TeamButton(Team team, PlayerProfile playerProfile, PlayerProfile opponentProfile) {
        this.team = team;
        this.playerProfile = playerProfile;
        this.opponentProfile = opponentProfile;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return createTeamItem(team);
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (clickType.isLeftClick()) {
            player.sendMessage(ChatUtils.colorize("&7Team: &f" + (team != null ? team.getLeader().getName() : "—")));
        }
    }

    private ItemStack createTeamItem(Team team) {
        if (team == null) {
            return new ItemBuilder(XMaterial.PAPER).name(ChatUtils.colorize("&7Team")).build();
        }
        List<String> lore = new ArrayList<>();
        if (team.getLeader() != null) {
            lore.add(ChatUtils.colorize("&7Leader: &f" + team.getLeader().getName()));
        }
        lore.add(ChatUtils.colorize("&7Members: &f" + (team.getMembers() != null ? team.getMembers().size() : 0)));
        return new ItemBuilder(XMaterial.PLAYER_HEAD)
            .name(ChatUtils.colorize("&e" + (team.getLeader() != null ? team.getLeader().getName() : "Team")))
            .lore(lore)
            .build();
    }
}