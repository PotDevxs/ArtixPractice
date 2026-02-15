package dev.artixdev.practice.inventory;

import dev.artixdev.practice.models.Match;

/**
 * Interface for inventory management during matches.
 */
public interface InventoryManagerInterface {

    /**
     * Validates the inventory state for the given match.
     *
     * @param match the match to validate
     * @return true if the inventory is valid, false otherwise
     */
    boolean validateInventory(Match match);
}
