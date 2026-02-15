package dev.artixdev.practice.utils.cuboid;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

/**
 * Represents a cuboid region in a Minecraft world.
 * A cuboid is defined by two opposite corners (x1,y1,z1) and (x2,y2,z2).
 * 
 * @author RefineDev
 */
public class Cuboid implements Cloneable, Iterable<Block> {
   private int x1;
   private int y1;
   private int z1;
   private int x2;
   private int y2;
   private int z2;
   private String worldName;
   private transient List<Chunk> chunks;

   /**
    * Creates a new cuboid from world name and coordinates.
    * Coordinates are automatically normalized (min/max).
    */
   public Cuboid(String worldName, int x1, int y1, int z1, int x2, int y2, int z2) {
      this.worldName = worldName;
      this.x1 = Math.min(x1, x2);
      this.x2 = Math.max(x1, x2);
      this.y1 = Math.min(y1, y2);
      this.y2 = Math.max(y1, y2);
      this.z1 = Math.min(z1, z2);
      this.z2 = Math.max(z1, z2);
      this.chunks = new ArrayList<Chunk>();
   }

   /**
    * Creates a new cuboid from two locations.
    */
   public Cuboid(Location loc1, Location loc2) {
      this(loc1.getWorld().getName(), 
           loc1.getBlockX(), loc1.getBlockY(), loc1.getBlockZ(),
           loc2.getBlockX(), loc2.getBlockY(), loc2.getBlockZ());
   }

   /**
    * Creates a new cuboid from world and coordinates.
    */
   public Cuboid(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
      this(world.getName(), x1, y1, z1, x2, y2, z2);
   }

   // ========== Getters ==========

   public int getLowerX() {
      return this.x1;
   }

   public int getUpperX() {
      return this.x2;
   }

   public int getLowerY() {
      return this.y1;
   }

   public int getUpperY() {
      return this.y2;
   }

   public int getLowerZ() {
      return this.z1;
   }

   public int getUpperZ() {
      return this.z2;
   }

   public int getX1() {
      return this.x1;
   }

   public int getX2() {
      return this.x2;
   }

   public int getY1() {
      return this.y1;
   }

   public int getY2() {
      return this.y2;
   }

   public int getZ1() {
      return this.z1;
   }

   public int getZ2() {
      return this.z2;
   }

   public String getWorldName() {
      return this.worldName;
   }

   public World getWorld() {
      World world = Bukkit.getWorld(this.worldName);
      if (world == null) {
         throw new IllegalStateException("World '" + this.worldName + "' is not loaded");
      }
      return world;
   }

   // ========== Setters ==========

   public void setX1(int x1) {
      this.x1 = x1;
   }

   public void setX2(int x2) {
      this.x2 = x2;
   }

   public void setY1(int y1) {
      this.y1 = y1;
   }

   public void setY2(int y2) {
      this.y2 = y2;
   }

   public void setZ1(int z1) {
      this.z1 = z1;
   }

   public void setZ2(int z2) {
      this.z2 = z2;
   }

   public void setWorldName(String worldName) {
      this.worldName = worldName;
   }

   public void setChunks(List<Chunk> chunks) {
      this.chunks = chunks;
   }

   // ========== Size Methods ==========

   public int getSizeX() {
      return Math.abs(this.x2 - this.x1) + 1;
   }

   public int getSizeY() {
      return Math.abs(this.y2 - this.y1) + 1;
   }

   public int getSizeZ() {
      return Math.abs(this.z2 - this.z1) + 1;
   }

   public int getWidth() {
      return getSizeX();
   }

   public int getHeight() {
      return getSizeY();
   }

   public int getLength() {
      return getSizeZ();
   }

   public int getSize() {
      return getWidth() * getHeight() * getLength();
   }

   public int getVolume() {
      return getSize();
   }

   public int volume() {
      return getSize();
   }

   // ========== Contains Methods ==========

   /**
    * Checks if the cuboid contains the given coordinates.
    */
   public boolean contains(int x, int y, int z) {
      return x >= this.x1 && x <= this.x2 &&
             y >= this.y1 && y <= this.y2 &&
             z >= this.z1 && z <= this.z2;
   }

   /**
    * Checks if the cuboid contains the given x and z coordinates (ignoring y).
    */
   public boolean contains(int x, int z) {
      return x >= this.x1 && x <= this.x2 &&
             z >= this.z1 && z <= this.z2;
   }

   /**
    * Checks if the cuboid contains the given location.
    */
   public boolean contains(Location location) {
      if (location == null || !this.worldName.equals(location.getWorld().getName())) {
         return false;
      }
      return contains(location.getBlockX(), location.getBlockY(), location.getBlockZ());
   }

   /**
    * Checks if the cuboid contains the given block.
    */
   public boolean contains(Block block) {
      return contains(block.getLocation());
   }

   /**
    * Checks if the cuboid contains the given entity.
    */
   public boolean contains(Entity entity) {
      return contains(entity.getLocation());
   }

   /**
    * Checks if the cuboid contains the given chunk.
    */
   public boolean contains(Chunk chunk) {
      return contains(chunk.getX(), chunk.getZ());
   }

   // ========== Corner Methods ==========

   /**
    * Gets the lower corner (minimum coordinates) of the cuboid.
    */
   public Location getLowerCorner() {
      return new Location(getWorld(), this.x1, this.y1, this.z1);
   }

   /**
    * Gets the upper corner (maximum coordinates) of the cuboid.
    */
   public Location getUpperCorner() {
      return new Location(getWorld(), this.x2, this.y2, this.z2);
   }

   /**
    * Gets the center location of the cuboid.
    */
   public Location getCenter() {
      return new Location(getWorld(),
         (this.x1 + this.x2) / 2.0,
         (this.y1 + this.y2) / 2.0,
         (this.z1 + this.z2) / 2.0);
   }

   /**
    * Gets all 8 corners of the cuboid.
    * Returns an array with 8 locations representing the corners.
    */
   public Location[] getCorners() {
      World world = getWorld();
      return new Location[] {
         new Location(world, this.x1, this.y1, this.z1), // Lower corner
         new Location(world, this.x2, this.y1, this.z1),
         new Location(world, this.x1, this.y2, this.z1),
         new Location(world, this.x2, this.y2, this.z1),
         new Location(world, this.x1, this.y1, this.z2),
         new Location(world, this.x2, this.y1, this.z2),
         new Location(world, this.x1, this.y2, this.z2),
         new Location(world, this.x2, this.y2, this.z2)  // Upper corner
      };
   }

   /**
    * Gets the lower corner as a Vector.
    */
   public Vector getLowerCornerVector() {
      return new Vector(this.x1, this.y1, this.z1);
   }

   /**
    * Gets the upper corner as a Vector.
    */
   public Vector getUpperCornerVector() {
      return new Vector(this.x2, this.y2, this.z2);
   }

   // ========== Block Methods ==========

   /**
    * Gets a block relative to the lower corner of the cuboid.
    */
   public Block getRelativeBlock(int x, int y, int z) {
      return getWorld().getBlockAt(this.x1 + x, this.y1 + y, this.z1 + z);
   }

   /**
    * Gets a block relative to the lower corner of the cuboid in the specified world.
    */
   public Block getRelativeBlock(World world, int x, int y, int z) {
      return world.getBlockAt(this.x1 + x, this.y1 + y, this.z1 + z);
   }

   // ========== Iterator ==========

   /**
    * Returns an iterator over all blocks in the cuboid.
    */
   @Override
   public Iterator<Block> iterator() {
      return new CuboidBlockIterator(getWorld(), this.x1, this.y1, this.z1, this.x2, this.y2, this.z2);
   }

   // ========== Chunk Methods ==========

   /**
    * Gets all chunks that intersect with this cuboid.
    * Chunks are loaded if they are not already loaded.
    */
   public List<Chunk> getChunks() {
      if (this.chunks == null || this.chunks.isEmpty()) {
         this.chunks = new ArrayList<Chunk>();
         World world = getWorld();
         int minChunkX = this.x1 >> 4;
         int maxChunkX = this.x2 >> 4;
         int minChunkZ = this.z1 >> 4;
         int maxChunkZ = this.z2 >> 4;
         
         for (int x = minChunkX; x <= maxChunkX; x++) {
            for (int z = minChunkZ; z <= maxChunkZ; z++) {
               Chunk chunk = world.getChunkAt(x, z);
               if (!chunk.isLoaded()) {
                  chunk.load();
               }
               this.chunks.add(chunk);
            }
         }
      }
      return this.chunks;
   }

   /**
    * Loads all chunks that intersect with this cuboid.
    * @return the number of chunks loaded
    */
   public int loadChunks() {
      List<Chunk> chunks = getChunks();
      return chunks.size();
   }

   /**
    * Unloads all chunks that were loaded for this cuboid.
    */
   public void unloadChunks() {
      if (this.chunks != null) {
         for (Chunk chunk : this.chunks) {
            if (chunk.isLoaded()) {
               chunk.unload();
            }
         }
         this.chunks.clear();
      }
   }

   // ========== Expansion Methods ==========

   /**
    * Expands the cuboid in the given direction by the specified amount.
    * Returns a new cuboid instance.
    */
   public Cuboid expand(CuboidDirection direction, int amount) {
      switch (direction) {
         case NORTH:
            return new Cuboid(this.worldName, this.x1, this.y1, this.z1 - amount, this.x2, this.y2, this.z2);
         case SOUTH:
            return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z2 + amount);
         case EAST:
            return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2 + amount, this.y2, this.z2);
         case WEST:
            return new Cuboid(this.worldName, this.x1 - amount, this.y1, this.z1, this.x2, this.y2, this.z2);
         case UP:
            return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2 + amount, this.z2);
         case DOWN:
            return new Cuboid(this.worldName, this.x1, this.y1 - amount, this.z1, this.x2, this.y2, this.z2);
         case HORIZONTAL:
            return expand(CuboidDirection.NORTH, amount)
                  .expand(CuboidDirection.SOUTH, amount)
                  .expand(CuboidDirection.EAST, amount)
                  .expand(CuboidDirection.WEST, amount);
         case VERTICAL:
            return expand(CuboidDirection.UP, amount)
                  .expand(CuboidDirection.DOWN, amount);
         case BOTH:
            return expand(CuboidDirection.HORIZONTAL, amount)
                  .expand(CuboidDirection.VERTICAL, amount);
         case EASY:
            // EASY is treated as EAST in the original code
            return expand(CuboidDirection.EAST, amount);
         default:
            return this;
      }
   }

   /**
    * Insets (contracts) the cuboid in the given direction by the specified amount.
    * Returns a new cuboid instance.
    */
   public Cuboid inset(CuboidDirection direction, int amount) {
      return expand(direction, -amount);
   }

   /**
    * Outsets (expands) the cuboid in the given direction by the specified amount.
    * Only works with HORIZONTAL, VERTICAL, or BOTH directions.
    * Returns a new cuboid instance.
    */
   public Cuboid outset(CuboidDirection direction, int amount) {
      switch (direction) {
         case HORIZONTAL:
            return expand(CuboidDirection.NORTH, amount)
                  .expand(CuboidDirection.SOUTH, amount)
                  .expand(CuboidDirection.EAST, amount)
                  .expand(CuboidDirection.WEST, amount);
         case VERTICAL:
            return expand(CuboidDirection.UP, amount)
                  .expand(CuboidDirection.DOWN, amount);
         case BOTH:
            return expand(CuboidDirection.HORIZONTAL, amount)
                  .expand(CuboidDirection.VERTICAL, amount);
         default:
            throw new IllegalArgumentException("Invalid direction for outset: " + direction);
      }
   }

   /**
    * Shifts the cuboid in the given direction by the specified amount.
    * This expands in one direction and contracts in the opposite direction.
    * Returns a new cuboid instance.
    */
   public Cuboid shift(CuboidDirection direction, int amount) {
      return expand(direction, amount).expand(direction.opposite(), -amount);
   }

   // ========== Face Methods ==========

   /**
    * Gets a face of the cuboid in the given direction.
    * Returns a new cuboid representing just that face.
    */
   public Cuboid getFace(CuboidDirection direction) {
      switch (direction) {
         case NORTH:
            return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y2, this.z1);
         case SOUTH:
            return new Cuboid(this.worldName, this.x1, this.y1, this.z2, this.x2, this.y2, this.z2);
         case EAST:
            return new Cuboid(this.worldName, this.x2, this.y1, this.z1, this.x2, this.y2, this.z2);
         case WEST:
            return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x1, this.y2, this.z2);
         case UP:
            return new Cuboid(this.worldName, this.x1, this.y2, this.z1, this.x2, this.y2, this.z2);
         case DOWN:
            return new Cuboid(this.worldName, this.x1, this.y1, this.z1, this.x2, this.y1, this.z2);
         default:
            return this;
      }
   }

   /**
    * Gets all four walls (faces) of the cuboid.
    * Returns an array with 4 cuboids representing NORTH, SOUTH, EAST, and WEST faces.
    */
   public Cuboid[] getWalls() {
      return new Cuboid[] {
         getFace(CuboidDirection.NORTH),
         getFace(CuboidDirection.SOUTH),
         getFace(CuboidDirection.EAST),
         getFace(CuboidDirection.WEST)
      };
   }

   // ========== Bounding Methods ==========

   /**
    * Gets the bounding cuboid that contains both this cuboid and the given cuboid.
    * Returns a new cuboid instance.
    */
   public Cuboid getBoundingCuboid(Cuboid other) {
      if (other == null) {
         return this;
      }
      int minX = Math.min(this.getLowerX(), other.getLowerX());
      int minY = Math.min(this.getLowerY(), other.getLowerY());
      int minZ = Math.min(this.getLowerZ(), other.getLowerZ());
      int maxX = Math.max(this.getUpperX(), other.getUpperX());
      int maxY = Math.max(this.getUpperY(), other.getUpperY());
      int maxZ = Math.max(this.getUpperZ(), other.getUpperZ());
      return new Cuboid(this.worldName, minX, minY, minZ, maxX, maxY, maxZ);
   }

   // ========== Clone ==========

   /**
    * Creates a deep copy of this cuboid.
    */
   @Override
   public Cuboid clone() {
      try {
         Cuboid cloned = (Cuboid) super.clone();
         cloned.chunks = new ArrayList<Chunk>(this.chunks);
         return cloned;
      } catch (CloneNotSupportedException e) {
         throw new Error(e);
      }
   }

   // ========== Equals and HashCode ==========

   /**
    * Checks if this cuboid equals another object.
    * Two cuboids are equal if they have the same coordinates and world name.
    */
   @Override
   public boolean equals(Object obj) {
      if (this == obj) {
         return true;
      }
      if (obj == null || !(obj instanceof Cuboid)) {
         return false;
      }
      Cuboid other = (Cuboid) obj;
      return this.x1 == other.x1 &&
             this.y1 == other.y1 &&
             this.z1 == other.z1 &&
             this.x2 == other.x2 &&
             this.y2 == other.y2 &&
             this.z2 == other.z2 &&
             (this.worldName == null ? other.worldName == null : this.worldName.equals(other.worldName));
   }

   /**
    * Returns the hash code for this cuboid.
    */
   @Override
   public int hashCode() {
      int result = 59;
      result = result * 59 + this.x1;
      result = result * 59 + this.y1;
      result = result * 59 + this.z1;
      result = result * 59 + this.x2;
      result = result * 59 + this.y2;
      result = result * 59 + this.z2;
      result = result * 59 + (this.worldName == null ? 43 : this.worldName.hashCode());
      return result;
   }

   /**
    * Checks if the given object can be equal to this cuboid.
    * Used by equals() method.
    */
   protected boolean canEqual(Object other) {
      return other instanceof Cuboid;
   }

   // ========== toString ==========

   /**
    * Returns a string representation of this cuboid.
    */
   @Override
   public String toString() {
      return "Cuboid{world=" + this.worldName +
             ",x1=" + this.x1 + ",y1=" + this.y1 + ",z1=" + this.z1 +
             ",x2=" + this.x2 + ",y2=" + this.y2 + ",z2=" + this.z2 + "}";
   }
}
