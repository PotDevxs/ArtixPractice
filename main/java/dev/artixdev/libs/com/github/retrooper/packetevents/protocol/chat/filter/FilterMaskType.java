package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.chat.filter;

public enum FilterMaskType {
   PASS_THROUGH,
   FULLY_FILTERED,
   PARTIALLY_FILTERED;

   public static final FilterMaskType[] VALUES = values();

   public int getId() {
      return this.ordinal();
   }

   public static FilterMaskType getById(int id) {
      return VALUES[id];
   }

   // $FF: synthetic method
   private static FilterMaskType[] $values() {
      return new FilterMaskType[]{PASS_THROUGH, FULLY_FILTERED, PARTIALLY_FILTERED};
   }
}
