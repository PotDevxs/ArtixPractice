package dev.artixdev.practice.listeners;

import java.util.UUID;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import dev.artixdev.practice.events.PlayerJoinEvent;
import dev.artixdev.practice.events.PlayerLeaveEvent;
import dev.artixdev.practice.events.PlayerDataLoadEvent;
import dev.artixdev.practice.managers.PlayerManager;
import dev.artixdev.practice.nametag.NameTagManager;
import dev.artixdev.practice.models.PlayerProfile;

public class PlayerEventListener implements Listener {

    private final PlayerManager playerManager;
    private final NameTagManager nameTagManager;

    public PlayerEventListener(PlayerManager playerManager, NameTagManager nameTagManager) {
        this.playerManager = playerManager;
        this.nameTagManager = nameTagManager;
    }

    @EventHandler(
        priority = EventPriority.HIGHEST
    )
    public void onPlayerJoin(PlayerJoinEvent event) {
        PlayerProfile profile = event.getPlayerProfile();
        if (profile == null) {
            return;
        }
        UUID playerId = profile.getUniqueId();
        
        try {
            // Atualiza o nametag do jogador com base no perfil carregado
            // O próprio NameTagManager já busca o Player e o PlayerProfile internamente.
            nameTagManager.updatePlayerNameTag(event.getPlayerProfile().getBukkitPlayer());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

   @EventHandler(
        priority = EventPriority.LOW
    )
    public void onPlayerQuit(PlayerLeaveEvent event) {
        // Handle player quit logic
    }

    @EventHandler(
        priority = EventPriority.LOW
    )
    public void onPlayerDataLoad(PlayerDataLoadEvent event) {
        // Handle player data load logic
    }
}
