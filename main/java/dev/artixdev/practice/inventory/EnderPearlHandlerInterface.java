package dev.artixdev.practice.inventory;

import dev.artixdev.practice.models.Match;

/**
 * Interface for ender pearl handling during matches.
 */
public interface EnderPearlHandlerInterface {

    /**
     * Validates the inventory state for the given match.
     *
     * @param match the match to validate
     * @return true if the inventory is valid, false otherwise
     */
    boolean validateInventory(Match match);
}
