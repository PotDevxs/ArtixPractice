package me.drizzy.api.chunk.impl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import me.drizzy.api.chunk.IChunkAdapter;
import org.bukkit.Chunk;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import dev.artixdev.practice.utils.ReflectionUtils;

/**
 * Chunk adapter implementation for Minecraft version 1.16 R3.
 * Handles caching and restoring chunk sections using NMS reflection.
 */
public class v1_16_R3ChunkAdapter implements IChunkAdapter {

    private static final String NMS = "net.minecraft.server.v1_16_R3";

    private final Map<Long, Object[]> chunkSectionMap = new Long2ObjectOpenHashMap<>();

    private static Class<?> chunkSectionClass;
    private static Class<?> chunkClass;
    private static MethodHandle nonEmptyBlockCountGetter;
    private static MethodHandle tickingBlockCountGetter;
    private static MethodHandle blocksSetter;
    private static MethodHandle fieldEGetter;
    private static MethodHandle sectionsSetter;

    @Override
    public void cacheChunk(Chunk chunk) {
        if (!chunk.isLoaded()) {
            chunk.load();
        }
        try {
            Object nmsChunk = chunk.getClass().getMethod("getHandle").invoke(chunk);
            Object pos = nmsChunk.getClass().getMethod("getPos").invoke(nmsChunk);
            long key = (Long) pos.getClass().getMethod("pair").invoke(pos);
            Object[] sections = (Object[]) nmsChunk.getClass().getMethod("getSections").invoke(nmsChunk);
            chunkSectionMap.put(key, cloneSections(sections));
        } catch (Throwable e) {
            throw new RuntimeException("Failed to cache chunk", e);
        }
    }

    @Override
    public void restoreChunk(Chunk chunk) {
        if (!chunk.isLoaded()) {
            chunk.load();
        }
        try {
            Object nmsChunk = chunk.getClass().getMethod("getHandle").invoke(chunk);
            Object pos = nmsChunk.getClass().getMethod("getPos").invoke(nmsChunk);
            long key = (Long) pos.getClass().getMethod("pair").invoke(pos);
            Object[] cachedSections = chunkSectionMap.get(key);
            if (cachedSections == null) {
                throw new UnsupportedOperationException("Chunk invoked but not saved!");
            }
            sectionsSetter.invoke(nmsChunk, cachedSections);
            Object craftWorld = chunk.getClass().getMethod("getCraftWorld").invoke(chunk);
            craftWorld.getClass().getMethod("refreshChunk", int.class, int.class).invoke(craftWorld, chunk.getX(), chunk.getZ());
        } catch (Throwable e) {
            throw new RuntimeException("Failed to restore chunk", e);
        }
    }

    private Object[] cloneSections(Object[] sections) {
        Object[] cloned = (Object[]) java.lang.reflect.Array.newInstance(chunkSectionClass, sections.length);
        for (int i = 0; i < sections.length; i++) {
            if (sections[i] != null) {
                cloned[i] = cloneSection(sections[i]);
            }
        }
        return cloned;
    }

    private Object cloneSection(Object original) {
        try {
            int yPos = (Integer) original.getClass().getMethod("getYPosition").invoke(original);
            Object nonEmpty = nonEmptyBlockCountGetter.invoke(original);
            Object ticking = tickingBlockCountGetter.invoke(original);
            Object fieldE = fieldEGetter.invoke(original);
            Constructor<?> ctor = null;
            for (Constructor<?> c : chunkSectionClass.getDeclaredConstructors()) {
                if (c.getParameterCount() == 4) {
                    ctor = c;
                    break;
                }
            }
            if (ctor == null) {
                throw new RuntimeException("ChunkSection 4-arg constructor not found");
            }
            ctor.setAccessible(true);
            Object newSection = ctor.newInstance(yPos, nonEmpty, ticking, fieldE);
            Object blocks = original.getClass().getMethod("getBlocks").invoke(original);
            blocksSetter.invoke(newSection, blocks);
            return newSection;
        } catch (Throwable e) {
            throw new RuntimeException("Failed to clone chunk section", e);
        }
    }

    static {
        Lookup lookup = MethodHandles.lookup();
        try {
            chunkSectionClass = Class.forName(NMS + ".ChunkSection");
            chunkClass = Class.forName(NMS + ".Chunk");

            Field nonEmptyBlockCount = chunkSectionClass.getDeclaredField("nonEmptyBlockCount");
            ReflectionUtils.makeFieldAccessible(nonEmptyBlockCount);
            nonEmptyBlockCountGetter = lookup.unreflectGetter(nonEmptyBlockCount);

            Field tickingBlockCount = chunkSectionClass.getDeclaredField("tickingBlockCount");
            ReflectionUtils.makeFieldAccessible(tickingBlockCount);
            tickingBlockCountGetter = lookup.unreflectGetter(tickingBlockCount);

            Field blockIds = chunkSectionClass.getDeclaredField("blockIds");
            ReflectionUtils.makeFieldAccessible(blockIds);
            blocksSetter = lookup.unreflectSetter(blockIds);

            Field fieldE = chunkSectionClass.getDeclaredField("e");
            ReflectionUtils.makeFieldAccessible(fieldE);
            fieldEGetter = lookup.unreflectGetter(fieldE);

            Field sectionsField = chunkClass.getDeclaredField("sections");
            ReflectionUtils.makeFieldAccessible(sectionsField);
            sectionsSetter = lookup.unreflectSetter(sectionsField);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to initialize chunk adapter", e);
        }
    }
}
