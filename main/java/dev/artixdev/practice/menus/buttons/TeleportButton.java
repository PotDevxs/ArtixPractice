package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.practice.models.PlayerProfile;
import dev.artixdev.practice.utils.ChatUtils;
import dev.artixdev.practice.utils.ItemBuilder;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;

import java.util.Collections;

public class TeleportButton extends Button {
    private final PlayerProfile targetProfile;

    public TeleportButton(PlayerProfile targetProfile) {
        this.targetProfile = targetProfile;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        String name = targetProfile != null && targetProfile.getUsername() != null ? targetProfile.getUsername() : "Unknown";
        return new ItemBuilder(XMaterial.ENDER_PEARL).name(ChatUtils.colorize("&eTeleport to &f" + name))
            .lore(Collections.singletonList(ChatUtils.colorize("&7Click to teleport"))).build();
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        Player targetPlayer = targetProfile.getBukkitPlayer();
        if (targetPlayer != null && targetPlayer.isOnline()) {
            player.closeInventory();
            player.teleport(targetPlayer.getLocation());
        }
    }

    public PlayerProfile getTargetProfile() {
        return targetProfile;
    }
}
