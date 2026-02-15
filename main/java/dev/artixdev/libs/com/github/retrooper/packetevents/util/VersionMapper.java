package dev.artixdev.libs.com.github.retrooper.packetevents.util;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;

public class VersionMapper {
   private final ClientVersion[] versions;
   private final ClientVersion[] reversedVersions;

   public VersionMapper(ClientVersion... versions) {
      this.versions = versions;
      this.reversedVersions = new ClientVersion[versions.length];
      int index = 0;

      for(int i = versions.length - 1; i >= 0; --i) {
         this.reversedVersions[index] = versions[i];
         ++index;
      }

   }

   public ClientVersion[] getVersions() {
      return this.versions;
   }

   public ClientVersion[] getReversedVersions() {
      return this.reversedVersions;
   }

   public int getIndex(ClientVersion version) {
      int index = this.reversedVersions.length - 1;
      ClientVersion[] var3 = this.reversedVersions;
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         ClientVersion v = var3[var5];
         if (version.isNewerThanOrEquals(v)) {
            return index;
         }

         --index;
      }

      return 0;
   }
}
