package dev.artixdev.practice.expansions.hologram;

import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.HologramData;

public class LeaderboardHologram extends AbstractHologram {

    private final HologramData hologramData;

    public LeaderboardHologram(Main plugin, HologramData hologramData) {
        super(plugin, hologramData != null && hologramData.getLocation() != null ? hologramData.getLocation() : null);
        this.hologramData = hologramData;
    }

    @Override
    public void tick() {
        updateHolograms();
    }

    private void updateHolograms() {
        updateHologram();
    }

    public void updateHologram() {
        // Update leaderboard hologram with current statistics
    }

    public boolean shouldUpdate() {
        return true;
    }

    public void onUpdate() {
        // Called when hologram is updated
    }
}
