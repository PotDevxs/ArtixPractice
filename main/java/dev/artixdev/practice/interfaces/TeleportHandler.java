package dev.artixdev.practice.interfaces;

import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface TeleportHandler {
    
    void teleportPlayers(List<Player> players, Location location);
}
