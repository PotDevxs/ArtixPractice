package me.drizzy.api.chunk.impl;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.util.Map;
import me.drizzy.api.chunk.IChunkAdapter;
import net.minecraft.world.level.chunk.ChunkSection;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.DataPaletteBlock;
import net.minecraft.world.level.chunk.IChunkAccess;
import org.bukkit.Chunk;
import org.bukkit.craftbukkit.v1_19_R3.CraftChunk;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import dev.artixdev.libs.it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import dev.artixdev.practice.llIllIlIIlIIlII.IIlllIlIIlIIlII;

public class v1_19_R3ChunkAdapter implements IChunkAdapter {
   private final Map<Long, ChunkSection[]> chunkSectionMap = new Long2ObjectOpenHashMap();
   private static MethodHandle FIELD_E_GETTER;
   private static MethodHandle FIELD_E_SETTER;
   private static MethodHandle FIELD_F_GETTER;
   private static MethodHandle FIELD_F_SETTER;
   private static MethodHandle FIELD_G_GETTER;
   private static MethodHandle FIELD_G_SETTER;
   private static MethodHandle sectionsSetter;

   public void cacheChunk(Chunk chunk) {
      if (!chunk.isLoaded()) {
         chunk.load();
      }

      IChunkAccess nmsChunk = ((CraftChunk)chunk).getHandle(ChunkStatus.o);
      this.chunkSectionMap.put(nmsChunk.f().a(), this.cloneSections(nmsChunk.d()));
   }

   public void restoreChunk(Chunk chunk) {
      try {
         if (!chunk.isLoaded()) {
            chunk.load();
         }

         IChunkAccess nmsChunk = ((CraftChunk)chunk).getHandle(ChunkStatus.o);
         long key = nmsChunk.f().a();
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
         ChunkSection newSection = new ChunkSection(chunkSection.g() >> 4, chunkSection.i(), (DataPaletteBlock)chunkSection.j());
         FIELD_E_SETTER.invoke(newSection, FIELD_E_GETTER.invoke(chunkSection));
         FIELD_F_SETTER.invoke(newSection, FIELD_F_GETTER.invoke(chunkSection));
         FIELD_G_SETTER.invoke(newSection, FIELD_G_GETTER.invoke(chunkSection));
         return newSection;
      } catch (Throwable e) {
         throw e;
      }
   }

   static {
      Lookup lookup = MethodHandles.lookup();

      try {
         Field fieldE = ChunkSection.class.getDeclaredField("f");
         IIlllIlIIlIIlII.lIIIIllIIlIIlII(fieldE);
         FIELD_E_GETTER = lookup.unreflectGetter(fieldE);
         FIELD_E_SETTER = lookup.unreflectSetter(fieldE);
         Field fieldF = ChunkSection.class.getDeclaredField("g");
         IIlllIlIIlIIlII.lIIIIllIIlIIlII(fieldF);
         FIELD_F_GETTER = lookup.unreflectGetter(fieldF);
         FIELD_F_SETTER = lookup.unreflectSetter(fieldF);
         Field fieldG = ChunkSection.class.getDeclaredField("h");
         IIlllIlIIlIIlII.lIIIIllIIlIIlII(fieldG);
         FIELD_G_GETTER = lookup.unreflectGetter(fieldG);
         FIELD_G_SETTER = lookup.unreflectSetter(fieldG);
         Field sections = IChunkAccess.class.getDeclaredField("k");
         IIlllIlIIlIIlII.lIIIIllIIlIIlII(sections);
         sectionsSetter = lookup.unreflectSetter(sections);
      } catch (Throwable e) {
         e.printStackTrace();
      }

   }
}
