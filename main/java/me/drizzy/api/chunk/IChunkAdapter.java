package me.drizzy.api.chunk;

import org.bukkit.Chunk;

public interface IChunkAdapter {
   void cacheChunk(Chunk var1);

   void restoreChunk(Chunk var1);
}
