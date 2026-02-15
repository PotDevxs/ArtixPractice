package dev.artixdev.libs.com.github.retrooper.packetevents.util;

public class Quaternion4f {
   private float x;
   private float y;
   private float z;
   private float w;

   public Quaternion4f(float x, float y, float z, float w) {
      this.x = x;
      this.y = y;
      this.z = z;
      this.w = w;
   }

   public float getX() {
      return this.x;
   }

   public void setX(float x) {
      this.x = x;
   }

   public float getY() {
      return this.y;
   }

   public void setY(float y) {
      this.y = y;
   }

   public float getZ() {
      return this.z;
   }

   public void setZ(float z) {
      this.z = z;
   }

   public float getW() {
      return this.w;
   }

   public void setW(float w) {
      this.w = w;
   }
}
