package me.drizzy.api.chunk.impl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import me.drizzy.api.chunk.IChunkAdapter;
import org.bukkit.Chunk;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import dev.artixdev.practice.utils.ReflectionUtils;

/**
 * Chunk adapter implementation for Minecraft version 1.12 R1.
 * Handles caching and restoring chunk sections using NMS reflection.
 */
public class v1_12_R1ChunkAdapter implements IChunkAdapter {

    private static final String NMS = "net.minecraft.server.v1_12_R1";

    private final Map<Long, Object[]> chunkSectionMap = new Long2ObjectOpenHashMap<>();

    private static Class<?> chunkSectionClass;
    private static Class<?> nibbleArrayClass;
    private static Class<?> chunkClass;
    private static MethodHandle nonEmptyBlockCountSetter;
    private static MethodHandle nonEmptyBlockCountGetter;
    private static MethodHandle tickingBlockCountSetter;
    private static MethodHandle tickingBlockCountGetter;
    private static MethodHandle blockIdsSetter;
    private static MethodHandle sectionsSetter;

    private static long toLong(int x, int z) {
        return (long) x << 32 | (z & 0xFFFFFFFFL);
    }

    @Override
    public void cacheChunk(Chunk chunk) {
        if (!chunk.isLoaded()) {
            chunk.load();
        }
        try {
            Object nmsChunk = chunk.getClass().getMethod("getHandle").invoke(chunk);
            int locX = chunkClass.getField("locX").getInt(nmsChunk);
            int locZ = chunkClass.getField("locZ").getInt(nmsChunk);
            long key = toLong(locX, locZ);
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
            int locX = chunkClass.getField("locX").getInt(nmsChunk);
            int locZ = chunkClass.getField("locZ").getInt(nmsChunk);
            long key = toLong(locX, locZ);
            Object[] cachedSections = chunkSectionMap.get(key);
            if (cachedSections == null) {
                throw new UnsupportedOperationException("Chunk invoked but not saved!");
            }
            sectionsSetter.invoke(nmsChunk, cachedSections);
            Object craftWorld = chunk.getClass().getMethod("getCraftWorld").invoke(chunk);
            craftWorld.getClass().getMethod("refreshChunk", int.class, int.class).invoke(craftWorld, locX, locZ);
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
            Method getSkyLight = original.getClass().getMethod("getSkyLightArray");
            Object skyLight = getSkyLight.invoke(original);
            boolean hasSkyLight = skyLight != null;
            Method getYPosition = original.getClass().getMethod("getYPosition");
            int yPos = (Integer) getYPosition.invoke(original);
            Object cloned = chunkSectionClass.getConstructor(int.class, boolean.class).newInstance(yPos, hasSkyLight);

            nonEmptyBlockCountSetter.invoke(cloned, nonEmptyBlockCountGetter.invoke(original));
            tickingBlockCountSetter.invoke(cloned, tickingBlockCountGetter.invoke(original));

            Method getBlocks = original.getClass().getMethod("getBlocks");
            Object blockData = getBlocks.invoke(original);
            blockIdsSetter.invoke(cloned, blockData);

            Method getEmittedLight = original.getClass().getMethod("getEmittedLightArray");
            Object emittedLight = getEmittedLight.invoke(original);
            if (emittedLight != null) {
                Method a = cloned.getClass().getMethod("a", nibbleArrayClass);
                a.invoke(cloned, cloneNibbleArray(emittedLight));
            }
            if (skyLight != null) {
                Method b = cloned.getClass().getMethod("b", nibbleArrayClass);
                b.invoke(cloned, cloneNibbleArray(skyLight));
            }
            return cloned;
        } catch (Throwable e) {
            throw new RuntimeException("Failed to clone chunk section", e);
        }
    }

    private Object cloneNibbleArray(Object original) throws Throwable {
        Method asBytes = original.getClass().getMethod("asBytes");
        byte[] data = (byte[]) ((byte[]) asBytes.invoke(original)).clone();
        return nibbleArrayClass.getConstructor(byte[].class).newInstance(data);
    }

    static {
        Lookup lookup = MethodHandles.lookup();
        try {
            chunkSectionClass = Class.forName(NMS + ".ChunkSection");
            nibbleArrayClass = Class.forName(NMS + ".NibbleArray");
            chunkClass = Class.forName(NMS + ".Chunk");

            Field nonEmptyBlockCount = chunkSectionClass.getDeclaredField("nonEmptyBlockCount");
            ReflectionUtils.makeFieldAccessible(nonEmptyBlockCount);
            nonEmptyBlockCountGetter = lookup.unreflectGetter(nonEmptyBlockCount);
            nonEmptyBlockCountSetter = lookup.unreflectSetter(nonEmptyBlockCount);

            Field tickingBlockCount = chunkSectionClass.getDeclaredField("tickingBlockCount");
            ReflectionUtils.makeFieldAccessible(tickingBlockCount);
            tickingBlockCountGetter = lookup.unreflectGetter(tickingBlockCount);
            tickingBlockCountSetter = lookup.unreflectSetter(tickingBlockCount);

            Field blockIds = chunkSectionClass.getDeclaredField("blockIds");
            ReflectionUtils.makeFieldAccessible(blockIds);
            blockIdsSetter = lookup.unreflectSetter(blockIds);

            Field sectionsField = chunkClass.getDeclaredField("sections");
            ReflectionUtils.makeFieldAccessible(sectionsField);
            sectionsSetter = lookup.unreflectSetter(sectionsField);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to initialize chunk adapter", e);
        }
    }
}
