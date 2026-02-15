package dev.artixdev.practice.managers;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.PluginManager;
import dev.artixdev.libs.it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import dev.artixdev.practice.Main;
import dev.artixdev.practice.models.Area;
import dev.artixdev.practice.utils.cuboid.Cuboid;

public class AreaManager {
    
    private static final Logger LOGGER = LogManager.getLogger(AreaManager.class);
    private final Set<Area> areas;

    public AreaManager(Main plugin) {
        this.areas = new ObjectOpenHashSet<>();
        
        // Register event listeners
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new AreaEventListener(this), plugin);
        pluginManager.registerEvents(new AreaBlockListener(this), plugin);
        pluginManager.registerEvents(new AreaEntityListener(this), plugin);
    }

    public Area getArea(Entity entity) {
        Iterator<Area> iterator = this.areas.iterator();
        
        while (iterator.hasNext()) {
            Area area = iterator.next();
            if (area.getCuboid().contains(entity.getLocation())) {
                return area;
            }
        }
        
        return null;
    }

    public void forEachArea(Location location, Consumer<Area> consumer) {
        Iterator<Area> iterator = this.areas.iterator();
        
        while (iterator.hasNext()) {
            Area area = iterator.next();
            Cuboid cuboid = area.getCuboid();
            if (cuboid.contains(location)) {
                consumer.accept(area);
            }
        }
    }

    public void forEachArea(Entity entity, Consumer<Area> consumer) {
        Iterator<Area> iterator = this.areas.iterator();
        
        while (iterator.hasNext()) {
            Area area = iterator.next();
            Cuboid cuboid = area.getCuboid();
            if (cuboid.contains(entity.getLocation())) {
                consumer.accept(area);
            }
        }
    }

    public Set<Area> getAllAreas() {
        return this.areas;
    }

    public void removeArea(Area area, File file) {
        this.areas.remove(area);
        area.save(file);
    }

    public Area createArea(Cuboid cuboid) {
        // This method was obfuscated and its exact logic needs to be inferred or provided.
        // It seems to create a new area with the given cuboid.
        // For now, it's a placeholder.
        return null;
    }

    public Area getArea(UUID areaId) {
        // This method was obfuscated and its exact logic needs to be inferred or provided.
        // It seems to get an area by its UUID.
        // For now, it's a placeholder.
        return null;
    }

    public void addArea(Area area) {
        this.areas.add(area);
    }

    public void removeArea(Area area) {
        this.areas.remove(area);
    }

    public boolean containsArea(Area area) {
        return this.areas.contains(area);
    }

    public int getAreaCount() {
        return this.areas.size();
    }

    public void clearAreas() {
        this.areas.clear();
    }
}
