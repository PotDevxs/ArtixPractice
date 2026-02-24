package dev.artixdev.practice.tasks;

import org.bukkit.Material;
import org.bukkit.block.Block;
import dev.artixdev.practice.utils.cuboid.Cuboid;
import dev.artixdev.practice.Main;

import java.util.List;

public class CuboidResetTask implements Runnable {

    private final List<Cuboid> cuboids;
    private final Runnable callback;

    public CuboidResetTask(List<Cuboid> cuboids, Runnable callback) {
        this.cuboids = cuboids;
        this.callback = callback;
    }

    @Override
    public void run() {
        Main plugin = Main.getInstance();
        plugin.getServer().getScheduler().runTask(plugin, () -> {
            for (Cuboid cuboid : cuboids) {
                try {
                    for (Block block : cuboid) {
                        if (block != null && block.getType() != Material.AIR) {
                            block.setType(Material.AIR);
                        }
                    }
                } catch (Exception e) {
                    plugin.getLogger().warning("CuboidResetTask: failed to reset cuboid - " + e.getMessage());
                }
            }
            callback.run();
        });
    }
}
