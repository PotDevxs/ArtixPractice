package dev.artixdev.libs.com.github.retrooper.packetevents.util;

import java.util.Objects;
import dev.artixdev.libs.com.github.retrooper.packetevents.PacketEvents;
import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.BlockFace;

public class Vector3i {
   public final int x;
   public final int y;
   public final int z;

   public Vector3i() {
      this.x = 0;
      this.y = 0;
      this.z = 0;
   }

   public Vector3i(long val) {
      this(val, PacketEvents.getAPI().getServerManager().getVersion());
   }

   public Vector3i(long val, ServerVersion serverVersion) {
      int x = (int)(val >> 38);
      int y;
      int z;
      if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14)) {
         y = (int)(val << 52 >> 52);
         z = (int)(val << 26 >> 38);
      } else {
         y = (int)(val >> 26 & 4095L);
         z = (int)(val << 38 >> 38);
      }

      this.x = x;
      this.y = y;
      this.z = z;
   }

   public Vector3i(int x, int y, int z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public Vector3i(int[] array) {
      if (array.length > 0) {
         this.x = array[0];
         if (array.length > 1) {
            this.y = array[1];
            if (array.length > 2) {
               this.z = array[2];
            } else {
               this.z = 0;
            }

         } else {
            this.y = 0;
            this.z = 0;
         }
      } else {
         this.x = 0;
         this.y = 0;
         this.z = 0;
      }
   }

   public long getSerializedPosition(ServerVersion serverVersion) {
      if (serverVersion.isNewerThanOrEquals(ServerVersion.V_1_17)) {
         long x = (long)(this.getX() & 67108863);
         long y = (long)(this.getY() & 4095);
         long z = (long)(this.getZ() & 67108863);
         return x << 38 | z << 12 | y;
      } else {
         return serverVersion.isNewerThanOrEquals(ServerVersion.V_1_14) ? (long)(this.getX() & 67108863) << 38 | (long)(this.getZ() & 67108863) << 12 | (long)(this.getY() & 4095) : (long)(this.getX() & 67108863) << 38 | (long)(this.getY() & 4095) << 26 | (long)(this.getZ() & 67108863);
      }
   }

   public long getSerializedPosition() {
      return this.getSerializedPosition(PacketEvents.getAPI().getServerManager().getVersion());
   }

   public int getX() {
      return this.x;
   }

   public int getY() {
      return this.y;
   }

   public int getZ() {
      return this.z;
   }

   public boolean equals(Object obj) {
      if (obj instanceof Vector3i) {
         Vector3i vec = (Vector3i)obj;
         return this.x == vec.x && this.y == vec.y && this.z == vec.z;
      } else if (obj instanceof Vector3d) {
         Vector3d vec = (Vector3d)obj;
         return (double)this.x == vec.x && (double)this.y == vec.y && (double)this.z == vec.z;
      } else if (!(obj instanceof Vector3f)) {
         return false;
      } else {
         Vector3f vec = (Vector3f)obj;
         return (float)this.x == vec.x && (float)this.y == vec.y && (float)this.z == vec.z;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.x, this.y, this.z});
   }

   public Vector3d toVector3d() {
      return new Vector3d((double)this.x, (double)this.y, (double)this.z);
   }

   public Vector3i add(int x, int y, int z) {
      return new Vector3i(this.x + x, this.y + y, this.z + z);
   }

   public Vector3i add(Vector3i other) {
      return this.add(other.x, other.y, other.z);
   }

   public Vector3i offset(BlockFace face) {
      return this.add(face.getModX(), face.getModY(), face.getModZ());
   }

   public Vector3i subtract(int x, int y, int z) {
      return new Vector3i(this.x + x, this.y + y, this.z + z);
   }

   public Vector3i subtract(Vector3i other) {
      return this.subtract(other.x, other.y, other.z);
   }

   public Vector3i multiply(int x, int y, int z) {
      return new Vector3i(this.x * x, this.y * y, this.z * z);
   }

   public Vector3i multiply(Vector3i other) {
      return this.multiply(other.x, other.y, other.z);
   }

   public Vector3i multiply(int value) {
      return this.multiply(value, value, value);
   }

   public Vector3i crossProduct(Vector3i other) {
      int newX = this.y * other.z - other.y * this.z;
      int newY = this.z * other.x - other.z * this.x;
      int newZ = this.x * other.y - other.x * this.y;
      return new Vector3i(newX, newY, newZ);
   }

   public int dot(Vector3i other) {
      return this.x * other.x + this.y * other.y + this.z * other.z;
   }

   public Vector3i with(Integer x, Integer y, Integer z) {
      return new Vector3i(x == null ? this.x : x, y == null ? this.y : y, z == null ? this.z : z);
   }

   public Vector3i withX(int x) {
      return new Vector3i(x, this.y, this.z);
   }

   public Vector3i withY(int y) {
      return new Vector3i(this.x, y, this.z);
   }

   public Vector3i withZ(int z) {
      return new Vector3i(this.x, this.y, z);
   }

   public String toString() {
      return "X: " + this.x + ", Y: " + this.y + ", Z: " + this.z;
   }

   public static Vector3i zero() {
      return new Vector3i();
   }
}
