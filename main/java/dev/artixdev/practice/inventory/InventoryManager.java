package dev.artixdev.practice.inventory;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import dev.artixdev.practice.Practice;
import dev.artixdev.practice.models.Match;

import java.util.List;

public class InventoryManager implements InventoryManagerInterface {
    private ItemStack helmet;
    private int chestplateSlot;
    private ItemStack boots;
    public static final boolean DEBUG_MODE = false;
    private static final String[] INVENTORY_CONSTANTS = {"InventoryManager"};
    public static final int MANAGER_VERSION = 1;
    private ItemStack leggings;
    private final Match match;
    private static final String[] INVENTORY_MESSAGES = new String[1];
    private int leggingsSlot;
    private int helmetSlot;

    private static String getInventoryMessage(int param0, int param1) {
        String base = (param0 >= 0 && param0 < INVENTORY_CONSTANTS.length)
            ? INVENTORY_CONSTANTS[param0]
            : INVENTORY_CONSTANTS[0];
        return base + " [" + param0 + "," + param1 + "]";
    }

    private void updateInventory(PlayerInventory inventory) {
        if (inventory == null) {
            return;
        }
        if (helmet != null) {
            inventory.setHelmet(helmet.clone());
        }
        if (leggings != null) {
            inventory.setLeggings(leggings.clone());
        }
        if (boots != null) {
            inventory.setBoots(boots.clone());
        }
        if (chestplateSlot >= 0 && chestplateSlot <= 8) {
            inventory.setHeldItemSlot(chestplateSlot);
        }
        if (inventory.getHolder() instanceof org.bukkit.entity.Player) {
            ((org.bukkit.entity.Player) inventory.getHolder()).updateInventory();
        }
    }

    public InventoryManager() {
        this.chestplateSlot = -1;
        this.leggingsSlot = -1;
        this.helmetSlot = -1;
        this.match = Practice.getPlugin().getMatchManager().getCurrentMatch();
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

    public ItemStack getHelmet() {
        return this.helmet;
    }

    public Match getMatch() {
        return this.match;
    }

    private void updateInventory(Match match, PlayerInventory inventory, int slot) {
        if (match == null || match.isEnded() || inventory == null) {
            return;
        }
        int clampedSlot = slot;
        if (clampedSlot < 0) {
            clampedSlot = 0;
        } else if (clampedSlot > 8) {
            clampedSlot = 8;
        }
        inventory.setHeldItemSlot(clampedSlot);
        updateInventory(inventory);
    }

    public int getChestplateSlot() {
        return this.chestplateSlot;
    }

    public ItemStack getBoots() {
        return this.boots;
    }

    public int getHelmetSlot() {
        return this.helmetSlot;
    }

    public ItemStack getLeggings() {
        return this.leggings;
    }

    public int getLeggingsSlot() {
        return this.leggingsSlot;
    }

    static {
        INVENTORY_MESSAGES[0] = "InventoryManager";
    }
}
