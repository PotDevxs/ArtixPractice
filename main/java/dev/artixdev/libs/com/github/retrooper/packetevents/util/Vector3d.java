package dev.artixdev.libs.com.github.retrooper.packetevents.util;

import java.util.Objects;
import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world.BlockFace;

public class Vector3d {
   public final double x;
   public final double y;
   public final double z;

   public Vector3d() {
      this.x = 0.0D;
      this.y = 0.0D;
      this.z = 0.0D;
   }

   public Vector3d(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }

   public Vector3d(double[] array) {
      if (array.length > 0) {
         this.x = array[0];
         if (array.length > 1) {
            this.y = array[1];
            if (array.length > 2) {
               this.z = array[2];
            } else {
               this.z = 0.0D;
            }

         } else {
            this.y = 0.0D;
            this.z = 0.0D;
         }
      } else {
         this.x = 0.0D;
         this.y = 0.0D;
         this.z = 0.0D;
      }
   }

   public double getX() {
      return this.x;
   }

   public double getY() {
      return this.y;
   }

   public double getZ() {
      return this.z;
   }

   public boolean equals(Object obj) {
      if (obj instanceof Vector3d) {
         Vector3d vec = (Vector3d)obj;
         return this.x == vec.x && this.y == vec.y && this.z == vec.z;
      } else if (obj instanceof Vector3f) {
         Vector3f vec = (Vector3f)obj;
         return this.x == (double)vec.x && this.y == (double)vec.y && this.z == (double)vec.z;
      } else if (!(obj instanceof Vector3i)) {
         return false;
      } else {
         Vector3i vec = (Vector3i)obj;
         return this.x == (double)vec.x && this.y == (double)vec.y && this.z == (double)vec.z;
      }
   }

   public int hashCode() {
      return Objects.hash(new Object[]{this.x, this.y, this.z});
   }

   public Vector3d add(double x, double y, double z) {
      return new Vector3d(this.x + x, this.y + y, this.z + z);
   }

   public Vector3d add(Vector3d other) {
      return this.add(other.x, other.y, other.z);
   }

   public Vector3d offset(BlockFace face) {
      return this.add((double)face.getModX(), (double)face.getModY(), (double)face.getModZ());
   }

   public Vector3d subtract(double x, double y, double z) {
      return new Vector3d(this.x - x, this.y - y, this.z - z);
   }

   public Vector3d subtract(Vector3d other) {
      return this.subtract(other.x, other.y, other.z);
   }

   public Vector3d multiply(double x, double y, double z) {
      return new Vector3d(this.x * x, this.y * y, this.z * z);
   }

   public Vector3d multiply(Vector3d other) {
      return this.multiply(other.x, other.y, other.z);
   }

   public Vector3d multiply(double value) {
      return this.multiply(value, value, value);
   }

   public Vector3d crossProduct(Vector3d other) {
      double newX = this.y * other.z - other.y * this.z;
      double newY = this.z * other.x - other.z * this.x;
      double newZ = this.x * other.y - other.x * this.y;
      return new Vector3d(newX, newY, newZ);
   }

   public double dot(Vector3d other) {
      return this.x * other.x + this.y * other.y + this.z * other.z;
   }

   public Vector3d with(Double x, Double y, Double z) {
      return new Vector3d(x == null ? this.x : x, y == null ? this.y : y, z == null ? this.z : z);
   }

   public Vector3d withX(double x) {
      return new Vector3d(x, this.y, this.z);
   }

   public Vector3d withY(double y) {
      return new Vector3d(this.x, y, this.z);
   }

   public Vector3d withZ(double z) {
      return new Vector3d(this.x, this.y, z);
   }

   public double distance(Vector3d other) {
      return Math.sqrt(this.distanceSquared(other));
   }

   public double length() {
      return Math.sqrt(this.lengthSquared());
   }

   public double lengthSquared() {
      return this.x * this.x + this.y * this.y + this.z * this.z;
   }

   public Vector3d normalize() {
      double length = this.length();
      return new Vector3d(this.x / length, this.y / length, this.z / length);
   }

   public double distanceSquared(Vector3d other) {
      double distX = (this.x - other.x) * (this.x - other.x);
      double distY = (this.y - other.y) * (this.y - other.y);
      double distZ = (this.z - other.z) * (this.z - other.z);
      return distX + distY + distZ;
   }

   public Vector3i toVector3i() {
      return new Vector3i((int)this.x, (int)this.y, (int)this.z);
   }

   public String toString() {
      return "X: " + this.x + ", Y: " + this.y + ", Z: " + this.z;
   }

   public static Vector3d zero() {
      return new Vector3d();
   }
}
