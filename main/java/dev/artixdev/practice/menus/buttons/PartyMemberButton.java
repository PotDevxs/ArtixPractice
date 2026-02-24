package dev.artixdev.practice.menus.buttons;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.Team;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.libs.com.cryptomorin.xseries.SkullUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PartyMemberButton extends Button {
    public static final boolean DEBUG_MODE = false;
    private final UUID memberUuid;
    private static final String[] BUTTON_CONSTANTS = {"PartyMemberButton"};
    private final Team team;
    private static final String[] BUTTON_MESSAGES = new String[4];
    public static final int BUTTON_VERSION = 1;

    static {
        BUTTON_MESSAGES[0] = "Party Member";
        BUTTON_MESSAGES[1] = "Click to manage";
        BUTTON_MESSAGES[2] = "Right-click to kick";
        BUTTON_MESSAGES[3] = "Left-click to promote";
    }

    public PartyMemberButton(UUID memberUuid, Team team) {
        super();
        this.memberUuid = memberUuid;
        this.team = team;
    }

    @Override
    public boolean shouldUpdate(Player player, ClickType clickType) {
        return true;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (team == null || !team.isLeader(player.getUniqueId())) {
            player.sendMessage(ChatUtils.translate("&cOnly the party leader can manage members."));
            return;
        }
        if (memberUuid.equals(player.getUniqueId())) {
            player.sendMessage(ChatUtils.translate("&cYou cannot manage yourself here."));
            return;
        }
        if (clickType == ClickType.RIGHT || clickType == ClickType.SHIFT_RIGHT) {
            Main.getInstance().getPartyManager().removePlayerFromParty(memberUuid);
            player.sendMessage(ChatUtils.translate("&aMember removed from the party."));
            player.closeInventory();
        } else if (clickType == ClickType.LEFT || clickType == ClickType.SHIFT_LEFT) {
            player.sendMessage(ChatUtils.translate("&7Promote to leader is not available."));
        }
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        OfflinePlayer offline = Bukkit.getOfflinePlayer(memberUuid);
        String name = offline.getName() != null ? offline.getName() : memberUuid.toString().substring(0, 8);
        ItemStack skull = XMaterial.PLAYER_HEAD.parseItem();
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(ChatUtils.translate("&e" + name));
            SkullUtils.applySkin(meta, offline);
            List<String> lore = new ArrayList<>();
            lore.add(ChatUtils.translate("&7" + BUTTON_MESSAGES[0]));
            lore.add("");
            lore.add(ChatUtils.translate("&a" + BUTTON_MESSAGES[1]));
            lore.add(ChatUtils.translate("&c" + BUTTON_MESSAGES[2]));
            lore.add(ChatUtils.translate("&e" + BUTTON_MESSAGES[3]));
            meta.setLore(lore);
            skull.setItemMeta(meta);
        }
        return skull;
    }
}
