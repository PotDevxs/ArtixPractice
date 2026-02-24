package dev.artixdev.practice.models;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import dev.artixdev.practice.utils.cuboid.Cuboid;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Simple Area model used by AreaManager. Holds an id and a cuboid region.
 */
public class Area {
    public enum Type { LOBBY, ARENA, FFA }

    private final UUID id;
    private final Cuboid cuboid;
    private Type type = Type.LOBBY;

    public Area(UUID id, Cuboid cuboid) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.cuboid = cuboid;
    }

    public Area(UUID id, Cuboid cuboid, Type type) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.cuboid = cuboid;
        this.type = type != null ? type : Type.LOBBY;
    }

    public UUID getId() {
        return id;
    }

    public Cuboid getCuboid() {
        return cuboid;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type != null ? type : Type.LOBBY;
    }

    public boolean isFFA() {
        return type == Type.FFA;
    }

    /**
     * Persist the area to disk as a simple properties-style file (id, type, world, min/max coords).
     */
    public void save(File file) {
        if (file == null || cuboid == null) return;
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();
            StringBuilder sb = new StringBuilder();
            sb.append("id=").append(id.toString()).append("\n");
            sb.append("type=").append(type.name()).append("\n");
            Location min = cuboid.getLowerCorner();
            Location max = cuboid.getUpperCorner();
            if (min != null && min.getWorld() != null) {
                sb.append("world=").append(min.getWorld().getName()).append("\n");
                sb.append("minX=").append(min.getBlockX()).append("\n");
                sb.append("minY=").append(min.getBlockY()).append("\n");
                sb.append("minZ=").append(min.getBlockZ()).append("\n");
            }
            if (max != null && max.getWorld() != null) {
                sb.append("maxX=").append(max.getBlockX()).append("\n");
                sb.append("maxY=").append(max.getBlockY()).append("\n");
                sb.append("maxZ=").append(max.getBlockZ()).append("\n");
            }
            Files.write(file.toPath(), sb.toString().getBytes(StandardCharsets.UTF_8),
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException ignored) {
        }
    }

    /**
     * Load an area from a file saved by {@link #save(File)}. Returns null if file missing or invalid.
     */
    public static Area load(File file) {
        if (file == null || !file.exists()) return null;
        try {
            java.util.Properties p = new java.util.Properties();
            try (java.io.Reader r = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
                p.load(r);
            }
            String idStr = p.getProperty("id");
            UUID areaId = idStr != null ? UUID.fromString(idStr.trim()) : UUID.randomUUID();
            Type t = Type.LOBBY;
            String typeStr = p.getProperty("type");
            if (typeStr != null) {
                try {
                    t = Type.valueOf(typeStr.trim().toUpperCase());
                } catch (IllegalArgumentException ignored) { }
            }
            String worldName = p.getProperty("world");
            World w = worldName != null ? Bukkit.getWorld(worldName) : null;
            if (w == null) return null;
            int minX = Integer.parseInt(p.getProperty("minX", "0"));
            int minY = Integer.parseInt(p.getProperty("minY", "0"));
            int minZ = Integer.parseInt(p.getProperty("minZ", "0"));
            int maxX = Integer.parseInt(p.getProperty("maxX", "0"));
            int maxY = Integer.parseInt(p.getProperty("maxY", "0"));
            int maxZ = Integer.parseInt(p.getProperty("maxZ", "0"));
            Location min = new Location(w, minX, minY, minZ);
            Location max = new Location(w, maxX, maxY, maxZ);
            Cuboid c = new Cuboid(min, max);
            return new Area(areaId, c, t);
        } catch (Exception e) {
            return null;
        }
    }
}
