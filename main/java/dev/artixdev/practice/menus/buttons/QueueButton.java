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

public class QueueButton extends Button {
    private final boolean enabled;
    private final PlayerProfile playerProfile;

    public QueueButton(PlayerProfile playerProfile, boolean enabled) {
        this.playerProfile = playerProfile;
        this.enabled = enabled;
    }

    @Override
    public ItemStack getButtonItem(Player player) {
        return createQueueItem(enabled);
    }

    @Override
    public void clicked(Player player, ClickType clickType) {
        if (enabled) {
            player.sendMessage(ChatUtils.colorize("&7Use the queue menu to join a queue."));
        } else {
            dev.artixdev.practice.Main.getInstance().getQueueManager().removePlayerFromQueue(player);
            player.sendMessage(ChatUtils.colorize("&cLeft the queue."));
        }
    }

    private ItemStack createQueueItem(boolean enabled) {
        if (enabled) {
            return new ItemBuilder(XMaterial.LIME_DYE).name(ChatUtils.colorize("&aIn Queue"))
                .lore(Collections.singletonList(ChatUtils.colorize("&7Click to leave queue"))).build();
        }
        return new ItemBuilder(XMaterial.GRAY_DYE).name(ChatUtils.colorize("&7Not in Queue"))
            .lore(Collections.singletonList(ChatUtils.colorize("&7Use queue menu to join"))).build();
    }
}