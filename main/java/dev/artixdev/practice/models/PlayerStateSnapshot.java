package dev.artixdev.practice.models;

import java.util.Arrays;
import java.util.Collection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class PlayerStateSnapshot {
    
    private int ping;
    private final double health;
    private final Collection<PotionEffect> potionEffects;
    private final int foodLevel;
    private final ItemStack[] armorContents;
    private final ItemStack[] inventoryContents;

    public PlayerStateSnapshot(double health, int foodLevel, ItemStack[] armorContents, ItemStack[] inventoryContents, Collection<PotionEffect> potionEffects) {
        this.health = health;
        this.foodLevel = foodLevel;
        this.armorContents = armorContents;
        this.inventoryContents = inventoryContents;
        this.potionEffects = potionEffects;
    }

    public ItemStack[] getArmorContents() {
        return this.armorContents;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        
        if (!(obj instanceof PlayerStateSnapshot)) {
            return false;
        }
        
        PlayerStateSnapshot other = (PlayerStateSnapshot) obj;
        if (!other.canEqual(this)) {
            return false;
        }
        
        return Double.compare(this.health, other.health) == 0 &&
               this.foodLevel == other.foodLevel &&
               this.ping == other.ping &&
               Arrays.deepEquals(this.armorContents, other.armorContents) &&
               Arrays.deepEquals(this.inventoryContents, other.inventoryContents) &&
               (this.potionEffects == null ? other.potionEffects == null : this.potionEffects.equals(other.potionEffects));
    }

    public String toString() {
        return "PlayerStateSnapshot{" +
                "health=" + health +
                ", foodLevel=" + foodLevel +
                ", ping=" + ping +
                ", armorContents=" + Arrays.toString(armorContents) +
                ", inventoryContents=" + Arrays.toString(inventoryContents) +
                ", potionEffects=" + potionEffects +
                '}';
    }

    public double getHealth() {
        return this.health;
    }

    public Collection<PotionEffect> getPotionEffects() {
        return this.potionEffects;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public int getFoodLevel() {
        return this.foodLevel;
    }

    public ItemStack[] getInventoryContents() {
        return this.inventoryContents;
    }

    public int getPing() {
        return this.ping;
    }

    protected boolean canEqual(Object obj) {
        return obj instanceof PlayerStateSnapshot;
    }

    @Override
    public int hashCode() {
        int result = 1;
        long temp = Double.doubleToLongBits(this.health);
        result = result * 59 + (int) (temp ^ (temp >>> 32));
        result = result * 59 + this.foodLevel;
        result = result * 59 + this.ping;
        result = result * 59 + Arrays.deepHashCode(this.armorContents);
        result = result * 59 + Arrays.deepHashCode(this.inventoryContents);
        result = result * 59 + (this.potionEffects == null ? 43 : this.potionEffects.hashCode());
        return result;
    }
}
