package dev.artixdev.libs.com.github.retrooper.packetevents.protocol.player;

public enum GameMode {
   SURVIVAL,
   CREATIVE,
   ADVENTURE,
   SPECTATOR;

   private static final GameMode[] VALUES = values();

   public int getId() {
      return this.ordinal();
   }

   public static GameMode getById(int id) {
      return id >= 0 && id < VALUES.length ? VALUES[id] : SURVIVAL;
   }

   public static GameMode defaultGameMode() {
      return SURVIVAL;
   }

   // $FF: synthetic method
   private static GameMode[] $values() {
      return new GameMode[]{SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR};
   }
}
