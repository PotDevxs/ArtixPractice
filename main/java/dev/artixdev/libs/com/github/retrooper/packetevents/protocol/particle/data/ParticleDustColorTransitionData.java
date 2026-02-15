package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.particle.data;

import dev.artixdev.libs.com.github.retrooper.packetevents.util.Vector3f;
import dev.artixdev.libs.com.github.retrooper.packetevents.wrapper.PacketWrapper;

public class ParticleDustColorTransitionData extends ParticleData {
   private float scale;
   private float startRed;
   private float startGreen;
   private float startBlue;
   private float endRed;
   private float endGreen;
   private float endBlue;

   public ParticleDustColorTransitionData(float scale, float startRed, float startGreen, float startBlue, float endRed, float endGreen, float endBlue) {
      this.scale = scale;
      this.startRed = startRed;
      this.startGreen = startGreen;
      this.startBlue = startBlue;
      this.endRed = endRed;
      this.endGreen = endGreen;
      this.endBlue = endBlue;
   }

   public ParticleDustColorTransitionData(float scale, Vector3f startRGB, Vector3f endRGB) {
      this(scale, startRGB.getX(), startRGB.getY(), startRGB.getZ(), endRGB.getX(), endRGB.getY(), endRGB.getZ());
   }

   public float getScale() {
      return this.scale;
   }

   public void setScale(float scale) {
      this.scale = scale;
   }

   public float getStartRed() {
      return this.startRed;
   }

   public void setStartRed(float startRed) {
      this.startRed = startRed;
   }

   public float getStartGreen() {
      return this.startGreen;
   }

   public void setStartGreen(float startGreen) {
      this.startGreen = startGreen;
   }

   public float getStartBlue() {
      return this.startBlue;
   }

   public void setStartBlue(float startBlue) {
      this.startBlue = startBlue;
   }

   public float getEndRed() {
      return this.endRed;
   }

   public void setEndRed(float endRed) {
      this.endRed = endRed;
   }

   public float getEndGreen() {
      return this.endGreen;
   }

   public void setEndGreen(float endGreen) {
      this.endGreen = endGreen;
   }

   public float getEndBlue() {
      return this.endBlue;
   }

   public void setEndBlue(float endBlue) {
      this.endBlue = endBlue;
   }

   public static ParticleDustColorTransitionData read(PacketWrapper<?> wrapper) {
      float startRed = wrapper.readFloat();
      float startGreen = wrapper.readFloat();
      float startBlue = wrapper.readFloat();
      float scale = wrapper.readFloat();
      float endRed = wrapper.readFloat();
      float endGreen = wrapper.readFloat();
      float endBlue = wrapper.readFloat();
      return new ParticleDustColorTransitionData(scale, startRed, startGreen, startBlue, endRed, endGreen, endBlue);
   }

   public static void write(PacketWrapper<?> wrapper, ParticleDustColorTransitionData data) {
      wrapper.writeFloat(data.getStartRed());
      wrapper.writeFloat(data.getStartGreen());
      wrapper.writeFloat(data.getStartBlue());
      wrapper.writeFloat(data.getScale());
      wrapper.writeFloat(data.getEndRed());
      wrapper.writeFloat(data.getEndGreen());
      wrapper.writeFloat(data.getEndBlue());
   }

   public boolean isEmpty() {
      return false;
   }
}
