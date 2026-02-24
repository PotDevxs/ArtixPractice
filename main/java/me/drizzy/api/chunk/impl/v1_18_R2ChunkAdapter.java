package me.drizzy.api.chunk.impl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import me.drizzy.api.chunk.IChunkAdapter;
import org.bukkit.Chunk;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import dev.artixdev.practice.utils.ReflectionUtils;

/**
 * Chunk adapter implementation for Minecraft version 1.18 R2.
 * Handles caching and restoring chunk sections using NMS reflection.
 */
public class v1_18_R2ChunkAdapter implements IChunkAdapter {

    private static final String NMS_CHUNK = "net.minecraft.world.level.chunk";

    private final Map<Long, Object[]> chunkSectionMap = new Long2ObjectOpenHashMap<>();

    private static Class<?> chunkSectionClass;
    private static Class<?> chunkAccessClass;
    private static MethodHandle fieldEGetter;
    private static MethodHandle fieldESetter;
    private static MethodHandle fieldFGetter;
    private static MethodHandle fieldFSetter;
    private static MethodHandle fieldGGetter;
    private static MethodHandle fieldGSetter;
    private static MethodHandle sectionsSetter;

    @Override
    public void cacheChunk(Chunk chunk) {
        if (!chunk.isLoaded()) {
            chunk.load();
        }
        try {
            Object nmsChunk = chunk.getClass().getMethod("getHandle").invoke(chunk);
            Object pos = nmsChunk.getClass().getMethod("f").invoke(nmsChunk);
            long key = (Long) pos.getClass().getMethod("a").invoke(pos);
            Object[] sections = (Object[]) nmsChunk.getClass().getMethod("d").invoke(nmsChunk);
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
            Object pos = nmsChunk.getClass().getMethod("f").invoke(nmsChunk);
            long key = (Long) pos.getClass().getMethod("a").invoke(pos);
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
            Method g = original.getClass().getMethod("g");
            Method i = original.getClass().getMethod("i");
            Method j = original.getClass().getMethod("j");
            int y = (Integer) g.invoke(original) >> 4;
            Object argI = i.invoke(original);
            Object argJ = j.invoke(original);
            Constructor<?> ctor = null;
            for (Constructor<?> c : chunkSectionClass.getDeclaredConstructors()) {
                if (c.getParameterCount() == 3) {
                    ctor = c;
                    break;
                }
            }
            if (ctor == null) {
                throw new RuntimeException("ChunkSection 3-arg constructor not found");
            }
            ctor.setAccessible(true);
            Object newSection = ctor.newInstance(y, argI, argJ);
            fieldESetter.invoke(newSection, fieldEGetter.invoke(original));
            fieldFSetter.invoke(newSection, fieldFGetter.invoke(original));
            fieldGSetter.invoke(newSection, fieldGGetter.invoke(original));
            return newSection;
        } catch (Throwable e) {
            throw new RuntimeException("Failed to clone chunk section", e);
        }
    }

    static {
        Lookup lookup = MethodHandles.lookup();
        try {
            chunkSectionClass = Class.forName(NMS_CHUNK + ".ChunkSection");
            chunkAccessClass = Class.forName(NMS_CHUNK + ".IChunkAccess");

            Field fieldE = chunkSectionClass.getDeclaredField("f");
            ReflectionUtils.makeFieldAccessible(fieldE);
            fieldEGetter = lookup.unreflectGetter(fieldE);
            fieldESetter = lookup.unreflectSetter(fieldE);

            Field fieldF = chunkSectionClass.getDeclaredField("g");
            ReflectionUtils.makeFieldAccessible(fieldF);
            fieldFGetter = lookup.unreflectGetter(fieldF);
            fieldFSetter = lookup.unreflectSetter(fieldF);

            Field fieldG = chunkSectionClass.getDeclaredField("h");
            ReflectionUtils.makeFieldAccessible(fieldG);
            fieldGGetter = lookup.unreflectGetter(fieldG);
            fieldGSetter = lookup.unreflectSetter(fieldG);

            Field sectionsField = chunkAccessClass.getDeclaredField("k");
            ReflectionUtils.makeFieldAccessible(sectionsField);
            sectionsSetter = lookup.unreflectSetter(sectionsField);
        } catch (Throwable e) {
            throw new RuntimeException("Failed to initialize chunk adapter", e);
        }
    }
}
