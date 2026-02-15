package me.drizzy.api.chunk.impl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.util.Map;
import me.drizzy.api.chunk.IChunkAdapter;
import net.minecraft.server.v1_16_R3.ChunkSection;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.libs.it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import org.bukkit.craftbukkit.v1_16_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import dev.artixdev.practice.llIllIlIIlIIlII.IIlllIlIIlIIlII;

public class v1_16_R3ChunkAdapter implements IChunkAdapter {
   private final Map<Long, ChunkSection[]> chunkSectionMap = new Long2ObjectOpenHashMap();
   private static MethodHandle nonEmptyBlockCountGetter;
   private static MethodHandle tickingBlockCountGetter;
   private static MethodHandle blocksSetter;
   private static MethodHandle FIELD_E_GETTER;
   private static MethodHandle sectionsSetter;

   public void cacheChunk(Chunk chunk) {
      if (!chunk.isLoaded()) {
         chunk.load();
      }

      net.minecraft.server.v1_16_R3.Chunk nmsChunk = ((CraftChunk)chunk).getHandle();
      this.chunkSectionMap.put(nmsChunk.getPos().pair(), this.cloneSections(nmsChunk.getSections()));
   }

   public void restoreChunk(Chunk chunk) {
      try {
         if (!chunk.isLoaded()) {
            chunk.load();
         }

         net.minecraft.server.v1_16_R3.Chunk nmsChunk = ((CraftChunk)chunk).getHandle();
         long key = nmsChunk.getPos().pair();
         if (!this.chunkSectionMap.containsKey(key)) {
            throw new UnsupportedOperationException("Chunk invoked but not saved!");
         } else {
            ChunkSection[] sections = (ChunkSection[])this.chunkSectionMap.get(key);
            sectionsSetter.invoke(nmsChunk, sections);
            CraftWorld world = ((CraftChunk)chunk).getCraftWorld();
            world.refreshChunk(chunk.getX(), chunk.getZ());
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
         ChunkSection newSection = new ChunkSection(chunkSection.getYPosition(), nonEmptyBlockCountGetter.invoke(chunkSection), tickingBlockCountGetter.invoke(chunkSection), FIELD_E_GETTER.invoke(chunkSection));
         blocksSetter.invoke(newSection, chunkSection.getBlocks());
         return newSection;
      } catch (Throwable e) {
         throw e;
      }
   }

   static {
      Lookup lookup = MethodHandles.lookup();

      try {
         Field nonEmptyBlockCount = ChunkSection.class.getDeclaredField("nonEmptyBlockCount");
         IIlllIlIIlIIlII.lIIIIllIIlIIlII(nonEmptyBlockCount);
         nonEmptyBlockCountGetter = lookup.unreflectGetter(nonEmptyBlockCount);
         Field tickingBlockCount = ChunkSection.class.getDeclaredField("tickingBlockCount");
         IIlllIlIIlIIlII.lIIIIllIIlIIlII(tickingBlockCount);
         tickingBlockCountGetter = lookup.unreflectGetter(tickingBlockCount);
         Field blockIds = ChunkSection.class.getDeclaredField("blockIds");
         IIlllIlIIlIIlII.lIIIIllIIlIIlII(blockIds);
         blocksSetter = lookup.unreflectSetter(blockIds);
         Field FIELD_E = ChunkSection.class.getDeclaredField("e");
         IIlllIlIIlIIlII.lIIIIllIIlIIlII(FIELD_E);
         FIELD_E_GETTER = lookup.unreflectGetter(FIELD_E);
         Field sections = net.minecraft.server.v1_16_R3.Chunk.class.getDeclaredField("sections");
         IIlllIlIIlIIlII.lIIIIllIIlIIlII(sections);
         sectionsSetter = lookup.unreflectSetter(sections);
      } catch (Throwable e) {
         e.printStackTrace();
      }

   }
}
