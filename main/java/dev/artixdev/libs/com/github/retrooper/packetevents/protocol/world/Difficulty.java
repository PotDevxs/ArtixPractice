package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.world;

public enum Difficulty {
   PEACEFUL,
   EASY,
   NORMAL,
   HARD;

   private static final Difficulty[] VALUES = values();

   public int getId() {
      return this.ordinal();
   }

   public static Difficulty getById(int id) {
      return VALUES[id];
   }

   // $FF: synthetic method
   private static Difficulty[] $values() {
      return new Difficulty[]{PEACEFUL, EASY, NORMAL, HARD};
   }
}
