package dev.artixdev.practice.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import dev.artixdev.practice.models.Match;

import java.util.List;

public class InventoryHandler implements InventoryHandlerInterface {
    private int chestplateSlot = -1;
    private ItemStack boots;
    public static final boolean DEBUG_MODE = false;
    public static final int HANDLER_VERSION = 1;
    private int leggingsSlot;

    private void updateInventory(PlayerInventory inventory) {
        if (inventory == null) {
            return;
        }
        if (boots != null) {
            inventory.setBoots(boots.clone());
        }
        int slot = (chestplateSlot >= 0 && chestplateSlot <= 8) ? chestplateSlot
            : (leggingsSlot >= 0 && leggingsSlot <= 8) ? leggingsSlot : -1;
        if (slot >= 0) {
            inventory.setHeldItemSlot(slot);
        }
        if (inventory.getHolder() instanceof org.bukkit.entity.Player) {
            ((org.bukkit.entity.Player) inventory.getHolder()).updateInventory();
        }
    }

    public InventoryHandler() {
        this.leggingsSlot = -1;
    }

    @Override
    public boolean validateInventory(Match match) {
        if (match == null) {
            return false;
        }
        if (match.isEnded()) {
            return false;
        }
        List<Player> players = match.getPlayers();
        if (players == null || players.isEmpty()) {
            return false;
        }
        for (Player player : players) {
            if (player == null || !player.isOnline()) {
                return false;
            }
            if (player.getInventory() == null) {
                return false;
            }
        }
        return true;
    }

    private void updateInventory(Match match, PlayerInventory inventory, int slot, boolean flag) {
        int adjustedSlot = slot;
        
        if (adjustedSlot > 8) {
            // Adjust slot to valid range
            adjustedSlot = 8;
        }

        inventory.setHeldItemSlot(adjustedSlot);
        if (inventory.getHolder() instanceof org.bukkit.entity.Player) {
            ((org.bukkit.entity.Player) inventory.getHolder()).updateInventory();
        }
    }
}
