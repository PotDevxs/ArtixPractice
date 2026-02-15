package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world;

public enum BlockFace {
   DOWN(0, -1, 0),
   UP(0, 1, 0),
   NORTH(0, 0, -1),
   SOUTH(0, 0, 1),
   WEST(-1, 0, 0),
   EAST(1, 0, 0),
   OTHER((short)255, -1, -1, -1);

   private static final BlockFace[] VALUES = values();
   private static final BlockFace[] CARTESIAN_VALUES = new BlockFace[]{DOWN, UP, NORTH, SOUTH, WEST, EAST};
   final short faceValue;
   final int modX;
   final int modY;
   final int modZ;

   private BlockFace(short faceValue, int modX, int modY, int modZ) {
      this.faceValue = faceValue;
      this.modX = modX;
      this.modY = modY;
      this.modZ = modZ;
   }

   private BlockFace(int modX, int modY, int modZ) {
      this.faceValue = (short)this.ordinal();
      this.modX = modX;
      this.modY = modY;
      this.modZ = modZ;
   }

   public static BlockFace getLegacyBlockFaceByValue(int face) {
      return face == 255 ? OTHER : VALUES[face % VALUES.length];
   }

   public static BlockFace getBlockFaceByValue(int face) {
      return VALUES[face % VALUES.length];
   }

   public int getModX() {
      return this.modX;
   }

   public int getModY() {
      return this.modY;
   }

   public int getModZ() {
      return this.modZ;
   }

   public BlockFace getOppositeFace() {
      switch(this) {
      case DOWN:
         return UP;
      case UP:
         return DOWN;
      case NORTH:
         return SOUTH;
      case SOUTH:
         return NORTH;
      case WEST:
         return EAST;
      case EAST:
         return WEST;
      default:
         return OTHER;
      }
   }

   public BlockFace getCCW() {
      switch(this) {
      case NORTH:
         return WEST;
      case SOUTH:
         return EAST;
      case WEST:
         return SOUTH;
      case EAST:
         return NORTH;
      default:
         return OTHER;
      }
   }

   public BlockFace getCW() {
      switch(this) {
      case NORTH:
         return EAST;
      case SOUTH:
         return WEST;
      case WEST:
         return NORTH;
      case EAST:
         return SOUTH;
      default:
         return OTHER;
      }
   }

   public short getFaceValue() {
      return this.faceValue;
   }

   // $FF: synthetic method
   private static BlockFace[] $values() {
      return new BlockFace[]{DOWN, UP, NORTH, SOUTH, WEST, EAST, OTHER};
   }
}
