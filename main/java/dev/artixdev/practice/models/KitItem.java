package dev.artixdev.practice.models;

import com.google.gson.annotations.SerializedName;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class KitItem {

    @SerializedName("contents")
    private ItemStack[] contents;

    @SerializedName("slot")
    private int slot;

    @SerializedName("kitName")
    private String kitName;

    @SerializedName("effects")
    private List<PotionEffect> effects;

    @SerializedName("customName")
    private String customName;

    public KitItem() {
        this.contents = new ItemStack[0];
        this.effects = new ArrayList<>();
    }

    public KitItem(Kit kit, int slot) {
        this.contents = new ItemStack[0];
        this.effects = new ArrayList<>();
        this.kitName = kit.getName().toLowerCase();
        this.contents = kit.getInventoryContents();
        this.slot = slot;
    }

    public KitItem(Kit kit, String customName, int slot) {
        this.contents = new ItemStack[0];
        this.effects = new ArrayList<>();
        this.customName = customName;
        this.kitName = kit.getName().toLowerCase();
        this.contents = kit.getInventoryContents();
        this.slot = slot;
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public void setContents(ItemStack[] contents) {
        this.contents = contents;
    }

    public int getSlot() {
        return slot;
    }

    public void setSlot(int slot) {
        this.slot = slot;
    }

    public String getKitName() {
        return kitName;
    }

    public void setKitName(String kitName) {
        this.kitName = kitName;
    }

    public String getCustomName() {
        return customName;
    }

    public void setCustomName(String customName) {
        this.customName = customName;
    }

    public List<PotionEffect> getEffects() {
        return effects;
    }

    public void setEffects(List<PotionEffect> effects) {
        this.effects = effects;
    }

    public int getTotalItems() {
        return countNonEmptyItems(contents);
    }

    public int getArmorItems() {
        return countArmorItems(contents);
    }

    public int getHotbarItems() {
        return countHotbarItems(contents);
    }

    public int getOffhandItems() {
        return countOffhandItems(contents);
    }

    private int countNonEmptyItems(ItemStack[] items) {
        int count = 0;
        for (ItemStack item : items) {
            if (item != null && item.getType() != org.bukkit.Material.AIR) {
                count++;
            }
        }
        return count;
    }

    private int countArmorItems(ItemStack[] items) {
        int count = 0;
        // Check armor slots (36-39 in player inventory)
        for (int i = 36; i <= 39; i++) {
            if (i < items.length && items[i] != null && items[i].getType() != org.bukkit.Material.AIR) {
                count++;
            }
        }
        return count;
    }

    private int countHotbarItems(ItemStack[] items) {
        int count = 0;
        // Check hotbar slots (0-8 in player inventory)
        for (int i = 0; i <= 8; i++) {
            if (i < items.length && items[i] != null && items[i].getType() != org.bukkit.Material.AIR) {
                count++;
            }
        }
        return count;
    }

    private int countOffhandItems(ItemStack[] items) {
        int count = 0;
        // Check offhand slot (40 in player inventory)
        if (items.length > 40 && items[40] != null && items[40].getType() != org.bukkit.Material.AIR) {
            count++;
        }
        return count;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof KitItem)) {
            return false;
        }
        KitItem other = (KitItem) obj;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSlot() != other.getSlot()) {
            return false;
        }
        String thisCustomName = this.getCustomName();
        String otherCustomName = other.getCustomName();
        if (thisCustomName == null) {
            if (otherCustomName != null) {
                return false;
            }
        } else if (!thisCustomName.equals(otherCustomName)) {
            return false;
        }
        if (!Arrays.deepEquals(this.getContents(), other.getContents())) {
            return false;
        }
        List<PotionEffect> thisEffects = this.getEffects();
        List<PotionEffect> otherEffects = other.getEffects();
        if (thisEffects == null) {
            if (otherEffects != null) {
                return false;
            }
        } else if (!thisEffects.equals(otherEffects)) {
            return false;
        }
        String thisKitName = this.getKitName();
        String otherKitName = other.getKitName();
        if (thisKitName == null) {
            if (otherKitName != null) {
                return false;
            }
        } else if (!thisKitName.equals(otherKitName)) {
            return false;
        }
        return true;
    }

    protected boolean canEqual(Object other) {
        return other instanceof KitItem;
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + this.getSlot();
        String customName = this.getCustomName();
        result = result * PRIME + (customName == null ? 43 : customName.hashCode());
        result = result * PRIME + Arrays.deepHashCode(this.getContents());
        List<PotionEffect> effects = this.getEffects();
        result = result * PRIME + (effects == null ? 43 : effects.hashCode());
        String kitName = this.getKitName();
        result = result * PRIME + (kitName == null ? 43 : kitName.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "KitItem(" +
                "customName=" + this.getCustomName() + ", " +
                "contents=" + Arrays.deepToString(this.getContents()) + ", " +
                "effects=" + this.getEffects() + ", " +
                "slot=" + this.getSlot() + ", " +
                "kitName=" + this.getKitName() + ")";
    }
}