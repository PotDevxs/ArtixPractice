package dev.artixdev.practice.interfaces;

import org.bukkit.entity.Projectile;
import org.bukkit.inventory.ItemStack;

public interface ProjectileHandler {
    
    boolean isAsync();
    
    ItemStack getProjectileItem();
    
    int getProjectileSlot();
    
    boolean canHandle(Projectile projectile);

    boolean handleProjectile(Projectile projectile);
}
