package dev.artixdev.practice.models;

import java.io.File;
import java.util.UUID;
import dev.artixdev.practice.utils.cuboid.Cuboid;

/**
 * Simple Area model used by AreaManager. Holds an id and a cuboid region.
 */
public class Area {
    private final UUID id;
    private final Cuboid cuboid;

    public Area(UUID id, Cuboid cuboid) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.cuboid = cuboid;
    }

    public UUID getId() {
        return id;
    }

    public Cuboid getCuboid() {
        return cuboid;
    }

    /**
     * Persist the area to disk. Currently a stub; fill with real persistence if needed.
     */
    public void save(File file) {
        // TODO: implement real persistence
    }
}
