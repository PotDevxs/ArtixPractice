package dev.artixdev.libs.com.github.retrooper.packetevents.protocol;

public enum ConnectionState {
   HANDSHAKING,
   STATUS,
   LOGIN,
   PLAY,
   CONFIGURATION;

   public static ConnectionState getById(int id) {
      return id < values().length && id >= 0 ? values()[id] : null;
   }

   // $FF: synthetic method
   private static ConnectionState[] $values() {
      return new ConnectionState[]{HANDSHAKING, STATUS, LOGIN, PLAY, CONFIGURATION};
   }
}
