package dev.artixdev.practice.listeners;

import java.util.List;
import java.util.UUID;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.PacketListenerPriority;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.SimplePacketListenerAbstract;
import dev.artixdev.libs.com.github.retrooper.packetevents.event.simple.PacketPlayReceiveEvent;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectArrayList;

public class PacketListener extends SimplePacketListenerAbstract {
    private final List<UUID> trackedPlayers;

    public PacketListener() {
        super(PacketListenerPriority.HIGHEST);
        this.trackedPlayers = new ObjectArrayList<>();
    }

    public void removePlayer(UUID playerId) {
        this.trackedPlayers.remove(playerId);
    }

    public boolean isPlayerTracked(UUID playerId) {
        return this.trackedPlayers.contains(playerId);
    }

    @Override
    public void onPacketPlayReceive(PacketPlayReceiveEvent event) {
        // Extend to process packets (e.g. combat, movement) for practice matches
    }
}
