package me.drizzy.api.chunk.impl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.util.Map;
import me.drizzy.api.chunk.IChunkAdapter;
import net.minecraft.server.v1_12_R1.ChunkSection;
import net.minecraft.server.v1_12_R1.NibbleArray;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.bukkit.craftbukkit.v1_12_R1.CraftChunk;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.util.LongHash;
import dev.artixdev.practice.llIllIlIIlIIlII.IIlllIlIIlIIlII;

public class v1_12_R1ChunkAdapter implements IChunkAdapter {
   private final Map<Long, ChunkSection[]> chunkSectionMap = new Long2ObjectOpenHashMap();
   private static MethodHandle nonEmptyBlockCountSetter;
   private static MethodHandle nonEmptyBlockCountGetter;
   private static MethodHandle tickingBlockCountSetter;
   private static MethodHandle tickingBlockCountGetter;
   private static MethodHandle blockIdsSetter;
   private static MethodHandle sectionsSetter;

   public void cacheChunk(Chunk chunk) {
      if (!chunk.isLoaded()) {
         chunk.load();
      }

      net.minecraft.server.v1_12_R1.Chunk nmsChunk = ((CraftChunk)chunk).getHandle();
      long key = LongHash.toLong(nmsChunk.locX, nmsChunk.locZ);
      this.chunkSectionMap.put(key, this.cloneSections(nmsChunk.getSections()));
   }

   public void restoreChunk(Chunk chunk) {
      try {
         if (!chunk.isLoaded()) {
            chunk.load();
         }

         net.minecraft.server.v1_12_R1.Chunk nmsChunk = ((CraftChunk)chunk).getHandle();
         long key = LongHash.toLong(nmsChunk.locX, nmsChunk.locZ);
         if (!this.chunkSectionMap.containsKey(key)) {
            throw new UnsupportedOperationException("Chunk invoked but not saved!");
         } else {
            ChunkSection[] sections = (ChunkSection[])this.chunkSectionMap.get(key);
            sectionsSetter.invoke(nmsChunk, sections);
            CraftWorld world = ((CraftChunk)chunk).getCraftWorld();
            world.refreshChunk(nmsChunk.locX, nmsChunk.locZ);
         }
      } catch (Throwable e) {
         throw e;
      }
   }

   public ChunkSection[] cloneSections(ChunkSection[] sections) {
      ChunkSection[] newSections = new ChunkSection[sections.length];

      for(int i = 0; i < sections.length; ++i) {
         if (sections[i] != null) {
            newSections[i] = this.cloneSection(sections[i]);
         }
      }

      return newSections;
   }

   public ChunkSection cloneSection(ChunkSection chunkSection) {
      try {
         ChunkSection section = new ChunkSection(chunkSection.getYPosition(), chunkSection.getSkyLightArray() != null);
         nonEmptyBlockCountSetter.invoke(section, nonEmptyBlockCountGetter.invoke(chunkSection));
         tickingBlockCountSetter.invoke(section, tickingBlockCountGetter.invoke(chunkSection));
         blockIdsSetter.invoke(section, chunkSection.getBlocks());
         if (chunkSection.getEmittedLightArray() != null) {
            section.a(this.cloneNibbleArray(chunkSection.getEmittedLightArray()));
         }

         if (chunkSection.getSkyLightArray() != null) {
            section.b(this.cloneNibbleArray(chunkSection.getSkyLightArray()));
         }

         return section;
      } catch (Throwable e) {
         throw e;
      }
   }

   public NibbleArray cloneNibbleArray(NibbleArray nibbleArray) {
      return new NibbleArray((byte[])nibbleArray.asBytes().clone());
   }

   static {
      Lookup lookup = MethodHandles.lookup();

      try {
         Field nonEmptyBlockCount = ChunkSection.class.getDeclaredField("nonEmptyBlockCount");
         nonEmptyBlockCount.setAccessible(true);
         nonEmptyBlockCountGetter = lookup.unreflectGetter(nonEmptyBlockCount);
         nonEmptyBlockCountSetter = lookup.unreflectSetter(nonEmptyBlockCount);
         Field tickingBlockCount = ChunkSection.class.getDeclaredField("tickingBlockCount");
         tickingBlockCount.setAccessible(true);
         tickingBlockCountGetter = lookup.unreflectGetter(tickingBlockCount);
         tickingBlockCountSetter = lookup.unreflectSetter(tickingBlockCount);
         Field blockIds = ChunkSection.class.getDeclaredField("blockIds");
         blockIds.setAccessible(true);
         blockIdsSetter = lookup.unreflectSetter(blockIds);
         Field sections = net.minecraft.server.v1_12_R1.Chunk.class.getDeclaredField("sections");
         IIlllIlIIlIIlII.lIIIIllIIlIIlII(sections);
         sectionsSetter = lookup.unreflectSetter(sections);
      } catch (Throwable e) {
         e.printStackTrace();
      }

   }
}
