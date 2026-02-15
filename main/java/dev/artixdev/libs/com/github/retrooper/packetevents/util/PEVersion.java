package dev.artixdev.libs.com.github.retrooper.packetevents.util;

import java.util.Arrays;

public class PEVersion {
   private final int[] versionIntArray;

   public PEVersion(int... version) {
      this.versionIntArray = version;
   }

   public PEVersion(String version) {
      String[] versionIntegers = version.split("\\.");
      int length = versionIntegers.length;
      this.versionIntArray = new int[length];

      for(int i = 0; i < length; ++i) {
         this.versionIntArray[i] = Integer.parseInt(versionIntegers[i]);
      }

   }

   public int compareTo(PEVersion version) {
      int localLength = this.versionIntArray.length;
      int oppositeLength = version.versionIntArray.length;
      int length = Math.max(localLength, oppositeLength);

      for(int i = 0; i < length; ++i) {
         int localInteger = i < localLength ? this.versionIntArray[i] : 0;
         int oppositeInteger = i < oppositeLength ? version.versionIntArray[i] : 0;
         if (localInteger > oppositeInteger) {
            return 1;
         }

         if (localInteger < oppositeInteger) {
            return -1;
         }
      }

      return 0;
   }

   public boolean isNewerThan(PEVersion version) {
      return this.compareTo(version) == 1;
   }

   public boolean isOlderThan(PEVersion version) {
      return this.compareTo(version) == -1;
   }

   public int[] asArray() {
      return this.versionIntArray;
   }

   public boolean equals(Object obj) {
      if (obj == null) {
         return false;
      } else {
         return obj instanceof PEVersion ? Arrays.equals(this.versionIntArray, ((PEVersion)obj).versionIntArray) : false;
      }
   }

   public PEVersion clone() {
      return new PEVersion(this.versionIntArray);
   }

   public String toString() {
      StringBuilder sb = (new StringBuilder(this.versionIntArray.length * 2 - 1)).append(this.versionIntArray[0]);

      for(int i = 1; i < this.versionIntArray.length; ++i) {
         sb.append(".").append(this.versionIntArray[i]);
      }

      return sb.toString();
   }
}
