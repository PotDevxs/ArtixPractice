package dev.artixdev.practice.models;

import org.bukkit.inventory.ItemStack;

/**
 * Simple data holder for item-related information used by {@link dev.artixdev.practice.interfaces.ItemInterface}.
 */
public class ItemData {

    private String name;
    private short durability;
    private ItemStack itemStack;

    public ItemData() {
    }

    public ItemData(String name, short durability) {
        this.name = name;
        this.durability = durability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getDurability() {
        return durability;
    }

    public void setDurability(short durability) {
        this.durability = durability;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
