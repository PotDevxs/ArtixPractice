package dev.artixdev.practice.menus.buttons;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.libs.com.cryptomorin.xseries.SkullUtils;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.configs.menus.PartyMenus;
import dev.artixdev.practice.utils.ItemBuilder;

public class PartyPlayerButton extends Button {
    public static final int BUTTON_VERSION = 1;
    private static final String[] BUTTON_CONSTANTS = new String[1];
    private final Player targetPlayer;
    private static final String[] BUTTON_MESSAGES = new String[1];
    public static final boolean DEBUG_MODE = false;

    public PartyPlayerButton(Player targetPlayer) {
        this.targetPlayer = targetPlayer;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        ItemBuilder itemBuilder = new ItemBuilder(XMaterial.PLAYER_HEAD);
        itemBuilder.name(PartyMenus.PARTY_MANAGE_NAME.replace("%player%", this.targetPlayer.getName()));
        itemBuilder.lore(PartyMenus.PARTY_MANAGE_LORE);
        itemBuilder.durability((short) PartyMenus.PARTY_MANAGE_DURABILITY);
        
        ItemStack item = itemBuilder.build();
        
        if (item.hasItemMeta() && item.getItemMeta() instanceof SkullMeta) {
            ItemMeta meta = item.getItemMeta();
            SkullMeta skullMeta = SkullUtils.applySkin(meta, (OfflinePlayer) this.targetPlayer);
            item.setItemMeta(skullMeta);
        }
        
        return item;
    }

    @Override
    public boolean shouldUpdate(Player player, ClickType clickType) {
        return true;
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        dev.artixdev.practice.Main main = dev.artixdev.practice.Main.getInstance();
        dev.artixdev.practice.managers.PartyManager pm = main != null ? main.getPartyManager() : null;
        dev.artixdev.practice.models.Team team = pm != null ? pm.getPlayerParty(player.getUniqueId()) : null;

        if (clickType.isLeftClick()) {
            if (team != null && pm.sendInvitation(team, targetPlayer)) {
                Button.playSuccess(player);
            }
            player.closeInventory();
        } else if (clickType.isRightClick()) {
            if (team != null && team.isLeader(player.getUniqueId()) && team.hasMember(targetPlayer.getUniqueId())) {
                pm.removePlayerFromParty(targetPlayer.getUniqueId());
                player.sendMessage(dev.artixdev.practice.utils.ChatUtils.translate("&aMember removed from the party."));
                Button.playSuccess(player);
            }
            player.closeInventory();
        }
    }

    static {
        BUTTON_CONSTANTS[0] = "PartyPlayerButton";
        BUTTON_MESSAGES[0] = "Party Player";
    }
}
