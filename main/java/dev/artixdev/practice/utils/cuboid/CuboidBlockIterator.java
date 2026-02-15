package dev.artixdev.practice.utils.cuboid;

import java.util.Iterator;
import org.bukkit.World;
import org.bukkit.block.Block;

public class CuboidBlockIterator implements Iterator<Block> {
   private final int baseZ;
   private final int baseY;
   private int x;
   private final int sizeZ;
   private final int sizeX;
   private final int baseX;
   private final World world;
   private int y;
   private final int sizeY;
   private int z;

   public Block next() {
      if (!hasNext()) {
         throw new java.util.NoSuchElementException();
      }
      
      Block block = this.world.getBlockAt(
         this.baseX + this.x,
         this.baseY + this.y,
         this.baseZ + this.z
      );
      
      // Advance to next position
      this.x++;
      if (this.x >= this.sizeX) {
         this.x = 0;
         this.y++;
         if (this.y >= this.sizeY) {
            this.y = 0;
            this.z++;
         }
      }
      
      return block;
   }

   public int getX() {
      return this.x;
   }

   public void remove() throws UnsupportedOperationException {
      throw new UnsupportedOperationException();
   }

   public int getSizeZ() {
      return this.sizeZ;
   }

   public int getZ() {
      return this.z;
   }

   public int getBaseZ() {
      return this.baseZ;
   }

   public int getSizeY() {
      return this.sizeY;
   }

   public int getBaseY() {
      return this.baseY;
   }

   public int getBaseX() {
      return this.baseX;
   }

   public int getY() {
      return this.y;
   }

   CuboidBlockIterator(World world, int baseX, int baseY, int baseZ, int maxX, int maxY, int maxZ) {
      this.world = world;
      this.baseX = baseX;
      this.baseY = baseY;
      this.baseZ = baseZ;
      this.sizeX = Math.abs(maxX - baseX) + 1;
      this.sizeY = Math.abs(maxY - baseY) + 1;
      this.sizeZ = Math.abs(maxZ - baseZ) + 1;
      this.x = 0;
      this.y = 0;
      this.z = 0;
   }

   public int getSizeX() {
      return this.sizeX;
   }

   public World getWorld() {
      return this.world;
   }

   public boolean hasNext() {
      return this.z < this.sizeZ;
   }
}
