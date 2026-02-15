package dev.artixdev.libs.com.github.retrooper.packetevents.manager.server;

import dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player.ClientVersion;
import dev.artixdev.libs.org.jetbrains.annotations.NotNull;

public enum ServerVersion {
   V_1_7_10(5),
   V_1_8(47),
   V_1_8_3(47),
   V_1_8_8(47),
   V_1_9(107),
   V_1_9_2(109),
   V_1_9_4(110),
   V_1_10(210),
   V_1_10_1(210),
   V_1_10_2(210),
   V_1_11(315),
   V_1_11_2(316),
   V_1_12(335),
   V_1_12_1(338),
   V_1_12_2(340),
   V_1_13(393),
   V_1_13_1(401),
   V_1_13_2(404),
   V_1_14(477),
   V_1_14_1(480),
   V_1_14_2(485),
   V_1_14_3(490),
   V_1_14_4(498),
   V_1_15(573),
   V_1_15_1(575),
   V_1_15_2(578),
   V_1_16(735),
   V_1_16_1(736),
   V_1_16_2(751),
   V_1_16_3(753),
   V_1_16_4(754),
   V_1_16_5(754),
   V_1_17(755),
   V_1_17_1(756),
   V_1_18(757),
   V_1_18_1(757),
   V_1_18_2(758),
   V_1_19(759),
   V_1_19_1(760),
   V_1_19_2(760),
   V_1_19_3(761),
   V_1_19_4(762),
   V_1_20(763),
   V_1_20_1(763),
   V_1_20_2(764),
   V_1_20_3(765),
   V_1_20_4(765),
   ERROR(-1, true);

   private static final ServerVersion[] VALUES = values();
   private static final ServerVersion[] REVERSED_VALUES = values();
   private final int protocolVersion;
   private final String name;
   private ClientVersion toClientVersion;

   private ServerVersion(int protocolVersion) {
      this.protocolVersion = protocolVersion;
      this.name = this.name().substring(2).replace("_", ".");
   }

   private ServerVersion(int protocolVersion, boolean isNotRelease) {
      this.protocolVersion = protocolVersion;
      if (isNotRelease) {
         this.name = this.name();
      } else {
         this.name = this.name().substring(2).replace("_", ".");
      }

   }

   public static ServerVersion[] reversedValues() {
      return REVERSED_VALUES;
   }

   public static ServerVersion getLatest() {
      return REVERSED_VALUES[1];
   }

   public static ServerVersion getOldest() {
      return VALUES[0];
   }

   /** @deprecated */
   @Deprecated
   public static ServerVersion getById(int protocolVersion) {
      ServerVersion[] var1 = VALUES;
      int var2 = var1.length;

      for(int var3 = 0; var3 < var2; ++var3) {
         ServerVersion version = var1[var3];
         if (version.protocolVersion == protocolVersion) {
            return version;
         }
      }

      return null;
   }

   public ClientVersion toClientVersion() {
      if (this.toClientVersion == null) {
         this.toClientVersion = ClientVersion.getById(this.protocolVersion);
      }

      return this.toClientVersion;
   }

   public String getReleaseName() {
      return this.name;
   }

   public int getProtocolVersion() {
      return this.protocolVersion;
   }

   public boolean isNewerThan(ServerVersion target) {
      return this.ordinal() > target.ordinal();
   }

   public boolean isOlderThan(ServerVersion target) {
      return this.ordinal() < target.ordinal();
   }

   public boolean isNewerThanOrEquals(ServerVersion target) {
      return this.ordinal() >= target.ordinal();
   }

   public boolean isOlderThanOrEquals(ServerVersion target) {
      return this.ordinal() <= target.ordinal();
   }

   public boolean is(@NotNull VersionComparison comparison, @NotNull ServerVersion targetVersion) {
      switch(comparison) {
      case EQUALS:
         return this.protocolVersion == targetVersion.protocolVersion;
      case NEWER_THAN:
         return this.isNewerThan(targetVersion);
      case NEWER_THAN_OR_EQUALS:
         return this.isNewerThanOrEquals(targetVersion);
      case OLDER_THAN:
         return this.isOlderThan(targetVersion);
      case OLDER_THAN_OR_EQUALS:
         return this.isOlderThanOrEquals(targetVersion);
      default:
         return false;
      }
   }

   // $FF: synthetic method
   private static ServerVersion[] $values() {
      return new ServerVersion[]{V_1_7_10, V_1_8, V_1_8_3, V_1_8_8, V_1_9, V_1_9_2, V_1_9_4, V_1_10, V_1_10_1, V_1_10_2, V_1_11, V_1_11_2, V_1_12, V_1_12_1, V_1_12_2, V_1_13, V_1_13_1, V_1_13_2, V_1_14, V_1_14_1, V_1_14_2, V_1_14_3, V_1_14_4, V_1_15, V_1_15_1, V_1_15_2, V_1_16, V_1_16_1, V_1_16_2, V_1_16_3, V_1_16_4, V_1_16_5, V_1_17, V_1_17_1, V_1_18, V_1_18_1, V_1_18_2, V_1_19, V_1_19_1, V_1_19_2, V_1_19_3, V_1_19_4, V_1_20, V_1_20_1, V_1_20_2, V_1_20_3, V_1_20_4, ERROR};
   }

   static {
      int i = 0;

      ServerVersion tmp;
      for(int j = REVERSED_VALUES.length - 1; j > i; REVERSED_VALUES[i++] = tmp) {
         tmp = REVERSED_VALUES[j];
         REVERSED_VALUES[j--] = REVERSED_VALUES[i];
      }

   }
}
