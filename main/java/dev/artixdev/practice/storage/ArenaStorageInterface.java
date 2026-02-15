package dev.artixdev.practice.storage;

import dev.artixdev.practice.models.Arena;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ArenaStorageInterface {

    /**
     * Loads arenas from a file
     * @param file The file to load arenas from
     * @return List of loaded arenas
     * @throws IOException If there's an error reading the file
     */
    List<Arena> loadArenas(File file) throws IOException;

    /**
     * Saves arenas to storage
     * @param arenas List of arenas to save
     */
    void saveArenas(List<Arena> arenas);

    /**
     * Saves a single arena
     * @param arena The arena to save
     * @param id The ID of the arena
     */
    void saveArena(Arena arena, int id);
}
