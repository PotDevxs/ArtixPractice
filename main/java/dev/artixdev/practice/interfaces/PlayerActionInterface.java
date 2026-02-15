package dev.artixdev.practice.interfaces;

import org.bukkit.entity.Player;

public interface PlayerActionInterface {
    int DEFAULT_PRIORITY = 0;
    boolean ENABLED = true;

    void executeAction(Player player);

    void executeAction(Player player, String action, String data, int x, int y, int z);
}
