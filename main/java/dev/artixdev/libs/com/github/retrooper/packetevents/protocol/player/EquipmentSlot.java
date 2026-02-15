package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player;

import dev.artixdev.libs.com.github.retrooper.packetevents.manager.server.ServerVersion;
import dev.artixdev.libs.org.jetbrains.annotations.Nullable;

public enum EquipmentSlot {
   MAIN_HAND(0),
   OFF_HAND(0),
   BOOTS(1),
   LEGGINGS(2),
   CHEST_PLATE(3),
   HELMET(4);

   private static final EquipmentSlot[] VALUES = values();
   private final byte legacyId;

   private EquipmentSlot(int legacyId) {
      this.legacyId = (byte)legacyId;
   }

   public int getId(ServerVersion version) {
      return version.isOlderThan(ServerVersion.V_1_9) ? this.legacyId : this.ordinal();
   }

   @Nullable
   public static EquipmentSlot getById(ServerVersion version, int id) {
      EquipmentSlot[] var2 = VALUES;
      int var3 = var2.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         EquipmentSlot slot = var2[var4];
         if (slot.getId(version) == id) {
            return slot;
         }
      }

      return null;
   }

   // $FF: synthetic method
   private static EquipmentSlot[] $values() {
      return new EquipmentSlot[]{MAIN_HAND, OFF_HAND, BOOTS, LEGGINGS, CHEST_PLATE, HELMET};
   }
}
