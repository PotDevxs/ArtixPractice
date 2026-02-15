package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world;

import dev.artixdev.libs.com.github.retrooper.packetevents.util.Vector3d;

public class Location {
   private Vector3d position;
   private float yaw;
   private float pitch;

   public Location(Vector3d position, float yaw, float pitch) {
      this.position = position;
      this.yaw = yaw;
      this.pitch = pitch;
   }

   public Location(double x, double y, double z, float yaw, float pitch) {
      this(new Vector3d(x, y, z), yaw, pitch);
   }

   public Vector3d getPosition() {
      return this.position;
   }

   public double getX() {
      return this.position.getX();
   }

   public double getY() {
      return this.position.getY();
   }

   public double getZ() {
      return this.position.getZ();
   }

   public void setPosition(Vector3d position) {
      this.position = position;
   }

   public float getYaw() {
      return this.yaw;
   }

   public void setYaw(float yaw) {
      this.yaw = yaw;
   }

   public float getPitch() {
      return this.pitch;
   }

   public void setPitch(float pitch) {
      this.pitch = pitch;
   }

   public Location clone() {
      return new Location(this.position, this.yaw, this.pitch);
   }

   public String toString() {
      return "Location {[" + this.position.toString() + "], yaw: " + this.yaw + ", pitch: " + this.pitch + "}";
   }
}
