package dev.artixdev.practice.menus.buttons;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import dev.artixdev.api.practice.menu.Button;
import dev.artixdev.practice.models.PlayerProfile;

public class TeleportButton extends Button {
    private final PlayerProfile targetProfile;

    public TeleportButton(PlayerProfile targetProfile) {
        this.targetProfile = targetProfile;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        // Implementation for teleport button item
        // This would typically show a teleport icon with player name
        return null; // Placeholder
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
