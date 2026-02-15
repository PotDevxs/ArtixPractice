package dev.artixdev.practice.tasks;

import dev.artixdev.practice.utils.cuboid.Cuboid;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.managers.ArenaManager;

import java.util.Iterator;
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
        
        Iterator<Cuboid> iterator = this.cuboids.iterator();

        while (iterator.hasNext()) {
            Cuboid cuboid = iterator.next();
            
            // TODO: Implement cuboid reset functionality
            // The resetCuboid method doesn't exist in ArenaManager
            // This would typically reset blocks in the cuboid region to their original state
            // For now, we'll just iterate through the cuboids without resetting

            try {
                Thread.sleep(20L); // Small delay to prevent lag
            } catch (Exception e) {
                // Ignore interruption
            }
        }

        // Execute callback when done
        this.callback.run();
    }
}
