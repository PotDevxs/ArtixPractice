package me.drizzy.api.chunk.impl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.util.Map;
import me.drizzy.api.chunk.IChunkAdapter;
// NMS imports - These classes are provided by the Minecraft server at runtime
// IDE errors are expected and can be safely ignored
import net.minecraft.server.v1_8_R3.ChunkSection;
import net.minecraft.server.v1_8_R3.NibbleArray;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.util.LongHash;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import dev.artixdev.practice.utils.ReflectionUtils;

/**
 * Chunk adapter implementation for Minecraft version 1.8 R3.
 * Handles caching and restoring chunk sections using NMS reflection.
 */
public class v1_8_R3ChunkAdapter implements IChunkAdapter {
    
    private final Map<Long, ChunkSection[]> chunkSectionMap = new Long2ObjectOpenHashMap<>();
    
    // Method handles for accessing ChunkSection fields via reflection
    private static MethodHandle nonEmptyBlockCountSetter;
    private static MethodHandle nonEmptyBlockCountGetter;
    private static MethodHandle tickingBlockCountSetter;
    private static MethodHandle tickingBlockCountGetter;
    private static MethodHandle blockIdsSetter;
    private static MethodHandle sectionsSetter;

    @Override
    public void cacheChunk(Chunk chunk) {
        ensureChunkLoaded(chunk);
        
        net.minecraft.server.v1_8_R3.Chunk nmsChunk = ((CraftChunk) chunk).getHandle();
        long key = LongHash.toLong(nmsChunk.locX, nmsChunk.locZ);
        chunkSectionMap.put(key, cloneSections(nmsChunk.getSections()));
    }

    @Override
    public void restoreChunk(Chunk chunk) {
        ensureChunkLoaded(chunk);
        
        net.minecraft.server.v1_8_R3.Chunk nmsChunk = ((CraftChunk) chunk).getHandle();
        long key = LongHash.toLong(nmsChunk.locX, nmsChunk.locZ);
        
        ChunkSection[] cachedSections = chunkSectionMap.get(key);
        if (cachedSections == null) {
            throw new UnsupportedOperationException("Chunk invoked but not saved!");
        }
        
        try {
            sectionsSetter.invoke(nmsChunk, cachedSections);
            CraftWorld world = ((CraftChunk) chunk).getCraftWorld();
            world.refreshChunk(nmsChunk.locX, nmsChunk.locZ);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to restore chunk", e);
        }
    }

    /**
     * Clones an array of chunk sections.
     */
    private ChunkSection[] cloneSections(ChunkSection[] sections) {
        ChunkSection[] clonedSections = new ChunkSection[sections.length];
        
        for (int i = 0; i < sections.length; i++) {
            if (sections[i] != null) {
                clonedSections[i] = cloneSection(sections[i]);
            }
        }
        
        return clonedSections;
    }

    /**
     * Creates a deep copy of a chunk section.
     */
    private ChunkSection cloneSection(ChunkSection original) {
        try {
            boolean hasSkyLight = original.getSkyLightArray() != null;
            ChunkSection cloned = new ChunkSection(original.getYPosition(), hasSkyLight);
            
            // Copy block counts
            nonEmptyBlockCountSetter.invoke(cloned, nonEmptyBlockCountGetter.invoke(original));
            tickingBlockCountSetter.invoke(cloned, tickingBlockCountGetter.invoke(original));
            
            // Copy block IDs
            char[] blockIds = (char[]) original.getIdArray().clone();
            blockIdsSetter.invoke(cloned, blockIds);
            
            // Copy light arrays if present
            NibbleArray emittedLight = original.getEmittedLightArray();
            if (emittedLight != null) {
                cloned.a(cloneNibbleArray(emittedLight));
            }
            
            NibbleArray skyLight = original.getSkyLightArray();
            if (skyLight != null) {
                cloned.b(cloneNibbleArray(skyLight));
            }
            
            return cloned;
        } catch (Throwable e) {
            throw new RuntimeException("Failed to clone chunk section", e);
        }
    }

    /**
     * Creates a deep copy of a nibble array.
     */
    private NibbleArray cloneNibbleArray(NibbleArray original) {
        return new NibbleArray((byte[]) original.a().clone());
    }

    /**
     * Ensures the chunk is loaded before performing operations.
     */
    private void ensureChunkLoaded(Chunk chunk) {
        if (!chunk.isLoaded()) {
            chunk.load();
        }
    }

    /**
     * Initializes method handles for field access via reflection.
     */
    static {
        Lookup lookup = MethodHandles.lookup();

        try {
            // Initialize ChunkSection field accessors
            initializeFieldAccessor(lookup, ChunkSection.class, "nonEmptyBlockCount", 
                    (getter, setter) -> {
                        nonEmptyBlockCountGetter = getter;
                        nonEmptyBlockCountSetter = setter;
                    });
            
            initializeFieldAccessor(lookup, ChunkSection.class, "tickingBlockCount", 
                    (getter, setter) -> {
                        tickingBlockCountGetter = getter;
                        tickingBlockCountSetter = setter;
                    });
            
            initializeFieldAccessor(lookup, ChunkSection.class, "blockIds", 
                    (getter, setter) -> blockIdsSetter = setter);
            
            // Initialize Chunk sections field setter
            Field sectionsField = net.minecraft.server.v1_8_R3.Chunk.class.getDeclaredField("sections");
            ReflectionUtils.makeFieldAccessible(sectionsField);
            sectionsSetter = lookup.unreflectSetter(sectionsField);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to initialize chunk adapter", e);
        }
    }

    /**
     * Helper method to initialize field accessors.
     */
    private static void initializeFieldAccessor(Lookup lookup, Class<?> clazz, String fieldName, 
                                                FieldAccessorCallback callback) throws Throwable {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        MethodHandle getter = lookup.unreflectGetter(field);
        MethodHandle setter = lookup.unreflectSetter(field);
        callback.onInitialized(getter, setter);
    }

    /**
     * Callback interface for field accessor initialization.
     */
    @FunctionalInterface
    private interface FieldAccessorCallback {
        void onInitialized(MethodHandle getter, MethodHandle setter) throws Throwable;
    }
}
