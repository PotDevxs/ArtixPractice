package dev.artixdev.practice.enums;

import org.bukkit.inventory.ItemStack;
import dev.artixdev.libs.com.cryptomorin.xseries.XMaterial;
import dev.artixdev.practice.configs.menus.PartyMenus;
import dev.artixdev.practice.utils.ItemBuilder;

import java.util.List;

public enum PartyActionType {
    
    SPLIT("Split", new ItemBuilder(XMaterial.DIAMOND_SWORD)
            .name(PartyMenus.PARTY_SPLIT_NAME)
            .durability((short) PartyMenus.PARTY_SPLIT_DURABILITY)
            .lore(PartyMenus.PARTY_SPLIT_LORE)
            .build(), 
            PartyMenus.PARTY_SPLIT_LORE, 
            PartyMenus.PARTY_SPLIT_SLOT),
    
    TEAM_FIGHT("Team Fight", new ItemBuilder(XMaterial.BLAZE_POWDER)
            .name(PartyMenus.PARTY_TEAM_FIGHT_NAME)
            .durability((short) PartyMenus.PARTY_TEAM_FIGHT_DURABILITY)
            .lore(PartyMenus.PARTY_TEAM_FIGHT_LORE)
            .build(), 
            PartyMenus.PARTY_TEAM_FIGHT_LORE, 
            PartyMenus.PARTY_TEAM_FIGHT_SLOT),
    
    FFA("FFA", new ItemBuilder(XMaterial.GOLDEN_AXE)
            .name(PartyMenus.PARTY_FFA_NAME)
            .durability((short) PartyMenus.PARTY_FFA_DURABILITY)
            .lore(PartyMenus.PARTY_FFA_LORE)
            .build(), 
            PartyMenus.PARTY_FFA_LORE, 
            PartyMenus.PARTY_FFA_SLOT),
    
    DUEL("Duel", new ItemBuilder(XMaterial.COMPASS)
            .name(PartyMenus.PARTY_DUEL_NAME)
            .durability((short) PartyMenus.PARTY_DUEL_DURABILITY)
            .lore(PartyMenus.PARTY_DUEL_LORE)
            .build(), 
            PartyMenus.PARTY_DUEL_LORE, 
            PartyMenus.PARTY_DUEL_SLOT);

    private final String name;
    private final ItemStack itemStack;
    private final List<String> lore;
    private final int slot;

    PartyActionType(String name, ItemStack itemStack, List<String> lore, int slot) {
        this.name = name;
        this.itemStack = itemStack;
        this.lore = lore;
        this.slot = slot;
    }

    public String getName() {
        return name;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public List<String> getLore() {
        return lore;
    }

    public int getSlot() {
        return slot;
    }
}
