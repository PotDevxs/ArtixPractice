package dev.artixdev.practice.managers;

import org.bukkit.entity.Projectile;
import dev.artixdev.practice.interfaces.ProjectileHandler;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class ProjectileManager implements Runnable {
    
    private final Map<Projectile, ProjectileHandler> projectileHandlers;

    public ProjectileManager() {
        this.projectileHandlers = new ConcurrentHashMap<>();
    }

    @Override
    public void run() {
        Set<Entry<Projectile, ProjectileHandler>> entries = projectileHandlers.entrySet();
        
        Iterator<Entry<Projectile, ProjectileHandler>> iterator = entries.iterator();
        
        while (iterator.hasNext()) {
            Entry<Projectile, ProjectileHandler> entry = iterator.next();
            
            try {
                Projectile projectile = entry.getKey();
                ProjectileHandler handler = entry.getValue();
                
                if (!handler.handleProjectile(projectile)) {
                    iterator.remove();
                }
            } catch (Exception e) {
                iterator.remove();
            }
        }
    }

    public void addProjectile(Projectile projectile, ProjectileHandler handler) {
        projectileHandlers.put(projectile, handler);
    }

    public void removeProjectile(Projectile projectile) {
        projectileHandlers.remove(projectile);
    }

    public boolean hasProjectile(Projectile projectile) {
        return projectileHandlers.containsKey(projectile);
    }

    public ProjectileHandler getHandler(Projectile projectile) {
        return projectileHandlers.get(projectile);
    }

    public void clearProjectiles() {
        projectileHandlers.clear();
    }

    public int getProjectileCount() {
        return projectileHandlers.size();
    }
}
